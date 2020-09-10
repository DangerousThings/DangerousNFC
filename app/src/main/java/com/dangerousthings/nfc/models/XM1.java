package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class XM1 extends Implant implements Serializable
{
    public XM1()
    {
        setImplantType("xM1");
        setImplantImage(R.drawable.glass);
    }
}
