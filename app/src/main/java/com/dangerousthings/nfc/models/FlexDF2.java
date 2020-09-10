package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class FlexDF2 extends Implant implements Serializable
{
    public FlexDF2()
    {
        setImplantType("flexDF2");
        setImplantImage(R.drawable.flex);
    }
}
