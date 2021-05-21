package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.fragments.EditPlainTextFragment;
import com.dangerousthings.nfc.interfaces.IEditFragment;
import com.dangerousthings.nfc.interfaces.ITracksPayloadSize;
import com.google.android.material.navigation.NavigationView;

public class EditNdefActivity extends BaseActivity implements ITracksPayloadSize
{
    NdefRecord _record;
    String _ndefCapacityText;

    DrawerLayout mDrawer;
    NavigationView mNavigation;
    ConstraintLayout mConstraint;
    Button mPayloadTypeButton;
    ImageButton mBackButton;
    ImageButton mSaveButton;
    TextView mPayloadSizeText;

    IEditFragment _fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ndef);

        _record = getIntent().getParcelableExtra(getString(R.string.intent_record));
        int capacity = getIntent().getIntExtra(getString(R.string.intent_ndef_capacity), 0);
        if(capacity != 0)
        {
            _ndefCapacityText = "/" + capacity + " Bytes";
        }
        else
        {
            _ndefCapacityText = " Bytes";
        }

        setDrawer();
        startFragment();

        mPayloadTypeButton = findViewById(R.id.edit_ndef_button_payload_type);
        mPayloadTypeButton.setOnClickListener(v -> mDrawer.openDrawer(GravityCompat.END));
        mBackButton = findViewById(R.id.edit_ndef_button_back);
        mBackButton.setOnClickListener(v -> onBackPressed());
        mPayloadSizeText = findViewById(R.id.edit_ndef_text_payload_size);
        mPayloadSizeText.setText(getString(R.string.two_arguments, 0, _ndefCapacityText));
        mSaveButton = findViewById(R.id.edit_ndef_button_save);
        mSaveButton.setOnClickListener(v -> returnRecordResult());
    }

    private void returnRecordResult()
    {
        Intent result = new Intent();
        NdefRecord resultRecord = _fragment.getNdefRecord();
        result.putExtra(getString(R.string.intent_record), resultRecord);
        setResult(ViewRecordsActivity.RESULT_OK, result);
        finish();
        overridePendingTransition(0, 0);
    }

    private void startFragment()
    {
        if(_record == null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            _fragment = EditPlainTextFragment.newInstance();
            _fragment.setPayloadTrackingInterface(this);
            transaction.replace(R.id.edit_ndef_frame, ((Fragment)_fragment));
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

    @Override
    public void payloadChanged()
    {
        mPayloadSizeText.setText(getString(R.string.two_arguments, _fragment.getPayloadSize(), _ndefCapacityText));
    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setTitle("Discard Unchanged Changes?")
                .setMessage("Are you sure you want to leave this page without saving this record?")
                .setPositiveButton("Yes", ((dialog, which) ->
                {
                    //TODO: handle the record returned from the fragment
                    setResult(RESULT_CANCELED);
                    finish();
                    overridePendingTransition(0,0);
                }))
                .setNegativeButton("No", ((dialog, which) -> dialog.cancel()))
                .show();
    }
}