package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class FlexM1 implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "flexM1";
    }

    @Override
    public int getImplantImage()
    {
        return 0;
    }
}
