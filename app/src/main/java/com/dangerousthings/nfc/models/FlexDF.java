package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class FlexDF extends Implant implements Serializable
{
    public FlexDF()
    {
        setImplantType("flexDF");
        setImplantImage(R.drawable.flex);
    }
}
