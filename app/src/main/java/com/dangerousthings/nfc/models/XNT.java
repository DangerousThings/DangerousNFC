package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class XNT extends Implant implements Serializable
{
    public XNT()
    {
        setImplantType("xNT");
        setImplantImage(R.drawable.glass);
    }
}
