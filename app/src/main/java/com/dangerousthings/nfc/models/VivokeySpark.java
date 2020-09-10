package com.dangerousthings.nfc.models;

import com.dangerousthings.nfc.R;

import java.io.Serializable;

public class VivokeySpark extends Implant implements Serializable
{
    public VivokeySpark()
    {
        setImplantType("Vivokey Spark");
        setImplantImage(R.drawable.glass);
    }
}
