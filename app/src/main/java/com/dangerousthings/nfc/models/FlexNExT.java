package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class FlexNExT extends Implant implements Serializable
{
    public FlexNExT()
    {
        setImplantType("flexNExT");
        setImplantImage(R.drawable.flexnext);
    }
}
