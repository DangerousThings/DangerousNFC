package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.pages.SettingsActivity;

public class SettingsFragment extends Fragment
{
    Button mChangeThemeButton;
    ImageButton mBackButton;
    ImageButton mConfirmButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mChangeThemeButton = view.findViewById(R.id.settings_button_change_theme);
        mBackButton = view.findViewById(R.id.settings_button_back);
        mConfirmButton = view.findViewById(R.id.settings_button_confirm);
        mChangeThemeButton.setOnClickListener(v -> changeThemeButtonClicked());
        mBackButton.setOnClickListener(v -> requireActivity().onBackPressed());
        mConfirmButton.setOnClickListener(v -> confirmButtonClicked());
    }

    private void changeThemeButtonClicked()
    {
        ((SettingsActivity) requireActivity()).switchToThemeFragment();
    }

    private void confirmButtonClicked()
    {

    }
}