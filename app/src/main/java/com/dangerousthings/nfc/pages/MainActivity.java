package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.controls.OnSwipeTouchListener;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.enums.MainActionBarState;
import com.dangerousthings.nfc.fragments.MainDrawerFragment;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.interfaces.IMainMenuClickListener;
import com.dangerousthings.nfc.models.Implant;
import com.dangerousthings.nfc.utilities.ColorUtils;
import com.dangerousthings.nfc.utilities.FingerprintUtils;
import com.dangerousthings.nfc.utilities.HexUtils;
import com.dangerousthings.nfc.utilities.NdefUtils;
import com.dangerousthings.nfc.utilities.TagUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends BaseActivity implements IMainMenuClickListener
{
    //UI variables
    private DrawerLayout mDrawer;
    private NavigationView mNavigation;
    private ConstraintLayout mConstraint;
    private Button mToggleReadButton;
    private Button mToggleAdvancedButton;

    MainActionBarState _actionBarState = MainActionBarState.ReadNDEF;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton mDrawerButton = findViewById(R.id.main_button_drawer_toggle);
        mToggleReadButton = findViewById(R.id.main_button_toggle_read);
        mToggleAdvancedButton = findViewById(R.id.main_button_toggle_advanced);
        mToggleReadButton.setOnClickListener(v -> toggleReadPressed());
        mToggleAdvancedButton.setOnClickListener(v -> toggleAdvancedPressed());

        //set up menu fragments click listener
        MainDrawerFragment mMenuFragment = (MainDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment_menu);
        assert mMenuFragment != null;
        mMenuFragment.setOnClickListener(this);

        mDrawerButton.setOnClickListener(v -> drawerButtonClicked());

        setDrawer();
        setUpScanAnimation();
    }

    @Override
    public void handleActionDiscovered(Intent intent)
    {
        String action = intent.getAction();
        if(Objects.equals(intent.getAction(), NfcAdapter.ACTION_NDEF_DISCOVERED) || Objects.equals(intent.getAction(), NfcAdapter.ACTION_TECH_DISCOVERED) || Objects.equals(intent.getAction(), NfcAdapter.ACTION_TAG_DISCOVERED))
        {
            NdefMessage message = NdefUtils.getNdefMessage(intent);
            ImplantDatabase database = ImplantDatabase.getInstance(this);
            IImplantDAO implantDAO = database.implantDAO();
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            assert tag != null;
            Implant updateImplant = implantDAO.getImplantByUID(HexUtils.bytesToHex(tag.getId()));
            if(updateImplant != null)
            {
                updateImplant.setUID(HexUtils.bytesToHex(tag.getId()));
                updateImplant.setTagFamily(FingerprintUtils.fingerprintNfcTag(tag));
                updateImplant.setNdefCapacity(TagUtils.getNdefCapacity(tag));
                if(message != null)
                {
                    updateImplant.setNdefMessage(message);
                }
                implantDAO.updateImplant(updateImplant);
            }
            if(_actionBarState.equals(MainActionBarState.ReadNDEF))
            {
                if (message != null)
                {
                    Intent readMessageIntent = new Intent(this, ManageRecordsActivity.class);
                    readMessageIntent.putExtra(getString(R.string.intent_ndef_message), message);
                    startActivity(readMessageIntent);
                    overridePendingTransition(0, 0);
                }
            }
            else if(_actionBarState.equals(MainActionBarState.Advanced))
            {
                if(implantDAO.getImplantByUID(HexUtils.bytesToHex(tag.getId())) == null)
                {
                    Implant implant = new Implant();
                    implant.setUID(HexUtils.bytesToHex(tag.getId()));
                    implant.setTagFamily(FingerprintUtils.fingerprintNfcTag(tag));
                    implant.setNdefCapacity(TagUtils.getNdefCapacity(tag));
                    if(message != null)
                    {
                        implant.setNdefMessage(message);
                    }
                    new AlertDialog.Builder(this)
                            .setTitle("New Implant Detected")
                            .setMessage("Would you like to save this implant?")
                            .setPositiveButton("Yes", ((dialog, which) ->
                            {
                                if(message != null)
                                {
                                    implant.setNdefMessage(message);
                                }

                                implantDAO.insertImplant(implant);

                                Intent onboardImplant = new Intent(this, ManageImplantActivity.class);
                                onboardImplant.putExtra(getString(R.string.intent_tag_uid), HexUtils.bytesToHex(tag.getId()));
                                onboardImplant.putExtra(getString(R.string.intent_onboard_flag), true);
                                startActivity(onboardImplant);
                                overridePendingTransition(0, 0);
                            }))
                            .setNegativeButton("No", ((dialog, which) -> dialog.cancel()))
                            .show();
                }
                else
                {
                    Intent displayImplant = new Intent(this, ManageImplantActivity.class);
                    displayImplant.putExtra(getString(R.string.intent_tag_uid), HexUtils.bytesToHex(tag.getId()));
                    displayImplant.putExtra(getString(R.string.intent_onboard_flag), false);
                    startActivity(displayImplant);
                    overridePendingTransition(0, 0);
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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
        mDrawer.setOnTouchListener(new OnSwipeTouchListener(this)
        {
            public void onSwipeRight()
            {
                mDrawer.open();
            }
        });
    }

    private void drawerButtonClicked()
    {
        mDrawer.open();
    }

    private void toggleReadPressed()
    {
        mToggleAdvancedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.right_pill_secondary));
        mToggleReadButton.setBackground(ContextCompat.getDrawable(this, R.drawable.left_pill_accent));
        mToggleReadButton.setTextColor(ColorUtils.getSecondaryColor(this));
        mToggleAdvancedButton.setTextColor(ColorUtils.getPrimaryColor(this));

        _actionBarState = MainActionBarState.ReadNDEF;
    }

    private void toggleAdvancedPressed()
    {
        mToggleReadButton.setBackground(ContextCompat.getDrawable(this, R.drawable.left_pill_secondary));
        mToggleAdvancedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.right_pill_accent));
        mToggleReadButton.setTextColor(ColorUtils.getPrimaryColor(this));
        mToggleAdvancedButton.setTextColor(ColorUtils.getSecondaryColor(this));

        _actionBarState = MainActionBarState.Advanced;
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

    @Override
    public void onSettingsClicked()
    {
        mDrawer.close();
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onSavedImplantsClicked()
    {
        mDrawer.close();
        Intent savedImplantsIntent = new Intent(this, SavedImplantsActivity.class);
        startActivity(savedImplantsIntent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onNewNdefMessageClicked()
    {
        mDrawer.close();
        Intent emptyNdefMessage = new Intent(this, ManageRecordsActivity.class);
        startActivity(emptyNdefMessage);
        overridePendingTransition(0, 0);
    }
}