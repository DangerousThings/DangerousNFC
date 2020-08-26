package com.dangerousthings.nfc.models;

import android.media.Image;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IImplant;

public class XDF2 implements IImplant
{
    @Override
    public String getImplantType()
    {
        return "xDF2";
    }

    @Override
    public int getImplantImage()
    {
        return R.drawable.glass;
    }
}
