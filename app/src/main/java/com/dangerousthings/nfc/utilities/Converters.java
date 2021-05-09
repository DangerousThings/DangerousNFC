package com.dangerousthings.nfc.utilities;

import android.nfc.FormatException;
import android.nfc.NdefMessage;

import androidx.room.TypeConverter;

import com.dangerousthings.nfc.enums.TagFamily;

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

    @TypeConverter
    public static String stringFromTagFamily(TagFamily family)
    {
        return family.name();
    }

    @TypeConverter
    public static TagFamily tagFamilyFromString(String s)
    {
        return TagFamily.valueOf(s);
    }
}
