package com.dangerousthings.nfc.fragments;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IEditFragment;
import com.dangerousthings.nfc.interfaces.ITracksPayloadSize;

public class EditPlainTextFragment extends Fragment implements IEditFragment
{
    private static final String ARG_RECORD = "record";

    NdefRecord _record;
    ITracksPayloadSize _trackerInterface;

    EditText mEditText;

    public EditPlainTextFragment()
    {
    }

    public static EditPlainTextFragment newInstance(NdefRecord record)
    {
        EditPlainTextFragment fragment = new EditPlainTextFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECORD, record);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditPlainTextFragment newInstance()
    {
        return new EditPlainTextFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            _record = getArguments().getParcelable(ARG_RECORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_edit_plain_text, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mEditText = view.findViewById(R.id.edit_plaintext_edittext);
        setupTextChangedEvent();
    }

    private void setupTextChangedEvent()
    {
        mEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                _trackerInterface.payloadChanged();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    @Override
    public void setPayloadTrackingInterface(ITracksPayloadSize tracker)
    {
        _trackerInterface = tracker;
    }

    @Override
    public NdefRecord getNdefRecord()
    {
        return NdefRecord.createMime(getString(R.string.mime_plaintext), new byte[]{});
    }

    @Override
    public int getPayloadSize()
    {
        if(mEditText.getText() == null)
        {
            return 0;
        }
        else
        {
            //TODO: Make this better
            return new NdefMessage(NdefRecord.createMime("text/plain", mEditText.getText().toString().getBytes())).getByteArrayLength();
        }
    }
}