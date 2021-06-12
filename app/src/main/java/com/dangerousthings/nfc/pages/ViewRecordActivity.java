package com.dangerousthings.nfc.pages;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.controls.DecryptionPasswordDialog;
import com.dangerousthings.nfc.controls.EncryptionPasswordDialog;
import com.dangerousthings.nfc.enums.OnClickType;
import com.dangerousthings.nfc.fragments.ViewMarkdownFragment;
import com.dangerousthings.nfc.fragments.ViewPlainTextFragment;
import com.dangerousthings.nfc.interfaces.IClickListener;
import com.dangerousthings.nfc.utilities.EncryptionUtils;

public class ViewRecordActivity extends BaseActivity implements IClickListener
{
    ImageButton mBackButton;
    ImageButton mEditButton;

    DecryptionPasswordDialog _dialog;
    NdefRecord _record;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        setupViews();
        _record = getIntent().getParcelableExtra(getString(R.string.intent_record));
        if(_record != null)
        {
            try
            {
                String type = _record.toMimeType().substring(0, _record.toMimeType().indexOf("_"));
                if(type.equals("encrypted"))
                {
                    _dialog = new DecryptionPasswordDialog();
                    _dialog.setClickListener(this);
                    _dialog.show(getSupportFragmentManager(), "DecryptionDialog");
                }
                else
                {
                    loadFragment();
                }
            }
            catch(Exception e)
            {
                loadFragment();
            }
        }
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

        Fragment fragment;
        String mimeType = _record.toMimeType();
        if(mimeType.equals("text/plain"))
        {
            fragment = ViewPlainTextFragment.newInstance(_record);
        }
        else if(mimeType.equals("text/markdown"))
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

    @Override
    public void onClick(OnClickType clickType)
    {
        switch(clickType)
        {
            case cancel:
                finish();
                break;
            case decrypt_record:
                decryptRecord();
                break;
        }
    }

    private void decryptRecord()
    {
        String decryptionPassword = _dialog.getDecryptionPassword();
        try
        {
            byte[] decryptedBytes = EncryptionUtils.decryptAES128Data(decryptionPassword, _record.getPayload());
            String temp = EncryptionUtils.getDecryptedMimeType(_record.toMimeType());
            _record = NdefRecord.createMime(EncryptionUtils.getDecryptedMimeType(_record.toMimeType()), decryptedBytes);
            loadFragment();
        }
        catch(Exception e)
        {
            Log.d("Decryption Error:", e.toString());
        }
    }
}