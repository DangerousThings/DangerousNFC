package com.dangerousthings.nfc.models;

import android.nfc.NdefMessage;
import android.nfc.Tag;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "implant")
public class Implant
{
    public Implant()
    {
        UID = null;
    }

    //Device UID
    @PrimaryKey
    @NonNull
    private String UID;

    @NonNull
    public String getUID()
    {
        return UID;
    }

    public void setUID(@NonNull String UID)
    {
        this.UID = UID;
    }

    //Device Name
    @ColumnInfo(name = "Name")
    private String implantName;

    public String getImplantName()
    {
        if(implantName == null)
        {
            return "Implant";
        }
        return implantName;
    }

    public void setImplantName(String implantName)
    {
        this.implantName = implantName;
    }

    //NDEF Message
    @ColumnInfo(name = "NdefMessage")
    private NdefMessage ndefMessage;

    public NdefMessage getNdefMessage()
    {
        return ndefMessage;
    }

    public void setNdefMessage(NdefMessage ndefMessage)
    {
        this.ndefMessage = ndefMessage;
    }
}
