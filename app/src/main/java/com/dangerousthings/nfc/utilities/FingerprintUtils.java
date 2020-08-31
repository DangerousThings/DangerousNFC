package com.dangerousthings.nfc.utilities;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;

import com.dangerousthings.nfc.enums.TagType;
import com.dangerousthings.nfc.models.FlexDF;
import com.dangerousthings.nfc.models.FlexDF2;
import com.dangerousthings.nfc.models.FlexNExT;
import com.dangerousthings.nfc.models.FlexNT;
import com.dangerousthings.nfc.models.GenericNTAG216;
import com.dangerousthings.nfc.models.Implant;
import com.dangerousthings.nfc.models.NExT;
import com.dangerousthings.nfc.models.XDF2;
import com.dangerousthings.nfc.models.XNT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FingerprintUtils
{
    private static final String GET_VERSION_RESULT_NTAG_216 = "00 04 04 02 01 00 13 03";
    private static final String GET_VERSION_RESULT_DESFIRE_EV1_8K = "AF 04 01 02 01 00 1A 05";

    private static final String NFC_TECH_ISODEP = "android.nfc.tech.IsoDep";
    private static final String NFC_TECH_NFCA = "android.nfc.tech.NfcA";

    public static final String GET_VERSION = "60";

    public static TagType fingerprintNfcTag(Tag tag)
    {
        //send GET_VERSION command
        String get_version = sendNfcHexCommand(GET_VERSION, tag);
        if(get_version == null)
        {
            return TagType.Unknown;
        }
        switch(get_version)
        {
            case GET_VERSION_RESULT_NTAG_216:
                return TagType.Ntag216;
            case GET_VERSION_RESULT_DESFIRE_EV1_8K:
                return TagType.DesfireEv18k;
            default:
                return TagType.Unknown;
        }
    }

    private static String sendNfcHexCommand(String command, Tag tag)
    {
        String tech = tag.getTechList()[0];
        byte[] commandBytes = HexUtils.hexToBytes(command);
        byte[] response = new byte[0];

        try
        {
            if(tech.equals(NFC_TECH_NFCA))
            {
                NfcA nfcA = NfcA.get(tag);
                nfcA.connect();
                response = nfcA.transceive(commandBytes);
            }
            else if(tech.equals(NFC_TECH_ISODEP))
            {
                IsoDep isoDep = IsoDep.get(tag);
                isoDep.connect();
                response = isoDep.transceive(commandBytes);
            }
            if(response.length != 0)
            {
                return HexUtils.bytesToHex(response);
            }
            return null;
        }
        catch(IOException e)
        {
            return null;
        }
    }

    public static List<Implant> getImplantListFromType(TagType tagType)
    {
        List<Implant> implantList = new ArrayList<>();
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
