package com.dangerousthings.nfc.pages;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.fragments.DisplayPlainTextFragment;
import com.dangerousthings.nfc.fragments.ViewMessageFragment;
import com.dangerousthings.nfc.interfaces.IManageRecordsClickListener;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.models.Implant;

public class NdefManagementActivity extends BaseActivity implements IManageRecordsClickListener
{
    NdefMessage _message;
    Implant _implant;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //pull extras
        _message = getIntent().getParcelableExtra(getString(R.string.intent_ndef_message));
        String UID = getIntent().getExtras().getString(getString(R.string.intent_tag_uid));
        if(UID != null)
        {
            ImplantDatabase database = ImplantDatabase.getInstance(this);
            IImplantDAO implantDAO = database.implantDAO();
            _implant = implantDAO.getImplantByUID(UID);
        }
        startViewMessageFragment();
    }

    public void displayRecord(NdefRecord record)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment;
        //if plain text
        if(record.toMimeType().equals(getString(R.string.mime_plaintext)))
        {
            fragment = DisplayPlainTextFragment.newInstance(record);
        }
        //if the mimetype is not currently supported
        else
        {
            fragment = null;
        }

        fragmentTransaction.replace(R.id.base_frame, fragment);
        fragmentTransaction.addToBackStack("displayNdef");
        fragmentTransaction.commit();
    }

    private void startViewMessageFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ViewMessageFragment messages = ViewMessageFragment.newInstance(_message);
        messages.setClickListener(this);
        fragmentTransaction.replace(R.id.base_frame, messages);
        fragmentTransaction.commit();
    }

    @Override
    public void onNewRecordClick()
    {
        Intent addRecord = new Intent(this, EditNdefActivity.class);
        startActivity(addRecord);
        overridePendingTransition(0, 0);
    }
}