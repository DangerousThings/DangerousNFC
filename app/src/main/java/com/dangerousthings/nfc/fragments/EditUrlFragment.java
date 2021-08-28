package com.dangerousthings.nfc.fragments;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IEditFragment;
import com.dangerousthings.nfc.interfaces.ITracksPayloadSize;
import com.dangerousthings.nfc.utilities.NdefUtils;

public class EditUrlFragment extends Fragment implements IEditFragment
{
    private static final String ARG_RECORD = "record";

    NdefRecord _record;
    ITracksPayloadSize _tracker;

    EditText mEditText;
    ConstraintLayout mMainConstraint;

    public EditUrlFragment()
    {
    }

    public static EditUrlFragment newInstance(NdefRecord record)
    {
        EditUrlFragment fragment = new EditUrlFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECORD, record);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditUrlFragment newInstance()
    {
        return new EditUrlFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            _record = getArguments().getParcelable(ARG_RECORD);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mEditText = view.findViewById(R.id.edit_url_edittext_url);
        mEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                _tracker.payloadChanged();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        mMainConstraint = view.findViewById(R.id.edit_url_constraint_main);
        mMainConstraint.setOnClickListener(v -> focusEntry());

        if(_record != null)
        {
            byte[] payload = _record.getPayload();
            if(payload != null)
            {
                String uri = NdefUtils.getUrlStringFromRecord(_record);
                mEditText.setText(uri);
            }
        }
    }

    private void focusEntry()
    {
        mEditText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
        int position = mEditText.length();
        Editable editable = mEditText.getText();
        Selection.setSelection(editable, position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_edit_url, container, false);
    }

    @Override
    public NdefRecord getNdefRecord()
    {
        try
        {
            return NdefRecord.createUri(mEditText.getText().toString());
        }
        catch(Exception e)
        {
            return null;
        }
    }

    @Override
    public int getPayloadSize()
    {
        if(getNdefRecord() != null)
        {
            return new NdefMessage(getNdefRecord()).getByteArrayLength();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public void setPayloadTrackingInterface(ITracksPayloadSize tracker)
    {
        _tracker = tracker;
    }

    @Override
    public String getDataTypeName()
    {
        return "URL";
    }
}
