package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.utilities.NdefUtils;

public class DisplayPlainTextFragment extends Fragment
{
    TextView mPlainText;
    ImageButton mBackButton;

    private static final String ARG_BYTES = "record";

    private byte[] _recordBytes;

    public DisplayPlainTextFragment()
    {
    }

    public static DisplayPlainTextFragment newInstance(byte[] bytes)
    {
        DisplayPlainTextFragment fragment = new DisplayPlainTextFragment();
        Bundle args = new Bundle();
        args.putByteArray(ARG_BYTES, bytes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            _recordBytes = getArguments().getByteArray(ARG_BYTES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_display_plain_text, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mPlainText = view.findViewById(R.id.display_plaintext_text);
        mPlainText.setText(NdefUtils.getEnStringFromBytes(_recordBytes));
        mBackButton = view.findViewById(R.id.display_plaintext_button_back);
        mBackButton.setOnClickListener(v -> requireActivity().onBackPressed());
    }
}