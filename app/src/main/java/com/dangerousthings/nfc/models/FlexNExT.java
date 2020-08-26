package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IImplant;

public class FlexNExT implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "flexNExT";
    }

    @Override
    public int getImplantImage()
    {
        return R.drawable.flexnext;
    }
}
