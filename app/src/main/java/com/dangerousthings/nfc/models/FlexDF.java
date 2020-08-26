package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class FlexDF implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "flexDF";
    }

    @Override
    public Image getImplantImage()
    {
        return null;
    }
}
