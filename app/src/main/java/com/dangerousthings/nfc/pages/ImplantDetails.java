package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.fragments.DetailsToolbar;
import com.dangerousthings.nfc.fragments.DisplayImplantDetails;
import com.dangerousthings.nfc.models.Implant;

public class ImplantDetails extends AppCompatActivity
{
    Implant _implant;
    boolean _inEditMode;

    public static final String EXTRA_IMPLANT_UID = "implant_uid";
    public static final String EXTRA_IN_EDIT = "display_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implant_details);

        String implantUid = getIntent().getStringExtra(EXTRA_IMPLANT_UID);
        _inEditMode = getIntent().getBooleanExtra(EXTRA_IN_EDIT, false);
        ImplantDatabase implantDatabase = ImplantDatabase.getInstance(getApplicationContext());
        _implant = implantDatabase.implantDao().findImplantByUid(implantUid).get(0);

        if(_implant != null)
        {
            setContentFragment();
        }

        setTitleBar();
    }

    private void setTitleBar()
    {
        DetailsToolbar detailsToolbar = new DetailsToolbar();
        //detailsToolbar.setInterface(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_detail_toolbar, detailsToolbar);
        fragmentTransaction.commit();
    }

    private void setContentFragment()
    {
        Fragment contentFragment = null;

        if(!_inEditMode)
        {
            contentFragment = new DisplayImplantDetails(_implant);
        }
        else if(_inEditMode)
        {
            //edit details fragment
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        assert contentFragment != null;
        fragmentTransaction.replace(R.id.frame_detail_content, contentFragment);
        fragmentTransaction.commit();
    }
}