package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class VivokeySpark implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "Vivokey Spark";
    }

    @Override
    public int getImplantImage()
    {
        return 0;
    }
}
