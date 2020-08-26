package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class FlexDF2 implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "flexDF2";
    }

    @Override
    public Image getImplantImage()
    {
        return null;
    }
}
