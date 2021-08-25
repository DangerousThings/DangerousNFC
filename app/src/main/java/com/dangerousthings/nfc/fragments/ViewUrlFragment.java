package com.dangerousthings.nfc.fragments;

import android.nfc.NdefRecord;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.utilities.NdefUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ViewUrlFragment extends Fragment
{
    private static final String ARG_RECORD = "record";

    private NdefRecord _record;

    TextView mUrlText;
    Button mOpenUrlButton;

    public ViewUrlFragment()
    {
    }

    public static ViewUrlFragment newInstance(NdefRecord record)
    {
        ViewUrlFragment fragment = new ViewUrlFragment();
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
        return inflater.inflate(R.layout.fragment_view_url, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mUrlText = view.findViewById(R.id.view_url_textview_url);
        mOpenUrlButton = view.findViewById(R.id.view_url_button);

        byte[] payload = _record.getPayload();
        int prefixCode = payload[0] & 0x0FF;
        if(prefixCode >= NdefUtils.URI_PREFIX.length)
        {
            prefixCode = 0;
        }

        String reducedUri = new String(payload, 1, payload.length - 1, StandardCharsets.UTF_8);

        String uri = NdefUtils.URI_PREFIX[prefixCode] + reducedUri;

        mUrlText.setText(uri);
    }
}
