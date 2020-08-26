package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class XM1 implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "xM1";
    }

    @Override
    public Image getImplantImage()
    {
        return null;
    }
}
