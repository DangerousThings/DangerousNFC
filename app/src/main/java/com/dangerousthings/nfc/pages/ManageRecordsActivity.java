package com.dangerousthings.nfc.pages;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.NdefMessageRecyclerAdapter;
import com.dangerousthings.nfc.controls.DecryptionPasswordDialog;
import com.dangerousthings.nfc.controls.EncryptionPasswordDialog;
import com.dangerousthings.nfc.controls.EditRecordLabelDialog;
import com.dangerousthings.nfc.controls.RecordOptionsDialog;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.enums.OnClickActionType;
import com.dangerousthings.nfc.fragments.ViewRecordsToolbar;
import com.dangerousthings.nfc.interfaces.IClickListener;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.interfaces.IItemLongClickListener;
import com.dangerousthings.nfc.models.Implant;
import com.dangerousthings.nfc.utilities.EncryptionUtils;
import com.dangerousthings.nfc.utilities.NdefUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ManageRecordsActivity extends BaseActivity implements IItemLongClickListener, IClickListener
{
    public final static int REQ_CODE_RECORD = 1;
    public final static int REQ_CODE_VIEW_RECORD = 2;
    public final static int REQ_CODE_WRITE_MESSAGE = 3;

    NdefMessage _message;
    ArrayList<NdefRecord> _records;
    int _alteredIndex = 0;
    boolean _recordsEdited = false;
    Implant _implant;
    ViewRecordsToolbar _toolbar;
    NdefMessageRecyclerAdapter _recyclerAdapter;
    ActivityResultLauncher<Intent> _activityResultLauncher;
    DialogFragment _dialog;

    TextView mNoRecordsText;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);

        prepareActivity();
        setUpViews();
        setUpActivityResultLauncher();
        startViewRecordsToolbar();
    }

    private void prepareActivity()
    {
        mNoRecordsText = findViewById(R.id.view_records_text_no_records);

        //prepare an empty list for NdefRecords
        _records = new ArrayList<>();
        //pull in any passed in NdefMessages
        _message = getIntent().getParcelableExtra(getString(R.string.intent_ndef_message));
        if(_message != null)
        {
            //add NdefRecords to our global list
            Collections.addAll(_records, _message.getRecords());
            //accounts for empty tags
            _records.removeIf(record -> (record.toMimeType() == null));
            if(_records.size() > 0)
            {
                mNoRecordsText.setVisibility(View.GONE);
            }
            else
            {
                mNoRecordsText.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            mNoRecordsText.setVisibility(View.VISIBLE);
        }

        //Get the UID of the implant we're currently operating on if applicable
        String UID = getIntent().getStringExtra(getString(R.string.intent_tag_uid));
        if(UID != null)
        {
            //load in the working implant from the Room database if applicable
            ImplantDatabase database = ImplantDatabase.getInstance(this);
            IImplantDAO implantDAO = database.implantDAO();
            _implant = implantDAO.getImplantByUID(UID);
        }
    }

    //this creates a callback function following the new ActivityResult APIs google suggests rather than using startActivityForResult
    private void setUpActivityResultLauncher()
    {
        _activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent resultIntent = result.getData();
                        if(resultIntent != null)
                        {
                            //fetch the request code of the result intent
                            int requestCode = resultIntent.getIntExtra(getString(R.string.intent_request_code), -1);
                            //if the intent is returning a new or edited record
                            if(requestCode == REQ_CODE_RECORD)
                            {
                                NdefRecord record = Objects.requireNonNull(resultIntent.getExtras()).getParcelable(getString(R.string.intent_record));
                                //This is a little messy. I'll probably come back to it later to optimize a bit
                                if(record != null)
                                {
                                    try
                                    {
                                        if (_records.get(_alteredIndex) != null)
                                        {
                                            NdefRecord previousRecord = _records.get(_alteredIndex);
                                            //if a label has been set on this record
                                            if (NdefUtils.isRecordLabeled(previousRecord))
                                            {
                                                String label = NdefUtils.getRecordLabel(previousRecord);
                                                record = NdefUtils.generateLabeledRecord(label, record);
                                            }

                                            //update record at altered location
                                            _records.remove(_alteredIndex);
                                            _records.add(_alteredIndex, record);
                                        }
                                    } catch (Exception e)
                                    {
                                        _records.add(_alteredIndex, record);
                                    }
                                    updateRecords();
                                }
                            }
                            //if the edit button was clicked from the view record activity
                            else if(requestCode == REQ_CODE_VIEW_RECORD)
                            {
                                NdefRecord record = Objects.requireNonNull(resultIntent.getExtras()).getParcelable(getString(R.string.intent_record));
                                onEditButtonClick(record);
                            }
                            //if an NDEF message has been written
                            else if(requestCode == REQ_CODE_WRITE_MESSAGE)
                            {
                                ImplantDatabase database = ImplantDatabase.getInstance(this);
                                IImplantDAO implantDAO = database.implantDAO();
                                //checks to see if we have a loaded implant
                                if(_implant == null)
                                {
                                    //if we don't, then we try and see if one with the same UID of
                                    //the written tag exists in the database
                                    String uid = resultIntent.getStringExtra(getString(R.string.intent_tag_uid));
                                    _implant = implantDAO.getImplantByUID(uid);
                                }
                                //now we see if the tag is still null
                                if(_implant != null)
                                {
                                    //if its not, then we update the device's listing in database
                                    _implant.setNdefMessage(getNdefMessage());
                                    implantDAO.updateImplant(_implant);
                                }
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        }
                    }
                });
    }

    private void setUpViews()
    {
        mRecyclerView = findViewById(R.id.view_records_recycler);

        //set up recyclerview
        _recyclerAdapter = new NdefMessageRecyclerAdapter(this, _records);
        _recyclerAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(_recyclerAdapter);
    }

    private void onEditButtonClick(NdefRecord record)
    {
        Intent editRecord = new Intent(this, EditRecordActivity.class);
        editRecord.putExtra(getString(R.string.intent_record), record);
        if(_implant != null)
        {
            //if there is an implant loaded from the database that we are altering, pass on it's capacity
            editRecord.putExtra(getString(R.string.intent_ndef_capacity), _implant.getNdefCapacity());
        }
        openActivityForResult(editRecord, REQ_CODE_RECORD);
        overridePendingTransition(0, 0);
    }

    public void onNewRecordClick()
    {
        Intent addRecord = new Intent(this, EditRecordActivity.class);
        if(_implant != null)
        {
            addRecord.putExtra(getString(R.string.intent_ndef_capacity), _implant.getNdefCapacity());
        }
        openActivityForResult(addRecord, REQ_CODE_RECORD);
        overridePendingTransition(0, 0);
        //index gets set to the list size as the new record is placed at the end
        _alteredIndex = _records.size();
    }

    private void updateRecords()
    {
        _recyclerAdapter.notifyDataSetChanged();
        if(_records.size() > 0)
        {
            mNoRecordsText.setVisibility(View.GONE);
        }
        else
        {
            mNoRecordsText.setVisibility(View.VISIBLE);
        }
        _recordsEdited = true;
    }

    @Override
    public void onBackPressed()
    {
        if(_recordsEdited)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Discard Unsaved Changes?")
                    .setMessage("Are you sure you want to leave this page without writing changes to this implant?")
                    .setPositiveButton("Yes", ((dialog, which) ->
                    {
                        finish();
                        overridePendingTransition(0,0);
                    }))
                    .setNegativeButton("No", ((dialog, which) -> dialog.cancel()))
                    .show();
        }
        else
        {
            finish();
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onItemClick(int position)
    {
        //mark the selected record's index
        _alteredIndex = position;
        popFragmentStack();
        //get the clicked record
        NdefRecord record = _recyclerAdapter.getRecord(position);
        String mimeType = record.toMimeType();
        //first condition for encrypted mimetype
        if(mimeType.contains("$"))
        {
            //prompt for decryption password
            _dialog = DecryptionPasswordDialog.newInstance(true);
            ((DecryptionPasswordDialog)_dialog).setClickListener(this);
            _dialog.show(getSupportFragmentManager(), "DecryptionDialog");
        }
        else
        {
            viewRecord(record);
        }
    }

    //opens the record for viewing
    private void viewRecord(NdefRecord record)
    {
        Intent viewRecordIntent = new Intent(this, ViewRecordActivity.class);
        viewRecordIntent.putExtra(getString(R.string.intent_record), record);
        //opens for result so it can listen for the edit button
        openActivityForResult(viewRecordIntent, REQ_CODE_VIEW_RECORD);
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onItemLongClick(int position)
    {
        _alteredIndex = position;
        _dialog = RecordOptionsDialog.newInstance(EncryptionUtils.isRecordEncrypted(_recyclerAdapter.getRecord(position)));
        ((RecordOptionsDialog)_dialog).setClickListener(this);
        _dialog.show(getSupportFragmentManager(), "RecordOptionsDialog");
        return true;
    }

    private void writeRecords()
    {
        Intent writeIntent = new Intent(this, ImplantInterfaceActivity.class);
        writeIntent.putExtra(getString(R.string.intent_ndef_message), getNdefMessage());
        openActivityForResult(writeIntent, REQ_CODE_WRITE_MESSAGE);
        overridePendingTransition(0, 0);
    }

    public void openActivityForResult(Intent intent, int requestCode)
    {
        //store our result code for later retrieval
        intent.putExtra(getString(R.string.intent_request_code), requestCode);
        _activityResultLauncher.launch(intent);
    }

    private NdefMessage getNdefMessage()
    {
        if(_records.size()>0)
        {
            NdefRecord[] records = new NdefRecord[_records.size()];
            records = _records.toArray(records);
            return new NdefMessage(records);
        }
        else
        {
            NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_EMPTY, null, null, null);
            return new NdefMessage(ndefRecord);
        }
    }

    //pops the current fragment off.
    //used to pop the RecordOptions toolbar off the ViewRecords toolbar
    private void popFragmentStack()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    //cases for the different IClickListener interface conditions
    @Override
    public void onClick(OnClickActionType clickType)
    {
        if(clickType != null)
        {
            switch(clickType)
            {
                //back button is pressed from a fragment
                case back:
                    onBackPressed();
                    break;
                //the new record button is pressed from the ViewRecords toolbar
                case new_record:
                    onNewRecordClick();
                    break;
                //the write button is pressed from the ViewRecords toolbar
                case write:
                    writeRecords();
                    break;
                //the delete button is pressed from the RecordOptions toolbar
                case delete:
                    deleteRecord();
                    break;
                //the cancel button is pressed from one of the password prompt dialogs
                case cancel:
                    popFragmentStack();
                    break;
                //the ok button is pressed from the EncryptionPasswordDialog
                case encrypt_record:
                    encryptRecord();
                    popFragmentStack();
                    break;
                //the lock button is pressed from the RecordOptions toolbar
                case prompt_encryption_password:
                    promptForEncryption();
                    break;
                case prompt_decryption_password:
                //the unlock button is pressed from the RecordOptions toolbar
                    promptForDecryption(false);
                    break;
                //the ok button is pressed from the DecryptionPasswordDialog when told not to view the result
                case decrypt_record:
                    decryptRecord(false);
                    break;
                //the ok button is pressed from the DecryptionPasswordDialog when told TO view the result
                case decrypt_and_view:
                    decryptRecord(true);
                    break;
                case edit_label:
                    promptForRecordLabel();
                    break;
                case set_label:
                    setRecordLabel();
                    break;
            }
        }
    }

    private void setRecordLabel()
    {
        NdefRecord record = _records.get(_alteredIndex);
        record = NdefUtils.generateLabeledRecord(((EditRecordLabelDialog)_dialog).getRecordLabel(), record);
        _records.remove(_alteredIndex);
        _records.add(_alteredIndex, record);
        updateRecords();
        popFragmentStack();
    }

    private void decryptRecord(boolean view)
    {
        String decryptionPassword = ((DecryptionPasswordDialog)_dialog).getDecryptionPassword();
        NdefRecord record = _recyclerAdapter.getRecord(_alteredIndex);
        try
        {
            byte[] decryptedBytes = EncryptionUtils.decryptAES128Data(decryptionPassword, record.getPayload());
            record = NdefRecord.createMime(EncryptionUtils.getDecryptedMimeType(record.toMimeType()), decryptedBytes);
            if(view)
            {
                viewRecord(record);
            }
            else
            {
                _records.remove(_alteredIndex);
                _records.add(_alteredIndex, record);
                _recyclerAdapter.notifyDataSetChanged();
                popFragmentStack();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this, "ERROR: Decryption password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private void encryptRecord()
    {
        String encryptionPassword = ((EncryptionPasswordDialog)_dialog).getEncryptionPassword();
        try
        {
            NdefRecord unencryptedRecord = _recyclerAdapter.getRecord(_alteredIndex);
            byte[] encryptedBytes = EncryptionUtils.encryptDataWithAES128(encryptionPassword, unencryptedRecord.getPayload());
            NdefRecord encryptedRecord = NdefRecord.createMime(EncryptionUtils.getEncryptedMimeType(unencryptedRecord.toMimeType()), encryptedBytes);

            _records.remove(_alteredIndex);
            _records.add(_alteredIndex, encryptedRecord);
            updateRecords();
            popFragmentStack();
        }
        catch(Exception e)
        {
            Log.d("Encryption Error:", e.toString());
        }
    }

    private void promptForRecordLabel()
    {
        _dialog = EditRecordLabelDialog.newInstance();
        ((EditRecordLabelDialog)_dialog).setClickListener(this);
        _dialog.show(getSupportFragmentManager(), "EditRecordLabelDialog");
    }

    private void promptForEncryption()
    {
        _dialog = new EncryptionPasswordDialog();
        ((EncryptionPasswordDialog)_dialog).setClickListener(this);
        _dialog.show(getSupportFragmentManager(), "EncryptionDialog");
    }

    private void promptForDecryption(boolean viewDecryption)
    {
        _dialog = DecryptionPasswordDialog.newInstance(viewDecryption);
        ((DecryptionPasswordDialog)_dialog).setClickListener(this);
        _dialog.show(getSupportFragmentManager(), "DecryptionDialog");
    }

    private void deleteRecord()
    {
        new AlertDialog.Builder(this)
                .setTitle("Delete Record?")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Yes", ((dialog, which) ->
                {
                    _records.remove(_alteredIndex);
                    updateRecords();
                    popFragmentStack();
                }))
                .setNegativeButton("No", ((dialog, which) -> dialog.cancel()))
                .show();
    }

    private void startViewRecordsToolbar()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        _toolbar = new ViewRecordsToolbar();
        _toolbar.setClickListener(this);
        fragmentTransaction.replace(R.id.view_records_frame_toolbar, _toolbar);
        fragmentTransaction.commit();
    }
}