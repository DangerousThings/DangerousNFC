package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IImplant;

public class FlexNT implements IImplant
{

    @Override
    public String getImplantType()
    {
        return "flexNT";
    }

    @Override
    public Image getImplantImage()
    {
        return null;
    }
}
