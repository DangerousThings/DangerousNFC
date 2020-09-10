package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class XDF2 extends Implant implements Serializable
{
    public XDF2()
    {
        setImplantType("xDF2");
        setImplantImage(R.drawable.glass);
    }
}
