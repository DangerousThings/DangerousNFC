package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class FlexNT extends Implant implements Serializable
{
    public FlexNT()
    {
        setImplantType("flexNT");
        setImplantImage(R.drawable.flexnt);
    }
}
