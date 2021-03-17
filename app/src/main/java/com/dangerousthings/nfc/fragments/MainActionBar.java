package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IOpenDrawerButton;

public class MainActionBar extends Fragment
{
    IOpenDrawerButton drawerInterface;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main_action_bar, container, false);
    }

    public void setDrawerInterface(IOpenDrawerButton iOpenDrawerButton)
    {
        this.drawerInterface = iOpenDrawerButton;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        ImageButton mDrawerButton = view.findViewById(R.id.main_button_drawer_toggle);
        mDrawerButton.setOnClickListener(v -> drawerInterface.drawerButtonClicked());
    }
}