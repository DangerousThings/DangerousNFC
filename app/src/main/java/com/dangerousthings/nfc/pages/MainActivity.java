package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.enums.MainActionBarState;
import com.dangerousthings.nfc.utilities.ColorUtils;
import com.dangerousthings.nfc.utilities.NdefUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends BaseActivity
{
    //NFC globals
    IntentFilter[] _intentFilterArray;
    PendingIntent _pendingIntent;
    NfcAdapter _adapter;

    //UI variables
    private DrawerLayout mDrawer;
    private NavigationView mNavigation;
    private ConstraintLayout mConstraint;
    private Button mToggleReadButton;
    private Button mToggleSyncButton;

    MainActionBarState _actionBarState = MainActionBarState.ReadPayload;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton mDrawerButton = findViewById(R.id.main_button_drawer_toggle);
        ImageButton mSettingsButton = findViewById(R.id.main_button_settings);
        mToggleReadButton = findViewById(R.id.main_button_toggle_read);
        mToggleSyncButton = findViewById(R.id.main_button_toggle_sync);
        mToggleReadButton.setOnClickListener(v -> toggleReadPressed());
        mToggleSyncButton.setOnClickListener(v -> toggleSyncPressed());

        mDrawerButton.setOnClickListener(v -> drawerButtonClicked());
        mSettingsButton.setOnClickListener(v -> settingsButtonClicked());

        setDrawer();
        nfcPrimer();
        setUpScanAnimation();
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
            NdefMessage message = NdefUtils.getNdefMessage(intent);
            if(message != null)
            {
                Intent readMessageIntent = new Intent(this, NdefMessageActivity.class);
                readMessageIntent.putExtra(getString(R.string.intent_ndef_message), message);
                startActivity(readMessageIntent);
                overridePendingTransition(0, 0);
            }
        }
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

    private void drawerButtonClicked()
    {
        mDrawer.open();
    }

    private void toggleReadPressed()
    {
        mToggleSyncButton.setBackground(ContextCompat.getDrawable(this, R.drawable.right_pill_primary_dark));
        mToggleReadButton.setBackground(ContextCompat.getDrawable(this, R.drawable.left_pill_accent));
        mToggleReadButton.setTextColor(ColorUtils.getPrimaryDarkColor(this));
        mToggleSyncButton.setTextColor(ColorUtils.getPrimaryColor(this));

        _actionBarState = MainActionBarState.ReadPayload;
    }

    private void toggleSyncPressed()
    {
        mToggleReadButton.setBackground(ContextCompat.getDrawable(this, R.drawable.left_pill_primary_dark));
        mToggleSyncButton.setBackground(ContextCompat.getDrawable(this, R.drawable.right_pill_accent));
        mToggleReadButton.setTextColor(ColorUtils.getPrimaryColor(this));
        mToggleSyncButton.setTextColor(ColorUtils.getPrimaryDarkColor(this));

        _actionBarState = MainActionBarState.SyncImplant;
    }

    private void setUpScanAnimation()
    {
        ImageView mAnimationView = findViewById(R.id.main_image_scan_animation);
        final AnimatedVectorDrawableCompat animation = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_scan);
        assert animation != null;
        animation.setTint(ColorUtils.getPrimaryColor(this));
        mAnimationView.setImageDrawable(animation);
        final Handler handler = new Handler(Looper.getMainLooper());
        animation.registerAnimationCallback(new Animatable2Compat.AnimationCallback()
        {
            @Override
            public void onAnimationEnd(Drawable drawable)
            {
                handler.post(animation::start);
                super.onAnimationEnd(drawable);
            }
        });
        animation.start();
    }

    private void settingsButtonClicked()
    {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
        overridePendingTransition(0, 0);
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