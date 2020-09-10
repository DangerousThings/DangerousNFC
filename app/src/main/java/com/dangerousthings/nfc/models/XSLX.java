package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class XSLX extends Implant implements Serializable
{
    public XSLX()
    {
        setImplantType("xSLX");
        setImplantImage(R.drawable.glass);
    }
}
