package com.dangerousthings.nfc.fragments;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        String uri = NdefUtils.getUrlStringFromRecord(_record);
        mUrlText.setText(uri);

        mOpenUrlButton.setOnClickListener(v -> openUrl(uri));
    }

    private void openUrl(String uri)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        try
        {
            startActivity(browserIntent);
        }
        catch(Exception e)
        {
            Toast.makeText(requireActivity(), "URL cannot be opened", Toast.LENGTH_SHORT).show();
        }
    }
}
