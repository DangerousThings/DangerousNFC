package com.dangerousthings.nfc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.widget.TextView;

import com.dangerousthings.nfc.utilities.HexUtils;

import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    //For Intent filter
    IntentFilter[] _intentFilterArray;
    PendingIntent _pendingIntent;
    NfcAdapter _adapter;

    //Fingerprinting contexts
    Tag _tag;

    //Page objects
    TextView mTextViewScannedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewScannedDevice = findViewById(R.id.textview_scanned_device);

        nfcPrimer();
    }

    private void nfcPrimer()
    {
        _adapter = NfcAdapter.getDefaultAdapter(this);

        _pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try
        {
            ndef.addDataType("text/plain");
        }
        catch(IntentFilter.MalformedMimeTypeException e)
        {
            throw new RuntimeException("fail", e);
        }

        _intentFilterArray = new IntentFilter[] {ndef, tech, tag};
    }

    private void handleNfcActionDiscovered(Intent intent)
    {
        _tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        assert _tag != null;
        mTextViewScannedDevice.setText(HexUtils.bytesToHex(_tag.getId()));
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        if(Objects.equals(intent.getAction(), NfcAdapter.ACTION_NDEF_DISCOVERED)||Objects.equals(intent.getAction(), NfcAdapter.ACTION_TECH_DISCOVERED)||Objects.equals(intent.getAction(), NfcAdapter.ACTION_TAG_DISCOVERED))
        {
            handleNfcActionDiscovered(intent);
        }
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
        //TODO: add tech list
        _adapter.enableForegroundDispatch(this, _pendingIntent, _intentFilterArray, null);
    }
}