package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.enums.OnClickType;
import com.dangerousthings.nfc.fragments.DisplayImplantInfoFragment;
import com.dangerousthings.nfc.fragments.EditImplantInfoFragment;
import com.dangerousthings.nfc.interfaces.IClickListener;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.models.Implant;
import com.google.android.material.navigation.NavigationView;

/**
 * --------------REQUIRED ARGUMENTS----------------
 * - String UID for the operating implant passed in under the R.string.intent_uid tag
 * - boolean denoting onboarding status for the operating implant
 *   passed in under the R.string.intent_onboard_flag tag
 */

public class ImplantManagementActivity extends BaseActivity implements IClickListener
{
    Implant _implant;
    String _uid;
    public boolean _onboardFlag = false;

    DrawerLayout mDrawer;
    NavigationView mNavigation;
    ConstraintLayout mConstraint;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implant_management);

        getImplantFromUid();
        _onboardFlag = getIntent().getBooleanExtra(getString(R.string.intent_onboard_flag), false);
        setDrawer();
        lockDrawer(_onboardFlag);
        loadFragment();
    }

    private void loadFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(_onboardFlag)
        {
            //start onboard fragment
            EditImplantInfoFragment onboardFragment = EditImplantInfoFragment.newInstance(_uid);
            fragmentTransaction.replace(R.id.base_frame, onboardFragment);
        }
        else
        {
            DisplayImplantInfoFragment displayFragment = DisplayImplantInfoFragment.newInstance(_uid, this);
            fragmentTransaction.replace(R.id.base_frame, displayFragment);
        }
        fragmentTransaction.commit();
    }

    private void getImplantFromUid()
    {
        _uid = getIntent().getStringExtra(getString(R.string.intent_tag_uid));
        ImplantDatabase database = ImplantDatabase.getInstance(this);
        IImplantDAO implantDAO = database.implantDAO();
        _implant = implantDAO.getImplantByUID(_uid);
    }

    public void switchToDisplayFragment(String UID)
    {
        _onboardFlag = false;
        lockDrawer(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DisplayImplantInfoFragment displayFragment = DisplayImplantInfoFragment.newInstance(UID, this);
        fragmentTransaction.replace(R.id.base_frame, displayFragment);
        fragmentTransaction.commit();
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

    private void setDrawer()
    {
        mDrawer = findViewById(R.id.implant_management_drawer);
        mNavigation = findViewById(R.id.implant_management_navigation);
        mConstraint = findViewById(R.id.implant_management_constraint);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, null, R.string.drawer_toggle_open, R.string.drawer_toggle_close)
        {
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = -(mNavigation.getWidth() * slideOffset);

                mConstraint.setTranslationX(moveFactor);
            }
        };

        mDrawer.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
        mDrawer.setElevation(0);
        mDrawer.setDrawerElevation(0);
        mNavigation.setElevation(0);
        _implant.getOperationsMenu(mNavigation.getMenu());
        mDrawer.addDrawerListener(mDrawerToggle);

        mNavigation.setNavigationItemSelectedListener(this::drawerItemSelected);
    }

    private boolean drawerItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.string.menu_view_records)
        {
            //push to records
            Intent readMessageIntent = new Intent(this, ViewRecordsActivity.class);
            readMessageIntent.putExtra(getString(R.string.intent_ndef_message), _implant.getNdefMessage());
            readMessageIntent.putExtra(getString(R.string.intent_tag_uid), _implant.getUID());
            startActivity(readMessageIntent);
            overridePendingTransition(0, 0);
            mDrawer.closeDrawer(GravityCompat.END);
        }
        else if(id == R.string.menu_memory_management)
        {
            //push to memory management
            Toast.makeText(this, "Feature not implemented yet", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void lockDrawer(boolean locked)
    {
        int lockMode = locked ?  DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED;
        mDrawer.setDrawerLockMode(lockMode);
    }

    @Override
    public void onClick(OnClickType clickType)
    {
        mDrawer.openDrawer(GravityCompat.END);
    }

    @Override
    public void onResume()
    {
        getImplantFromUid();
        super.onResume();
    }
}