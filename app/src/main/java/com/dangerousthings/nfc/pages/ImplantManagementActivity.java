package com.dangerousthings.nfc.pages;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.fragments.EditImplantInfoFragment;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.models.Implant;

public class ImplantManagementActivity extends BaseActivity
{
    Implant _implant;
    public boolean _onboardFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        String UID = getIntent().getStringExtra(getString(R.string.intent_tag_uid));
        ImplantDatabase database = ImplantDatabase.getInstance(this);
        IImplantDAO implantDAO = database.implantDAO();
        _implant = implantDAO.getImplantByUID(UID);

        _onboardFlag = getIntent().getBooleanExtra(getString(R.string.intent_oboard_flag), false);

        if(_onboardFlag)
        {
            //start onboard fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            EditImplantInfoFragment onboardFragment = EditImplantInfoFragment.newInstance(UID);
            fragmentTransaction.replace(R.id.base_frame, onboardFragment);
            fragmentTransaction.commit();
        }
        else
        {
            //start operation/information fragment
        }
    }

    @Override
    public void onBackPressed()
    {
        if(_onboardFlag)
        {
            ImplantDatabase database = ImplantDatabase.getInstance(this);
            IImplantDAO implantDAO = database.implantDAO();
            implantDAO.deleteImplant(_implant);
        }

        super.onBackPressed();
    }
}