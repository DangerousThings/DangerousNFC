package com.dangerousthings.nfc.pages;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.NdefMessageRecyclerAdapter;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.fragments.DisplayPlainTextFragment;
import com.dangerousthings.nfc.fragments.ViewMessageFragment;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.interfaces.IItemClickListener;
import com.dangerousthings.nfc.models.Implant;
import com.dangerousthings.nfc.utilities.NdefUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NdefMessageActivity extends BaseActivity
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
        if(record.toMimeType().equals(getString(R.string.mime_plaintext)))
        {
            fragment = DisplayPlainTextFragment.newInstance(record.getPayload());
        }
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
        fragmentTransaction.replace(R.id.base_frame, messages);
        fragmentTransaction.commit();
    }
}