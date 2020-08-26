package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class XSLX implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "xSLX";
    }

    @Override
    public int getImplantImage()
    {
        return 0;
    }
}
