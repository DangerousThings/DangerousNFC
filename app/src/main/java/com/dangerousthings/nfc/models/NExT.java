package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class NExT extends Implant implements Serializable
{
    public NExT()
    {
        setImplantType("NExT");
        setImplantImage(R.drawable.glass);
    }
}
