package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class XSIID implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "xSIID";
    }

    @Override
    public int getImplantImage()
    {
        return 0;
    }
}
