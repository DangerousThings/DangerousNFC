package com.dangerousthings.nfc.pages;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.NdefMessageRecyclerAdapter;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.enums.OnClickType;
import com.dangerousthings.nfc.fragments.RecordOptionsToolbar;
import com.dangerousthings.nfc.fragments.ViewRecordsToolbar;
import com.dangerousthings.nfc.interfaces.IClickListener;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.interfaces.IItemLongClickListener;
import com.dangerousthings.nfc.models.Implant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ViewRecordsActivity extends BaseActivity implements IItemLongClickListener, IClickListener
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

    //UI elements
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
        //prepare an empty list for NdefRecords
        _records = new ArrayList<>();
        //pull in any passed in NdefMessages
        _message = getIntent().getParcelableExtra(getString(R.string.intent_ndef_message));
        if(_message != null)
        {
            //add NdefRecords to our global list
            Collections.addAll(_records, _message.getRecords());
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
                                try
                                {
                                    if(_records.get(_alteredIndex) != null)
                                    {
                                        _records.remove(_alteredIndex);
                                        _records.add(_alteredIndex, record);
                                    }
                                }
                                catch(Exception e)
                                {
                                    _records.add(_alteredIndex, record);
                                }
                                _recyclerAdapter.notifyDataSetChanged();
                                _recordsEdited = true;
                                _toolbar.setWriteButtonVisible();
                            }
                            //if the edit button was clicked from the view record activity
                            else if(requestCode == REQ_CODE_VIEW_RECORD)
                            {
                                onEditButtonClick();
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

    private void onEditButtonClick()
    {
        Intent editRecord = new Intent(this, EditNdefActivity.class);
        editRecord.putExtra(getString(R.string.intent_record), _recyclerAdapter.getRecord(_alteredIndex));
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
        Intent addRecord = new Intent(this, EditNdefActivity.class);
        if(_implant != null)
        {
            addRecord.putExtra(getString(R.string.intent_ndef_capacity), _implant.getNdefCapacity());
        }
        openActivityForResult(addRecord, REQ_CODE_RECORD);
        overridePendingTransition(0, 0);
        //index gets set to the list size as the new record is placed at the end
        _alteredIndex = _records.size();
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
        Intent viewRecordIntent = new Intent(this, ViewRecordActivity.class);
        viewRecordIntent.putExtra(getString(R.string.intent_record), _recyclerAdapter.getRecord(position));
        //stores the index of the selected item in the case that it is to be edited
        _alteredIndex = position;
        openActivityForResult(viewRecordIntent, REQ_CODE_VIEW_RECORD);
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onItemLongClick(int position)
    {
        _alteredIndex = position;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecordOptionsToolbar optionsToolbar = RecordOptionsToolbar.newInstance(false);
        optionsToolbar.setClickListener(this);
        fragmentTransaction.replace(R.id.view_records_frame_toolbar, optionsToolbar);
        fragmentTransaction.addToBackStack("options_toolbar");
        fragmentTransaction.commit();
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
        NdefRecord[] records = new NdefRecord[_records.size()];
        records = _records.toArray(records);
        return new NdefMessage(records);
    }

    private void popFragmentStack()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public void onClick(OnClickType clickType)
    {
        if(clickType != null)
        {
            switch(clickType)
            {
                case back:
                    onBackPressed();
                    break;
                case new_record:
                    onNewRecordClick();
                    break;
                case write:
                    writeRecords();
                    break;
                case delete:
                    deleteRecord();
                    break;
                case cancel:
                    popFragmentStack();
                    break;
            }
        }
    }

    private void deleteRecord()
    {
        new AlertDialog.Builder(this)
                .setTitle("Delete Record?")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Yes", ((dialog, which) ->
                {
                    _records.remove(_alteredIndex);
                    _recyclerAdapter.notifyDataSetChanged();
                    _recordsEdited = true;
                    _toolbar.setWriteButtonVisible();
                }))
                .setNegativeButton("No", ((dialog, which) -> dialog.cancel()))
                .show();
        popFragmentStack();
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