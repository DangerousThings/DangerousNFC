package com.dangerousthings.nfc.pages;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.dangerousthings.nfc.interfaces.IItemClickListener;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.models.Implant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ViewRecordsActivity extends BaseActivity implements IItemClickListener
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

        _records = new ArrayList<>();
        _message = getIntent().getParcelableExtra(getString(R.string.intent_ndef_message));
        if(_message != null)
        {
            Collections.addAll(_records, _message.getRecords());
        }

        String UID = getIntent().getStringExtra(getString(R.string.intent_tag_uid));
        if(UID != null)
        {
            ImplantDatabase database = ImplantDatabase.getInstance(this);
            IImplantDAO implantDAO = database.implantDAO();
            _implant = implantDAO.getImplantByUID(UID);
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == REQ_CODE_RECORD)
            {
                NdefRecord record = Objects.requireNonNull(data.getExtras()).getParcelable(getString(R.string.intent_record));
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
                finish();
                overridePendingTransition(0, 0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onEditButtonClick()
    {
        Intent editRecord = new Intent(this, EditNdefActivity.class);
        editRecord.putExtra(getString(R.string.intent_record), _recyclerAdapter.getRecord(_alteredIndex));
        if(_implant != null)
        {
            editRecord.putExtra(getString(R.string.intent_ndef_capacity), _implant.getNdefCapacity());
        }
        startActivityForResult(editRecord, REQ_CODE_RECORD);
        overridePendingTransition(0, 0);
    }

    public void onNewRecordClick()
    {
        Intent addRecord = new Intent(this, EditNdefActivity.class);
        if(_implant != null)
        {
            addRecord.putExtra(getString(R.string.intent_ndef_capacity), _implant.getNdefCapacity());
        }
        startActivityForResult(addRecord, REQ_CODE_RECORD);
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
        startActivityForResult(viewRecordIntent, REQ_CODE_VIEW_RECORD);
        overridePendingTransition(0, 0);
    }

    private void writeRecords()
    {
        Intent writeIntent = new Intent(this, ImplantInterfaceActivity.class);
        writeIntent.putExtra(getString(R.string.intent_ndef_message), getNdefMessage());
        startActivityForResult(writeIntent, REQ_CODE_WRITE_MESSAGE);
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode)
    {
        intent.putExtra(getString(R.string.intent_request_code), requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    private NdefMessage getNdefMessage()
    {
        NdefRecord[] records = new NdefRecord[_records.size()];
        records = _records.toArray(records);
        return new NdefMessage(records);
    }
}