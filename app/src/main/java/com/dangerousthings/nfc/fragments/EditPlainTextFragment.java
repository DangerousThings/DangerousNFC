package com.dangerousthings.nfc.fragments;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IEditFragment;
import com.dangerousthings.nfc.interfaces.ITracksPayloadSize;
import com.dangerousthings.nfc.utilities.NdefUtils;

public class EditPlainTextFragment extends Fragment implements IEditFragment
{
    private static final String ARG_RECORD = "record";

    NdefRecord _record;
    ITracksPayloadSize _tracker;

    EditText mEditText;
    LinearLayout mEditLinear;

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
        if(_record != null)
        {
            byte[] payload = _record.getPayload();
            if(payload != null)
            {
                mEditText.setText(NdefUtils.getEnStringFromBytes(payload));
            }
        }
        mEditLinear = view.findViewById(R.id.edit_plaintext_linear);
        mEditLinear.setOnClickListener(v -> focusEntry());
        _tracker.payloadChanged();

        setupTextChangedEvent();
    }

    private void focusEntry()
    {
        mEditText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager)requireActivity().getSystemService((Context.INPUT_METHOD_SERVICE));
        inputMethodManager.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
        int position = mEditText.length();
        Editable editable = mEditText.getText();
        Selection.setSelection(editable, position);
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
                _tracker.payloadChanged();
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
        _tracker = tracker;
    }

    @Override
    public NdefRecord getNdefRecord()
    {
        return NdefRecord.createTextRecord("en", mEditText.getText().toString());
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
            return new NdefMessage(getNdefRecord()).getByteArrayLength();
        }
    }
}