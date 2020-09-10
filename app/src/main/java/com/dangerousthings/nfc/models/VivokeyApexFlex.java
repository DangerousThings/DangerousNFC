package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class VivokeyApexFlex extends Implant implements Serializable
{
    public VivokeyApexFlex()
    {
        setImplantType("Vivokey Apex Flex");
        setImplantImage(R.drawable.flex);
    }
}
