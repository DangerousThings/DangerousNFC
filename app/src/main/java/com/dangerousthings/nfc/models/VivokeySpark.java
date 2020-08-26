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
    public Image getImplantImage()
    {
        return null;
    }
}
