package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IMainMenuClickListener;

public class MainDrawerFragment extends Fragment
{
    private IMainMenuClickListener _clickListener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main_drawer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        LinearLayout mSettingsLinear = view.findViewById(R.id.main_linear_settings);
        mSettingsLinear.setOnClickListener(v -> _clickListener.onSettingsClicked());
        LinearLayout mSavedImplantsLinear = view.findViewById(R.id.main_linear_saved_implants);
        mSavedImplantsLinear.setOnClickListener(v -> _clickListener.onSavedImplantsClicked());
    }

    public void setOnClickListener(IMainMenuClickListener listener)
    {
        _clickListener = listener;
    }
}