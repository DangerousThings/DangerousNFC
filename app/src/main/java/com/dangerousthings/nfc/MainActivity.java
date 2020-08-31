package com.dangerousthings.nfc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dangerousthings.nfc.fragments.ImplantSelectionRecycler;
import com.dangerousthings.nfc.enums.TagType;
import com.dangerousthings.nfc.models.Implant;
import com.dangerousthings.nfc.utilities.FingerprintUtils;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    //For Intent filter
    IntentFilter[] _intentFilterArray;
    PendingIntent _pendingIntent;
    NfcAdapter _adapter;

    //Fingerprinting contexts
    Tag _tag;
    Ndef _ndef;

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

        _intentFilterArray = new IntentFilter[] {ndef, tech, tag};
    }

    private void handleNfcActionDiscovered(Intent intent)
    {
        _tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(_tag != null)
        {
            TagType tagType = FingerprintUtils.fingerprintNfcTag(_tag);

            if(tagType == TagType.Unknown)
            {
                Toast toast = Toast.makeText(this, R.string.implant_cannot_identify, Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            List<Implant> list = FingerprintUtils.getImplantListFromType(tagType);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.addToBackStack(null);
            ImplantSelectionRecycler recyclerDialog = new ImplantSelectionRecycler(list);
            fragmentTransaction.replace(R.id.frame_recycler_dialog, recyclerDialog);
            fragmentTransaction.commit();
        }
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