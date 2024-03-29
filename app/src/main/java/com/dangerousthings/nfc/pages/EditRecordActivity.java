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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.fragments.EditPlainTextFragment;
import com.dangerousthings.nfc.interfaces.IEditFragment;
import com.dangerousthings.nfc.interfaces.ITracksPayloadSize;
import com.dangerousthings.nfc.utilities.NdefUtils;
import com.google.android.material.navigation.NavigationView;

/**
 * --------------REQUIRED ARGUMENTS----------------
 * - NdefRecord passed in as parcelableExtra under the R.string.intent_record tag
 * - String representing the operating implants maximum Ndef Capacity  passed in as
 *   a stringExtra under the R.string.intent_ndef_capacity tag
 */

public class EditRecordActivity extends BaseActivity implements ITracksPayloadSize
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

        setViews();
        getCapacityText();
        setDrawer();
        startFragment();
    }

    private void setViews()
    {
        mPayloadTypeButton = findViewById(R.id.edit_ndef_button_payload_type);
        mPayloadTypeButton.setOnClickListener(v -> mDrawer.openDrawer(GravityCompat.END));
        mBackButton = findViewById(R.id.edit_ndef_button_back);
        mBackButton.setOnClickListener(v -> onBackPressed());
        mPayloadSizeText = findViewById(R.id.edit_ndef_text_payload_size);
        mPayloadSizeText.setText(getString(R.string.two_arguments, 0, _ndefCapacityText));
        mSaveButton = findViewById(R.id.edit_ndef_button_save);
        mSaveButton.setOnClickListener(v -> returnRecordResult());
    }

    private void getCapacityText()
    {
        int capacity = getIntent().getIntExtra(getString(R.string.intent_ndef_capacity), 0);
        if(capacity != 0)
        {
            _ndefCapacityText = "/" + capacity + " Bytes";
        }
        else
        {
            _ndefCapacityText = " Bytes";
        }
    }

    private void returnRecordResult()
    {
        NdefRecord resultRecord = _fragment.getNdefRecord();
        if(resultRecord != null)
        {
            Intent result = new Intent();
            result.putExtra(getString(R.string.intent_record), resultRecord);
            result.putExtra(getString(R.string.intent_request_code), getIntent().getIntExtra(getString(R.string.intent_request_code), -1));
            setResult(ManageRecordsActivity.RESULT_OK, result);
            finish();
            overridePendingTransition(0, 0);
        }
        else
        {
            Toast.makeText(this, "Record cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void startFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(_record == null)
        {
            _fragment = EditPlainTextFragment.newInstance();
        }
        else
        {
            _fragment = NdefUtils.getEditFragmentForRecord(_record);
        }
        if(_fragment != null)
        {
            _fragment.setPayloadTrackingInterface(this);
            transaction.replace(R.id.edit_ndef_frame, ((Fragment) _fragment));
            transaction.commit();
            mPayloadTypeButton.setText(_fragment.getDataTypeName());
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

        mNavigation.setNavigationItemSelectedListener(this::drawerItemSelected);
    }

    @Override
    public void payloadChanged()
    {
        mPayloadSizeText.setText(getString(R.string.two_arguments, _fragment.getPayloadSize(), _ndefCapacityText));
    }

    private boolean drawerItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        _fragment = NdefUtils.getEditFragmentForNav(_record, id);
        if(_fragment != null)
        {
            _fragment.setPayloadTrackingInterface(this);
            transaction.replace(R.id.edit_ndef_frame, ((Fragment)_fragment));
            transaction.commit();

            mPayloadTypeButton.setText(_fragment.getDataTypeName());
            mDrawer.closeDrawer(GravityCompat.END);
        }

        return true;
    }

    @Override
    public void onBackPressed()
    {
        NdefRecord record = _fragment.getNdefRecord();
        if(record != null)
        {
            if (!record.equals(_record))
            {
                new AlertDialog.Builder(this)
                        .setTitle("Discard Unsaved Changes?")
                        .setMessage("Are you sure you want to leave this page without saving this record?")
                        .setPositiveButton("Yes", ((dialog, which) ->
                        {
                            setResult(RESULT_CANCELED);
                            finish();
                            overridePendingTransition(0, 0);
                        }))
                        .setNegativeButton("No", ((dialog, which) -> dialog.cancel()))
                        .show();
            }
            else
            {
                finish();
                overridePendingTransition(0, 0);
            }
        }
        else
        {
            finish();
            overridePendingTransition(0, 0);
        }
    }
}