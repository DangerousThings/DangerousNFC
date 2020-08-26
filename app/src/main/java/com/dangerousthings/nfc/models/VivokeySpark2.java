package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.interfaces.IImplant;

public class VivokeySpark2 implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "Vivokey Spark 2";
    }

    @Override
    public int getImplantImage()
    {
        return 0;
    }
}
