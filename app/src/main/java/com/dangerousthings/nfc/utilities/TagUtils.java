package com.dangerousthings.nfc.utilities;

import android.nfc.Tag;
import android.nfc.tech.Ndef;

public class TagUtils
{
    public static int getNdefCapacity(Tag tag)
    {
        Ndef ndef = Ndef.get(tag);
        return ndef.getMaxSize();
    }
}
