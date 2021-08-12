package com.dangerousthings.nfc.utilities;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;

import com.dangerousthings.nfc.enums.TagFamily;
import com.dangerousthings.nfc.models.Implant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FingerprintUtils
{
    private static final String GET_VERSION_RESULT_NTAG_216 = "00 04 04 02 01 00 13 03";
    private static final String GET_VERSION_RESULT_DESFIRE_EV1_8K = "AF 04 01 02 01 00 1A 05";
    private static final String GET_VERSION_RESULT_DESFIRE_EV2_8K = "AF 04 01 01 12 00 1A 05";
    private static final String GET_VERSION_RESULT_VIVOKEY_FLEX_ONE = "67 00";
    private static final String GET_VERSION_RESULT_NTAG_I2C_PLUS = "00 04 04 05 02 02 15 03";

    private static final String NFC_TECH_ISODEP = "android.nfc.tech.IsoDep";
    private static final String NFC_TECH_NFCA = "android.nfc.tech.NfcA";

    public static final String GET_VERSION = "60";

    public static TagFamily fingerprintNfcTag(Tag tag)
    {
        //send GET_VERSION command
        String get_version = sendNfcHexCommand(GET_VERSION, tag);
        if(get_version == null)
        {
            return TagFamily.UNKNOWN;
        }
        switch(get_version)
        {
            case GET_VERSION_RESULT_NTAG_216:
                return TagFamily.NTAG_Standard;
            case GET_VERSION_RESULT_DESFIRE_EV1_8K:
            case GET_VERSION_RESULT_DESFIRE_EV2_8K:
                return TagFamily.DESFire;
            case GET_VERSION_RESULT_VIVOKEY_FLEX_ONE:
                return TagFamily.Vivokey;
            case GET_VERSION_RESULT_NTAG_I2C_PLUS:
                return TagFamily.NTAG_I2C;
            default:
                return TagFamily.UNKNOWN;
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
}