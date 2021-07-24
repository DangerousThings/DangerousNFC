package com.dangerousthings.nfc.utilities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public static boolean writeNdefMessage(Tag tag, NdefMessage message)
    {
        Ndef ndef = Ndef.get(tag);
        try
        {
            if(ndef != null)
            {
                ndef.connect();
                if(ndef.isConnected())
                {
                    ndef.writeNdefMessage(message);
                }
            }
        }
        catch(Exception e)
        {
            try
            {
                ndef.close();
                return false;
            }
            catch(IOException ex)
            {
                return false;
            }
        }
        try
        {
            if(ndef != null)
            {
                ndef.close();
                return true;
            }
        }
        catch(IOException e)
        {
            return false;
        }
        return true;
    }

    public static NdefRecord generateLabeledRecord(String label, NdefRecord record)
    {
        String mimeType = record.toMimeType();
        if(mimeType.contains("|"))
        {
            mimeType = mimeType.substring(0, mimeType.indexOf("|"));
        }
        if(!label.equals(""))
        {
            mimeType = mimeType + "|" + label;
        }
        return NdefRecord.createMime(mimeType, record.getPayload());
    }

    public static String getMimeTypeFromRecord(NdefRecord record)
    {
        String mimeType = record.toMimeType();
        if(mimeType.contains("$"))
        {
            if(mimeType.length() > 1)
            {
                mimeType = mimeType.substring(1, mimeType.length());
            }
        }
        if(mimeType.contains("|"))
        {
             mimeType = mimeType.substring(0, mimeType.indexOf("|"));
        }
        return mimeType;
    }

    public static boolean isRecordLabeled(NdefRecord record)
    {
        return record.toMimeType().contains("|");
    }

    public static String getRecordLabel(NdefRecord record)
    {
        String mimeType = record.toMimeType();
        if(isRecordLabeled(record))
        {
            return mimeType.substring(mimeType.indexOf("|")+1, mimeType.length());
        }
        else
        {
            if(mimeType.contains("$"))
            {
                return mimeType.substring(1, mimeType.length());
            }
            return mimeType;
        }
    }
}
