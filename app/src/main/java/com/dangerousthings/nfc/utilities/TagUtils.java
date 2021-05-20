package com.dangerousthings.nfc.utilities;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;

import com.dangerousthings.nfc.enums.ImplantModel;
import com.dangerousthings.nfc.enums.TagFamily;

import java.util.ArrayList;
import java.util.List;

public class TagUtils
{
    public static int getNdefCapacity(Tag tag)
    {
        Ndef ndef = Ndef.get(tag);
        return ndef.getMaxSize();
    }
}
