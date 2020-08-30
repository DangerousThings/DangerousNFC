package com.dangerousthings.nfc.models;

//work ROOM database logic into this base class
public class Implant
{
    private String implantType;
    public String getImplantType()
    {
        return implantType;
    }
    public void setImplantType(String implantType)
    {
        this.implantType = implantType;
    }

    private int implantImage;
    public int getImplantImage()
    {
        return implantImage;
    }
    public void setImplantImage(int implantImage)
    {
        this.implantImage = implantImage;
    }
}
