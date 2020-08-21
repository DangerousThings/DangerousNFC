package com.dangerousthings.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import java.lang.Class;
import java.lang.IllegalArgumentException;
import java.lang.reflect.Method;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class PayloadActivity extends Activity {

    static final String STATE_CONNECTED = "tagConnected";

    boolean mConnected;

    PendingIntent mPendingIntent;

    /*
     * NfcAdapter.getDefaultAdapter(Context context) ends up actually
     * returning NfcAdapter.getNfcAdapter(context.getApplicationContext())
     * and NfcAdapter.getNfcAdapter(Context context) is memoized
     * so we really don't need an NfcAdapter field here
     */

    Class<? extends TagTechnology> mTagTechnology;
    List<byte[]> mCommands = new ArrayList<byte[]>();

    ShareActionProvider mShareActionProvider;
    Intent mShareIntent = new Intent(Intent.ACTION_SEND);

    TextView mLog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.payload);

            mLog = (TextView) findViewById(R.id.log);

            mShareIntent.setType("text/plain");
            mShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Dangerous NFC Payload Result");

            if (savedInstanceState != null) {
                mConnected = savedInstanceState.getBoolean(STATE_CONNECTED);
            }

            List<String> pathSegments = getIntent().getData().getPathSegments();

            String tech = pathSegments.get(1);
            if (tech.equalsIgnoreCase("NfcA")) {
                mTagTechnology = android.nfc.tech.NfcA.class;
            } else if (tech.equalsIgnoreCase("NfcB")) {
                mTagTechnology = android.nfc.tech.NfcB.class;
            } else if (tech.equalsIgnoreCase("IsoDep")) {
                mTagTechnology = android.nfc.tech.IsoDep.class;
            } else if (tech.equalsIgnoreCase("NfcF")) {
                mTagTechnology = android.nfc.tech.NfcF.class;
            } else if (tech.equalsIgnoreCase("NfcV")) {
                mTagTechnology = android.nfc.tech.NfcV.class;
            } else {
                throw new IllegalArgumentException(tech);
            }

            for (String command : pathSegments.subList(2, pathSegments.size())) {
                mCommands.add(Hex.decodeHex(command.replaceAll("[^\\p{XDigit}]", "").toCharArray()));
            }

            mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        } catch (IllegalArgumentException|DecoderException e) {
            log(ExceptionUtils.getRootCauseStackTrace(e)[0]);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPendingIntent != null) {
            NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        mShareIntent.putExtra(Intent.EXTRA_TEXT, mLog.getText());
        mShareActionProvider.setShareIntent(mShareIntent);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_CONNECTED, mConnected);
        super.onSaveInstanceState(savedInstanceState);
    }

    /*
     * NfcAdapter.class uses an OnActivityPausedListener to handle disabling
     * foreground dispatch for us so we can forgo an onPause() override
     */

    @Override
    public void onNewIntent(Intent intent) {
        if (mConnected) return; 

        try {
            Method factoryMethod = mTagTechnology.getMethod("get", Tag.class);
            Method transceiveMethod = mTagTechnology.getMethod("transceive", byte[].class);

            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            TagTechnology tag = (TagTechnology) factoryMethod.invoke(null, tagFromIntent);

            if (tag == null) {
                log(String.format("%s does not support %s", tagFromIntent, mTagTechnology.getName()));
            } else {
                tag.connect();
                mConnected = true;
                log(String.format("Connected (%s)", mTagTechnology.getSimpleName()));

                for (byte[] command : mCommands) {
                    log(String.format("-> %s", new String(Hex.encodeHex(command)).toUpperCase()));
                    byte[] response = (byte[]) transceiveMethod.invoke(tag, command);
                    log(String.format("<- %s", new String(Hex.encodeHex(response)).toUpperCase()));
                }

                tag.close();
                // mConnected = false;
                log(String.format("Disconnected (%s)", mTagTechnology.getSimpleName()));
            }
        } catch (Exception e) {
            log(ExceptionUtils.getRootCauseStackTrace(e)[0]);
        }
    }

    void log(String str) {
        mLog.append(str + "\n\n");
        mShareIntent.putExtra(Intent.EXTRA_TEXT, mLog.getText());

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(mShareIntent);
        }
    }

}
