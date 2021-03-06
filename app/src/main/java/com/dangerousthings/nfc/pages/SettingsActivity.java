package com.dangerousthings.nfc.pages;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.fragments.SettingsFragment;
import com.dangerousthings.nfc.fragments.ThemeFragment;

public class SettingsActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        setDefaultFragment();
    }

    private void setDefaultFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.replace(R.id.base_frame, settingsFragment);
        fragmentTransaction.commit();
    }

    public void switchToThemeFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ThemeFragment themeFragment = new ThemeFragment();
        fragmentTransaction.replace(R.id.base_frame, themeFragment);
        fragmentTransaction.addToBackStack("themeFragment");
        fragmentTransaction.commit();
    }
}