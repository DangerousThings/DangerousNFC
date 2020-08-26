package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class FlexNExT implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "flexNExT";
    }

    @Override
    public Image getImplantImage()
    {
        return null;
    }
}
