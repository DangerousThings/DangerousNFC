package com.dangerousthings.nfc.pages;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.NdefMessageRecyclerAdapter;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.fragments.DisplayPlainTextFragment;
import com.dangerousthings.nfc.interfaces.IItemClickListener;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.models.Implant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ViewRecordsActivity extends BaseActivity implements IItemClickListener
{
    public final static int REQ_CODE_RECORD = 1;

    NdefMessage _message;
    NdefMessage _stagingMessage;
    int _alteredIndex = 0;
    Implant _implant;

    //UI elements
    RecyclerView mRecyclerView;
    NdefMessageRecyclerAdapter _recyclerAdapter;
    ImageButton mBackButton;
    ImageButton mAddRecordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);

        //pull extras
        _message = getIntent().getParcelableExtra(getString(R.string.intent_ndef_message));
        _stagingMessage = _message;
        String UID = Objects.requireNonNull(getIntent().getExtras()).getString(getString(R.string.intent_tag_uid));
        if(UID != null)
        {
            ImplantDatabase database = ImplantDatabase.getInstance(this);
            IImplantDAO implantDAO = database.implantDAO();
            _implant = implantDAO.getImplantByUID(UID);
        }

        mRecyclerView = findViewById(R.id.view_records_recycler);
        mBackButton = findViewById(R.id.view_records_button_back);
        mAddRecordButton = findViewById(R.id.view_records_button_add);

        mAddRecordButton.setOnClickListener(v -> onNewRecordClick());

        //set up recyclerview
        _recyclerAdapter = new NdefMessageRecyclerAdapter(this, Arrays.asList(_message.getRecords()));
        _recyclerAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(_recyclerAdapter);

        mBackButton.setOnClickListener(v -> onBackPressed());
    }

    //TODO: rework this
    public void displayRecord(NdefRecord record)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment;
        //if plain text
        if(record.toMimeType().equals(getString(R.string.mime_plaintext)))
        {
            fragment = DisplayPlainTextFragment.newInstance(record);
        }
        //if the mimetype is not currently supported
        else
        {
            fragment = null;
            Toast.makeText(this, "Mime type not currently supported", Toast.LENGTH_SHORT).show();
        }

        if(fragment != null)
        {
            fragmentTransaction.replace(R.id.base_frame, fragment);
            fragmentTransaction.addToBackStack("displayNdef");
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            NdefRecord record = Objects.requireNonNull(data.getExtras()).getParcelable(getString(R.string.intent_record));
            List<NdefRecord> records = new ArrayList<>(Arrays.asList(_stagingMessage.getRecords()));
            records.add(record);
            NdefRecord[] recordArray = new NdefRecord[records.size()];
            recordArray = records.toArray(recordArray);
            _stagingMessage = new NdefMessage(recordArray);
            _recyclerAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        _alteredIndex = _stagingMessage.getRecords().length;
    }

    @Override
    public void onBackPressed()
    {
        //TODO: this doesn't work. redo this.
        if(_stagingMessage.equals(_message))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Discard Unchanged Changes?")
                    .setMessage("Are you sure you want to leave this page without writing changes to this implant?")
                    .setPositiveButton("Yes", ((dialog, which) ->
                    {
                        //TODO: handle the record returned from the fragment
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

    }
}