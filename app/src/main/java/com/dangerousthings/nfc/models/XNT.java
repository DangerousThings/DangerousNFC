package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class XNT implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "xNT";
    }

    @Override
    public Image getImplantImage()
    {
        return null;
    }
}
