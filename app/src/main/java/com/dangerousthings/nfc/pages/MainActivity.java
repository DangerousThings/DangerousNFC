package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.enums.MainActionBarState;
import com.dangerousthings.nfc.fragments.MainFragment;
import com.dangerousthings.nfc.interfaces.IMainActionBar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements IMainActionBar
{
    private DrawerLayout mDrawer;
    private NavigationView mNavigation;
    private ConstraintLayout mConstraint;
    private ActionBarDrawerToggle mDrawerToggle;

    MainActionBarState actionBarState = MainActionBarState.ReadPayload;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarColor();
        setDrawer();
        setDefaultFragments();
    }

    private void setDefaultFragments()
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

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, null, R.string.drawer_toggle_open, R.string.drawer_toggle_close)
        {
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = (mNavigation.getWidth() * slideOffset);

                mConstraint.setTranslationX(moveFactor);
            }
        };

        mDrawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        mDrawer.setElevation(0);
        mDrawer.setDrawerElevation(0);
        mNavigation.setElevation(0);
        mDrawer.setDrawerListener(mDrawerToggle);
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
}