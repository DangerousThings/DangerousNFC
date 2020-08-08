package com.dangerousthings.nfc;

import com.dangerousthings.nearfield.tech.Ntag21x;
import com.dangerousthings.nearfield.tech.Ntag213;
import com.dangerousthings.nearfield.tech.Ntag215;
import com.dangerousthings.nearfield.tech.Ntag216;
import com.dangerousthings.nearfield.utils.OtpUtils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.View;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends Activity implements PasswordFragment.OnPasswordListener {
    static byte NEW_NTAG213_AUTH0 = (byte)0x28;
    static byte NEW_NTAG215_AUTH0 = (byte)0x82;
    static byte NEW_NTAG216_AUTH0 = (byte)0xE2;

    static byte[] NEW_NTAG213_CC = new byte[] { (byte)0xE1, (byte)0x12, (byte)0x12, (byte)0x00 };
    static byte[] NEW_NTAG215_CC = new byte[] { (byte)0xE1, (byte)0x12, (byte)0x3E, (byte)0x00 };
    static byte[] NEW_NTAG216_CC = new byte[] { (byte)0xE1, (byte)0x12, (byte)0x6D, (byte)0x00 };

    static byte[] NEW_NTAG213_DYNAMIC_LOCK_BYTES = new byte[] { (byte)0x00, (byte)0x00, (byte)0x3F };
    static byte[] NEW_NTAG215_DYNAMIC_LOCK_BYTES = new byte[] { (byte)0x00, (byte)0x00, (byte)0x0F };
    static byte[] NEW_NTAG216_DYNAMIC_LOCK_BYTES = new byte[] { (byte)0x00, (byte)0x00, (byte)0x7F };

    static byte[] NEW_PACK = new byte[] { (byte)0x44, (byte)0x54 };
    static byte[] NEW_STATIC_LOCK_BYTES = new byte[] { (byte)0x0F, (byte)0x00 };

    byte[] mPassword = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onResume() {
        super.onResume();

        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter != null) adapter.enableForegroundDispatch(this, intent, null, null);
    }

    public void onNewIntent(Intent intent) {
        if (mPassword != null) {
            byte[] password = new byte[4];
            System.arraycopy(mPassword, 0, password, 0, mPassword.length);

            Tag intentTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            try {
                Ntag21x tag = Ntag21x.get(intentTag);
                tag.connect();

                byte[] newCC;
                byte newAuth0;
                byte[] newDynamicLockBytes;

                byte[] currentVersion = tag.getVersion();
                if (Arrays.equals(currentVersion, Ntag213.VERSION)) {
                    tag = Ntag213.get(intentTag);
                    newCC = NEW_NTAG213_CC;
                    newAuth0 = NEW_NTAG213_AUTH0;
                    newDynamicLockBytes = NEW_NTAG213_DYNAMIC_LOCK_BYTES;
                } else if (Arrays.equals(currentVersion, Ntag215.VERSION)) {
                    tag = Ntag215.get(intentTag);
                    newCC = NEW_NTAG215_CC;
                    newAuth0 = NEW_NTAG215_AUTH0;
                    newDynamicLockBytes = NEW_NTAG215_DYNAMIC_LOCK_BYTES;
                } else if (Arrays.equals(currentVersion, Ntag216.VERSION)) {
                    tag = Ntag216.get(intentTag);
                    newCC = NEW_NTAG216_CC;
                    newAuth0 = NEW_NTAG216_AUTH0;
                    newDynamicLockBytes = NEW_NTAG216_DYNAMIC_LOCK_BYTES;
                } else {
                    throw new IOException("Error: Unsupported Tag");
                }

                // tag.pwdAuth(password);

                boolean alteredStaticLockBytes = !Arrays.equals(tag.DEFAULT_STATIC_LOCK_BYTES, tag.getStaticLockBytes());
                boolean alteredDynamicLockBytes = !Arrays.equals(tag.DEFAULT_DYNAMIC_LOCK_BYTES, tag.getDynamicLockBytes());
                if (alteredStaticLockBytes || alteredDynamicLockBytes) throw new IOException("Error: Lock Bits Already Altered");

                byte[] currentCC = tag.getCC();
                if (OtpUtils.isWritePossible(currentCC, newCC)) {
                    tag.setCC(newCC);
                } else if ((currentCC[0] != newCC[0]) || (currentCC[1] != (byte)0x11) ||
                           (currentCC[2] != newCC[2]) || (currentCC[3] != newCC[3])) {
                    throw new IOException("Error: Bad Capability Container\n" + HexUtils.bytesToHex(currentCC));
                }

                tag.setStaticLockBytes(NEW_STATIC_LOCK_BYTES);
                tag.setDynamicLockBytes(newDynamicLockBytes);

                tag.setPwd(password);
                tag.setPack(NEW_PACK);
                tag.setAuth0(newAuth0);

                showAlert("Success! Tag has been updated! :)");
            } catch (IOException e) {
                showAlert(e.getMessage());
            }
        }
    }

    public void requestPassword(View view) {
        PasswordFragment.newInstance(mPassword).show(getFragmentManager(), null);
    }

    public void onPasswordInput(byte[] password) {
        setPasswordBytes(password);
    }

    public byte[] getPasswordBytes() {
        return mPassword;
    }

    public void setPasswordBytes(byte[] passwordBytes) {
        mPassword = passwordBytes;

        runOnUiThread(new Runnable() {
            public void run() {
                if (mPassword != null) {
                    findViewById(R.id.scan_tag_now).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.scan_tag_now).setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void showAlert(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(MainActivity.this)
                    .setMessage(message)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create().show();
            }
        });
    }
}
