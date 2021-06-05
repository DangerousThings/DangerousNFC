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

public class ViewRecordActivity extends BaseActivity
{
    NdefRecord _record;
    ImageButton mBackButton;
    ImageButton mEditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        _record = getIntent().getParcelableExtra(getString(R.string.intent_record));
        loadFragment();

        mBackButton = findViewById(R.id.view_record_button_back);
        mBackButton.setOnClickListener(v -> onBackPressed());
        mEditButton = findViewById(R.id.view_record_button_edit);
        mEditButton.setOnClickListener(v -> editButtonClicked());
    }

    private void loadFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment;
        //if plain text
        if(_record.toMimeType().equals(getString(R.string.mime_plaintext)))
        {
            fragment = ViewPlainTextFragment.newInstance(_record);
        }
        //if the mimetype is not currently supported
        else if(_record.toMimeType().equals("text/markdown"))
        {
            fragment = ViewMarkdownFragment.newInstance(_record);
        }
        else
        {
            fragment = null;
            Toast.makeText(this, "Mime type not currently supported", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(0, 0);
        }

        if(fragment != null)
        {
            fragmentTransaction.replace(R.id.view_record_frame, fragment);
            fragmentTransaction.commit();
        }
    }

    private void editButtonClicked()
    {
        Intent result = new Intent();
        result.putExtra(getString(R.string.intent_request_code), getIntent().getIntExtra(getString(R.string.intent_request_code), -1));
        setResult(ViewRecordsActivity.RESULT_OK, result);
        finish();
        overridePendingTransition(0, 0);
    }
}