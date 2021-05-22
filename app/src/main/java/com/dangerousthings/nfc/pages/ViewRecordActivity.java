package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dangerousthings.nfc.R;
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
        mEditButton.setOnClickListener(v -> broadcastEditRecordIntent());
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

    private void broadcastEditRecordIntent()
    {
        Intent broadcastIntent = new Intent(getString(R.string.intent_start_edit_ndef));
        sendBroadcast(broadcastIntent);
        finish();
        overridePendingTransition(0, 0);
    }
}