package com.dangerousthings.nfc.utilities;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;

import com.dangerousthings.nfc.enums.TagType;
import com.dangerousthings.nfc.interfaces.IImplant;
import com.dangerousthings.nfc.models.FlexDF;
import com.dangerousthings.nfc.models.FlexDF2;
import com.dangerousthings.nfc.models.FlexNExT;
import com.dangerousthings.nfc.models.FlexNT;
import com.dangerousthings.nfc.models.GenericNTAG216;
import com.dangerousthings.nfc.models.NExT;
import com.dangerousthings.nfc.models.XDF2;
import com.dangerousthings.nfc.models.XNT;

import java.util.ArrayList;
import java.util.List;

public class FingerprintUtils
{
    public static final int SIZE_NTAG_216 = 868;
    public static final int SIZE_DESFIRE_EV1_8K = 7676;

    public static TagType fingerprintNfcTag(Tag tag)
    {
        Ndef ndef = Ndef.get(tag);
        int capacity = ndef.getMaxSize();
        switch(capacity)
        {
            case SIZE_NTAG_216:
                return TagType.Ntag216;
            case SIZE_DESFIRE_EV1_8K:
                return TagType.DesfireEv18k;
        }
        return null;
    }

    public static List<IImplant> getImplantListFromType(TagType tagType)
    {
        List<IImplant> implantList = new ArrayList<>();
        switch(tagType)
        {
            case Ntag216:
                implantList.add(new XNT());
                implantList.add(new FlexNT());
                implantList.add(new NExT());
                implantList.add(new FlexNExT());
                implantList.add(new GenericNTAG216());
                break;
            case DesfireEv18k:
                implantList.add(new FlexDF());
                implantList.add(new FlexDF2());
                implantList.add(new XDF2());
                break;
        }
        return implantList;
    }
}
