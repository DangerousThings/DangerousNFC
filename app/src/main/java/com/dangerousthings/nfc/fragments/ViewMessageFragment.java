package com.dangerousthings.nfc.fragments;

import android.nfc.NdefMessage;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.NdefMessageRecyclerAdapter;
import com.dangerousthings.nfc.interfaces.IItemClickListener;
import com.dangerousthings.nfc.pages.NdefManagementActivity;

import java.util.Arrays;

public class ViewMessageFragment extends Fragment implements IItemClickListener
{
    private static final String ARG_MESSAGE = "message";

    private NdefMessage _message;

    //UI elements
    RecyclerView mRecyclerView;
    NdefMessageRecyclerAdapter _recyclerAdapter;
    ImageButton mBackButton;
    ImageButton mAddRecordButton;

    public ViewMessageFragment()
    {
        // Required empty public constructor
    }

    public static ViewMessageFragment newInstance(NdefMessage message)
    {
        ViewMessageFragment fragment = new ViewMessageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            _message = getArguments().getParcelable(ARG_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_view_message, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        mRecyclerView = view.findViewById(R.id.message_recycler_ndef_records);
        mBackButton = view.findViewById(R.id.message_button_back);
        mAddRecordButton = view.findViewById(R.id.message_button_add);

        mAddRecordButton.setOnClickListener(v -> addButtonClicked());

        //set up recyclerview
        _recyclerAdapter = new NdefMessageRecyclerAdapter(requireActivity(), Arrays.asList(_message.getRecords()));
        _recyclerAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mRecyclerView.setAdapter(_recyclerAdapter);

        mBackButton.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    @Override
    public void onItemClick(int position)
    {
        ((NdefManagementActivity)requireActivity()).displayRecord(_recyclerAdapter.getRecord(position));
    }

    private void addButtonClicked()
    {
        //TODO
    }
}