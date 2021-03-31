package com.dangerousthings.nfc.utilities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NdefUtils
{
    public static String parseStringNdefPayloadFromIntent(Intent intent)
    {
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
        {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(rawMessages != null)
            {
                NdefMessage ndefMessage = (NdefMessage)rawMessages[0];
                NdefRecord ndefRecord = ndefMessage.getRecords()[0];
                byte[] payload = ndefRecord.getPayload();
                return getEnStringFromBytes(payload);
            }
        }
        return "NDEF payload could not be parsed to a string";
    }

    public static String getEnStringFromBytes(byte[] payload)
    {
        if(payload.length != 0)
        {
            int languageCodeLength = payload[0] & 51;
            return new String(payload, languageCodeLength +1, payload.length - languageCodeLength - 1, StandardCharsets.UTF_8);
        }
        return "";
    }

    public static String getStringFromBytes(byte[] payload)
    {
        if(payload.length != 0)
        {
            return new String(payload);
        }
        return "";
    }

    public static NdefMessage getNdefMessage(Intent intent)
    {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(rawMessages != null)
        {
            return (NdefMessage)rawMessages[0];
        }
        return null;
    }

    public static NdefRecord createRandomByteRecord(int maxTagCapacity)
    {
        String lang = "en";
        byte[] langBytes = lang.getBytes(StandardCharsets.US_ASCII);
        byte[] randomByteArray = new byte[maxTagCapacity];
        Random random = new Random();
        random.nextBytes(randomByteArray);
        byte[] payload = new byte[1 + langBytes.length + randomByteArray.length];
        payload[0] = (byte)langBytes.length;
        System.arraycopy(langBytes, 0, payload, 1, langBytes.length);
        System.arraycopy(randomByteArray, 0, payload, 1 + langBytes.length, randomByteArray.length);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
    }

    public static List<NdefRecord> getNdefRecordList(NdefMessage message)
    {
        return Arrays.asList(message.getRecords());
    }
}
