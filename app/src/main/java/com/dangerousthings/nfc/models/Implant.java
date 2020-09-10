package com.dangerousthings.nfc.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//work ROOM database logic into this base class
@Entity(tableName = "implant")
public class Implant
{
    //UID
    @PrimaryKey
    @NonNull
    private String uid;
    public String getUid()
    {
        return uid;
    }
    public void setUid(String uid)
    {
        this.uid = uid;
    }

    //Implant Name
    @ColumnInfo(name = "implantName")
    private String implantName;
    public String getImplantName()
    {
        if(implantName == null)
        {
            return "Implant";
        }
        else
        {
            return implantName;
        }
    }
    public void setImplantName(String value)
    {
        implantName = value;
    }

    //Implant Type
    @ColumnInfo(name = "implantType")
    private String implantType;
    public String getImplantType()
    {
        return implantType;
    }
    public void setImplantType(String implantType)
    {
        this.implantType = implantType;
    }

    //Implant Image
    @ColumnInfo(name = "implantImage")
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
