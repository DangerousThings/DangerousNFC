package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        TextView settingsText = view.findViewById(R.id.main_text_settings);
        settingsText.setOnClickListener(v -> _clickListener.onSettingsClicked());
        TextView savedImplantsText = view.findViewById(R.id.main_text_saved_implants);
        savedImplantsText.setOnClickListener(v -> _clickListener.onSavedImplantsClicked());
        TextView newNdefMessageText = view.findViewById(R.id.main_text_new_message);
        newNdefMessageText.setOnClickListener(v -> _clickListener.onNewNdefMessageClicked());
    }

    public void setOnClickListener(IMainMenuClickListener listener)
    {
        _clickListener = listener;
    }
}