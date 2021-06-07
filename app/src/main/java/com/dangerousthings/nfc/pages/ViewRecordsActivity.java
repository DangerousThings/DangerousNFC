package com.dangerousthings.nfc.pages;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.NdefMessageRecyclerAdapter;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.interfaces.IItemLongClickListener;
import com.dangerousthings.nfc.models.Implant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ViewRecordsActivity extends BaseActivity implements IItemLongClickListener
{
    public final static int REQ_CODE_RECORD = 1;
    public final static int REQ_CODE_VIEW_RECORD = 2;
    public final static int REQ_CODE_WRITE_MESSAGE = 3;

    NdefMessage _message;
    ArrayList<NdefRecord> _records;
    int _alteredIndex = 0;
    boolean _recordsEdited = false;
    Implant _implant;
    NdefMessageRecyclerAdapter _recyclerAdapter;
    ActivityResultLauncher<Intent> _activityResultLauncher;

    //UI elements
    RecyclerView mRecyclerView;
    ImageButton mBackButton;
    ImageButton mAddRecordButton;
    ImageButton mWriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);

        prepareActivity();
        setUpViews();
        setUpActivityResultLauncher();
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
                            int requestCode = resultIntent.getIntExtra(getString(R.string.intent_request_code), -1);
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
                                mWriteButton.setVisibility(View.VISIBLE);
                            }
                            else if(requestCode == REQ_CODE_VIEW_RECORD)
                            {
                                onEditButtonClick();
                            }
                            else if(requestCode == REQ_CODE_WRITE_MESSAGE)
                            {
                                ImplantDatabase database = ImplantDatabase.getInstance(this);
                                IImplantDAO implantDAO = database.implantDAO();
                                if(_implant == null)
                                {
                                    String uid = resultIntent.getStringExtra(getString(R.string.intent_tag_uid));
                                    _implant = implantDAO.getImplantByUID(uid);
                                }
                                if(_implant != null)
                                {
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
        mBackButton = findViewById(R.id.view_records_button_back);
        mAddRecordButton = findViewById(R.id.view_records_button_add);
        mWriteButton = findViewById(R.id.view_records_button_write);
        mWriteButton.setOnClickListener(v -> writeRecords());

        mAddRecordButton.setOnClickListener(v -> onNewRecordClick());

        //set up recyclerview
        _recyclerAdapter = new NdefMessageRecyclerAdapter(this, _records);
        _recyclerAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(_recyclerAdapter);

        mBackButton.setOnClickListener(v -> onBackPressed());
    }

    private void onEditButtonClick()
    {
        Intent editRecord = new Intent(this, EditNdefActivity.class);
        editRecord.putExtra(getString(R.string.intent_record), _recyclerAdapter.getRecord(_alteredIndex));
        if(_implant != null)
        {
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
        _alteredIndex = position;
        openActivityForResult(viewRecordIntent, REQ_CODE_VIEW_RECORD);
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onItemLongClick(int position)
    {
        new AlertDialog.Builder(this)
                .setTitle("Delete Record?")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Yes", ((dialog, which) ->
                {
                    _records.remove(position);
                    _recyclerAdapter.notifyDataSetChanged();
                    _recordsEdited = true;
                    mWriteButton.setVisibility(View.VISIBLE);
                }))
                .setNegativeButton("No", ((dialog, which) -> dialog.cancel()))
                .show();
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
        intent.putExtra(getString(R.string.intent_request_code), requestCode);
        _activityResultLauncher.launch(intent);
    }

    private NdefMessage getNdefMessage()
    {
        NdefRecord[] records = new NdefRecord[_records.size()];
        records = _records.toArray(records);
        return new NdefMessage(records);
    }
}