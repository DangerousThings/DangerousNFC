package com.dangerousthings.nfc.pages;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.NdefMessageRecyclerAdapter;
import com.dangerousthings.nfc.interfaces.IItemClickListener;
import com.dangerousthings.nfc.utilities.NdefUtils;

import java.util.List;
import java.util.Objects;

public class NdefMessageActivity extends BaseActivity implements IItemClickListener
{
    List<NdefRecord> _message;

    //UI elements
    RecyclerView mRecyclerView;
    NdefMessageRecyclerAdapter _recyclerAdapter;
    ImageButton mBackButton;
    ImageButton mAddRecordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndef_message);

        mRecyclerView = findViewById(R.id.message_recycler_ndef_records);
        mBackButton = findViewById(R.id.message_button_back);
        mAddRecordButton = findViewById(R.id.message_button_add);

        mAddRecordButton.setOnClickListener(v -> addButtonClicked());

        _message = NdefUtils.getNdefRecordList((NdefMessage) Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get(getString(R.string.intent_ndef_message))));

        _recyclerAdapter = new NdefMessageRecyclerAdapter(this, _message);
        _recyclerAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(_recyclerAdapter);

        mBackButton.setOnClickListener(v -> onBackPressed());
    }

    private void addButtonClicked()
    {
        //TODO: push to a blank EditRecord activity
    }

    @Override
    public void onItemClick(int position)
    {

    }
}