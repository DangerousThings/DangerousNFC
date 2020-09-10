package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class VivokeyFlexOne extends Implant implements Serializable
{
    public VivokeyFlexOne()
    {
        setImplantType("Vivokey Flex One");
        setImplantImage(R.drawable.flex);
    }
}
