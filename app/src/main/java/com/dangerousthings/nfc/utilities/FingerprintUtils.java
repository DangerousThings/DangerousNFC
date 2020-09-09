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
import com.dangerousthings.nfc.models.Implant;
import com.dangerousthings.nfc.models.NExT;
import com.dangerousthings.nfc.models.VivokeyFlexOne;
import com.dangerousthings.nfc.models.XDF2;
import com.dangerousthings.nfc.models.XNT;
import com.dangerousthings.nfc.models.XSIID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.dangerousthings.nfc.enums.ImplantType.xSIID;

public class FingerprintUtils
{
    private static final String GET_VERSION_RESULT_NTAG_216 = "00 04 04 02 01 00 13 03";
    private static final String GET_VERSION_RESULT_DESFIRE_EV1_8K = "AF 04 01 02 01 00 1A 05";
    private static final String GET_VERSION_RESULT_VIVOKEY_FLEX_ONE = "67 00";
    private static final String GET_VERSION_RESULT_NTAG_I2C_PLUS = "00 04 04 05 02 02 15 03";

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
            case GET_VERSION_RESULT_VIVOKEY_FLEX_ONE:
                return TagType.SmartMX2;
            case GET_VERSION_RESULT_NTAG_I2C_PLUS:
                return TagType.NtagI2C;
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
                break;
            case DesfireEv18k:
                implantList.add(new FlexDF());
                break;
            case SmartMX2:
                implantList.add(new VivokeyFlexOne());
                break;
            case NtagI2C:
                implantList.add(new XSIID());
                break;
        }
        return implantList;
    }
}
