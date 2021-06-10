package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.enums.OnClickType;
import com.dangerousthings.nfc.interfaces.IClickListener;

public class ViewRecordsToolbar extends Fragment
{
    IClickListener _clickListener;

    ImageButton mAddRecordButton;
    ImageButton mBackButton;
    ImageButton mWriteButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.toolbar_view_records, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        mAddRecordButton = view.findViewById(R.id.view_records_button_add);
        mBackButton = view.findViewById(R.id.view_records_button_back);
        mWriteButton = view.findViewById(R.id.view_records_button_write);
        mAddRecordButton.setOnClickListener(v -> _clickListener.onClick(OnClickType.new_record));
        mBackButton.setOnClickListener(v -> _clickListener.onClick(OnClickType.back));
        mWriteButton.setOnClickListener(v -> _clickListener.onClick(OnClickType.write));
    }

    public void setWriteButtonVisible()
    {
        mWriteButton.setVisibility(View.VISIBLE);
    }

    public void setClickListener(IClickListener listener)
    {
        _clickListener = listener;
    }
}