package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.fragments.DetailsToolbar;

public class ImplantDetails extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implant_details);

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
}