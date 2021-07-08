package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.enums.OnClickActionType;
import com.dangerousthings.nfc.interfaces.IClickListener;

public class RecordOptionsToolbar extends Fragment
{
    private static final String ARG_ENCRYPTED = "encryption_status";

    boolean _isEncrypted;
    IClickListener _clickListener;

    ImageButton mCloseButton;
    ImageButton mDeleteButton;
    ImageButton mEncryptButton;
    ImageButton mDecryptButton;

    public static RecordOptionsToolbar newInstance(boolean isEncrypted)
    {
        RecordOptionsToolbar fragment = new RecordOptionsToolbar();
        Bundle args = new Bundle();
        args.putBoolean(ARG_ENCRYPTED, isEncrypted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            _isEncrypted = getArguments().getBoolean(ARG_ENCRYPTED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.toolbar_record_options, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        mCloseButton = view.findViewById(R.id.record_options_button_close);
        mDeleteButton = view.findViewById(R.id.record_options_button_delete);
        mEncryptButton = view.findViewById(R.id.record_options_button_encrypt);
        mDecryptButton = view.findViewById(R.id.record_options_button_decrypt);
        mCloseButton.setOnClickListener(v -> _clickListener.onClick(OnClickActionType.cancel));
        mDeleteButton.setOnClickListener(v -> _clickListener.onClick(OnClickActionType.delete));
        mEncryptButton.setOnClickListener(v -> _clickListener.onClick(OnClickActionType.prompt_encryption_password));
        mDecryptButton.setOnClickListener(v -> _clickListener.onClick(OnClickActionType.prompt_decryption_password));

        if(_isEncrypted)
        {
            mEncryptButton.setVisibility(View.GONE);
            mDecryptButton.setVisibility(View.VISIBLE);
        }
    }

    public void setClickListener(IClickListener listener)
    {
        _clickListener = listener;
    }
}