package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.fragments.EditPlainTextFragment;
import com.google.android.material.navigation.NavigationView;

public class EditNdefActivity extends BaseActivity
{
    NdefRecord _record;

    DrawerLayout mDrawer;
    NavigationView mNavigation;
    ConstraintLayout mConstraint;
    Button mPayloadTypeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ndef);

        _record = getIntent().getParcelableExtra(getString(R.string.intent_record));
        setDrawer();
        startFragment();

        mPayloadTypeButton = findViewById(R.id.edit_ndef_button_payload_type);
        mPayloadTypeButton.setOnClickListener(v -> mDrawer.openDrawer(GravityCompat.END));
    }

    private void startFragment()
    {
        if(_record == null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            EditPlainTextFragment plainTextFragment = EditPlainTextFragment.newInstance();
            transaction.replace(R.id.edit_ndef_frame, plainTextFragment);
            transaction.commit();
        }
    }

    private void setDrawer()
    {
        mDrawer = findViewById(R.id.edit_ndef_drawer);
        mNavigation = findViewById(R.id.edit_ndef_navigation);
        mConstraint = findViewById(R.id.edit_ndef_constraint);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, null, R.string.drawer_toggle_open, R.string.drawer_toggle_close)
        {
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = -(mNavigation.getWidth() * slideOffset);

                mConstraint.setTranslationX(moveFactor);
            }
        };

        mDrawer.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
        mDrawer.setElevation(0);
        mDrawer.setDrawerElevation(0);
        mNavigation.setElevation(0);
        mDrawer.addDrawerListener(mDrawerToggle);

        //mNavigation.setNavigationItemSelectedListener(this::drawerItemSelected);
    }
}