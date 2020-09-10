package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.fragments.DetailsToolbar;
import com.dangerousthings.nfc.fragments.DisplayImplantDetails;
import com.dangerousthings.nfc.models.Implant;

public class ImplantDetails extends AppCompatActivity
{
    Implant _implant;
    ImplantDetailsType _type;

    public static final String EXTRA_IMPLANT = "implant";
    public static final String EXTRA_IMPLANT_DETAIL_TYPE = "type";

    public enum ImplantDetailsType
    {
        displayDetails,
        editDetails
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implant_details);

        Bundle extras = getIntent().getExtras();
        _implant = (Implant)extras.getSerializable(EXTRA_IMPLANT);
        _type = (ImplantDetailsType)extras.getSerializable(EXTRA_IMPLANT_DETAIL_TYPE);

        if(_implant != null && _type != null)
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

        if(_type == ImplantDetailsType.displayDetails)
        {
            contentFragment = new DisplayImplantDetails(_implant);
        }
        else if(_type == ImplantDetailsType.editDetails)
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