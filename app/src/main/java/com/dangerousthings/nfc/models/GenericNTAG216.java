package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IImplant;

public class GenericNTAG216 implements IImplant
{
    @Override
    public String getImplantType() {
        return "Generic NTAG 216";
    }

    @Override
    public int getImplantImage() {
        return R.drawable.ntag216;
    }
}
