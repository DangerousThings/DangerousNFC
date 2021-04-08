package com.dangerousthings.nfc.utilities;

import android.nfc.FormatException;
import android.nfc.NdefMessage;

import androidx.room.TypeConverter;

public class Converters
{
    @TypeConverter
    public static NdefMessage fromBytes(byte[] bytes)
    {
        try
        {
            return new NdefMessage(bytes);
        }
        catch (FormatException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    @TypeConverter
    public static byte[] bytesFromNdefMessage(NdefMessage ndefMessage)
    {
        return ndefMessage.toByteArray();
    }
}
