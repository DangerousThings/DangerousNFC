package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.enums.MainActionBarState;
import com.dangerousthings.nfc.fragments.MainFragment;
import com.dangerousthings.nfc.interfaces.IMainActionBar;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements IMainActionBar
{
    //NFC globals
    IntentFilter[] _intentFilterArray;
    PendingIntent _pendingIntent;
    NfcAdapter _adapter;

    //UI variables
    private DrawerLayout mDrawer;
    private NavigationView mNavigation;
    private ConstraintLayout mConstraint;

    MainActionBarState actionBarState = MainActionBarState.ReadPayload;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarColor();
        setDrawer();
        setDefaultFragment();
        nfcPrimer();
    }

    private void nfcPrimer()
    {
        _adapter = NfcAdapter.getDefaultAdapter(this);

        _pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try
        {
            //add any additional NDEF related mimetypes here
            ndef.addDataType("text/plain");
            ndef.addDataType("image/jpeg");
            ndef.addDataType("text/markdown");
        }
        catch(IntentFilter.MalformedMimeTypeException e)
        {
            throw new RuntimeException("fail", e);
        }

        _intentFilterArray = new IntentFilter[] {ndef};

        handleActionDiscovered(this.getIntent());
    }

    private void handleActionDiscovered(Intent intent)
    {
        if(Objects.equals(intent.getAction(), NfcAdapter.ACTION_NDEF_DISCOVERED))
        {
            //push to read page
        }
    }

    private void setDefaultFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment mainFragment = new MainFragment();
        mainFragment.setActionBarInterface(this);
        fragmentTransaction.replace(R.id.main_frame_content, mainFragment);

        fragmentTransaction.commit();
    }

    private void setDrawer()
    {
        mDrawer = findViewById(R.id.main_drawer);
        mNavigation = findViewById(R.id.main_navigation_view);
        mConstraint = findViewById(R.id.main_constraint);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, null, R.string.drawer_toggle_open, R.string.drawer_toggle_close)
        {
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = (mNavigation.getWidth() * slideOffset);

                mConstraint.setTranslationX(moveFactor);
            }
        };

        mDrawer.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
        mDrawer.setElevation(0);
        mDrawer.setDrawerElevation(0);
        mNavigation.setElevation(0);
        mDrawer.addDrawerListener(mDrawerToggle);
    }

    private void setStatusBarColor()
    {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FFFFFF"));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    public void drawerButtonClicked()
    {
        mDrawer.open();
    }

    @Override
    public void mainActionToggled(MainActionBarState state)
    {
        actionBarState = state;
    }

    @Override
    public void settingsButtonClicked()
    {

    }

    @Override
    public void onPause()
    {
        super.onPause();
        _adapter.disableForegroundDispatch(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        _adapter.enableForegroundDispatch(this, _pendingIntent, _intentFilterArray, null);
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        handleActionDiscovered(intent);
    }
}