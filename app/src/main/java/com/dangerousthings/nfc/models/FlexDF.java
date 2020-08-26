package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IImplant;

public class FlexDF implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "flexDF";
    }

    @Override
    public int getImplantImage()
    {
        return R.drawable.flex;
    }
}
