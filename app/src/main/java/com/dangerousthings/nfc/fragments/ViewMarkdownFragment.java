package com.dangerousthings.nfc.fragments;

import android.nfc.NdefRecord;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.utilities.NdefUtils;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;

public class ViewMarkdownFragment extends Fragment
{
    TextView mMarkdownText;

    private static final String ARG_RECORD = "record";

    private NdefRecord _record;

    public ViewMarkdownFragment()
    {
    }

    public static ViewMarkdownFragment newInstance(NdefRecord record)
    {
        ViewMarkdownFragment fragment = new ViewMarkdownFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECORD, record);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_view_text, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mMarkdownText = view.findViewById(R.id.view_text_textview);
        final Markwon markwon = Markwon.builder(requireActivity())
                .usePlugin(StrikethroughPlugin.create())
                .build();
        if(_record != null)
        {
            markwon.setMarkdown(mMarkdownText, NdefUtils.getStringFromBytes(_record.getPayload()));
        }
    }
}