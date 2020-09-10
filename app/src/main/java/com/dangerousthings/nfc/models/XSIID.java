package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class XSIID extends Implant implements Serializable
{
    public XSIID()
    {
        setImplantType("xSIID");
        setImplantImage(R.drawable.glass);
    }
}
