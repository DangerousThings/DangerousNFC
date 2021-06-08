package com.dangerousthings.nfc.pages;

import android.content.Context;
import android.content.SharedPreferences;
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

public class BaseActivity extends AppCompatActivity
{
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
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
