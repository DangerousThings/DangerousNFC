package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class VivokeyApexFlex implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "Vivokey Apex Flex";
    }

    @Override
    public Image getImplantImage()
    {
        return null;
    }
}
