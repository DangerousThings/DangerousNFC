package com.dangerousthings.nfc.pages;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.utilities.ColorUtils;
import com.dangerousthings.nfc.utilities.HexUtils;
import com.dangerousthings.nfc.utilities.NdefUtils;

public class ImplantInterfaceActivity extends BaseActivity
{
    Intent _originalIntent;
    int _requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implant_interface);

        _originalIntent = getIntent();
        _requestCode = getIntent().getIntExtra(getString(R.string.intent_request_code), -1);

        setUpScanAnimation();
    }

    @Override
    public void handleActionDiscovered(Intent intent)
    {
        if(_requestCode == ManageRecordsActivity.REQ_CODE_WRITE_MESSAGE)
        {
            NdefMessage message = _originalIntent.getParcelableExtra(getString(R.string.intent_ndef_message));
            writeToTag(intent, message);
        }
    }

    private void writeToTag(Intent intent, NdefMessage message)
    {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(tag != null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (NdefUtils.writeNdefMessage(tag, message))
            {
                builder.setMessage("NDEF write successful");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", (dialog, which) ->
                {
                    dialog.cancel();
                    popOnWriteSuccess(tag);
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else
            {
                builder.setMessage("NDEF write unsuccessful");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private void popOnWriteSuccess(Tag tag)
    {
        Intent result = new Intent();
        setResult(ManageRecordsActivity.RESULT_OK, result);
        result.putExtra(getString(R.string.intent_request_code), getIntent().getIntExtra(getString(R.string.intent_request_code), -1));
        result.putExtra(getString(R.string.intent_tag_uid), HexUtils.bytesToHex(tag.getId()));
        finish();
        overridePendingTransition(0, 0);
    }

    private void setUpScanAnimation()
    {
        ImageView mAnimationView = findViewById(R.id.interface_image_scan_animation);
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
}