package com.dangerousthings.nfc.pages;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.fragments.ViewMarkdownFragment;
import com.dangerousthings.nfc.fragments.ViewPlainTextFragment;
import com.dangerousthings.nfc.utilities.NdefUtils;

public class ViewRecordActivity extends BaseActivity
{
    ImageButton mBackButton;
    ImageButton mEditButton;

    NdefRecord _record;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        setupViews();
        _record = getIntent().getParcelableExtra(getString(R.string.intent_record));
        loadFragment();
    }

    private void setupViews()
    {
        mBackButton = findViewById(R.id.view_record_button_back);
        mBackButton.setOnClickListener(v -> onBackPressed());
        mEditButton = findViewById(R.id.view_record_button_edit);
        mEditButton.setOnClickListener(v -> editButtonClicked());
    }

    private void loadFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = NdefUtils.getViewFragmentForRecord(_record);
        if(fragment == null)
        {
            Toast.makeText(this, "Mime type not currently supported", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(0, 0);
        }
        else
        {
            fragmentTransaction.replace(R.id.view_record_frame, fragment);
            fragmentTransaction.commit();
        }
    }

    private void editButtonClicked()
    {
        Intent result = new Intent();
        result.putExtra(getString(R.string.intent_request_code), getIntent().getIntExtra(getString(R.string.intent_request_code), -1));
        result.putExtra(getString(R.string.intent_record), _record);
        setResult(ManageRecordsActivity.RESULT_OK, result);
        finish();
        overridePendingTransition(0, 0);
    }
}