package com.dangerousthings.nfc.pages;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.utilities.ColorUtils;

/**
 * A base activity that handles changing the StatusBar color, activating the current theme, and back press animations
 */

public abstract class BaseActivity extends AppCompatActivity
{
    IntentFilter[] _intentFilterArray;
    PendingIntent _pendingIntent;
    NfcAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
        int defaultTheme = R.style.DT;
        int savedTheme = preferences.getInt(getString(R.string.saved_theme), defaultTheme);
        setTheme(savedTheme);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ColorUtils.getPrimaryVariantColor(this));
        if(savedTheme != R.style.Zytel)
        {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        super.onCreate(savedInstanceState);

        nfcPrimer();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    private void nfcPrimer()
    {
        _adapter = NfcAdapter.getDefaultAdapter(this);

        _pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try
        {
            //add any additional NDEF related mimetypes here
            ndef.addDataType("*/*");
        }
        catch(IntentFilter.MalformedMimeTypeException e)
        {
            throw new RuntimeException("fail", e);
        }

        _intentFilterArray = new IntentFilter[] {ndef, tag, tech};

        handleActionDiscovered(this.getIntent());
    }

    void handleActionDiscovered(Intent intent)
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
