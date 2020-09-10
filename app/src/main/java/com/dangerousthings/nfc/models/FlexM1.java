package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class FlexM1 extends Implant implements Serializable
{
    public FlexM1()
    {
        setImplantType("flexM1");
        setImplantImage(R.drawable.generic_implant);
    }
}
