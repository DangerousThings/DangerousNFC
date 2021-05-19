package com.dangerousthings.nfc.fragments;

import android.nfc.NdefRecord;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;

public class EditPlainTextFragment extends Fragment
{
    private static final String ARG_RECORD = "record";

    NdefRecord _record;

    ImageButton mBackButton;

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
        mBackButton = view.findViewById(R.id.edit_plaintext_button_back);
        mBackButton.setOnClickListener(v -> requireActivity().onBackPressed());
    }
}