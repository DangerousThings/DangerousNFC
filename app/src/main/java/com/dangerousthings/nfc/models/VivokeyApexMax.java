package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class VivokeyApexMax implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "Vivokey Apex Max";
    }

    @Override
    public int getImplantImage()
    {
        return 0;
    }
}
