package com.dangerousthings.nfc.models;

import android.nfc.NdefMessage;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.enums.ImplantModel;
import com.dangerousthings.nfc.enums.TagFamily;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "implant")
public class Implant
{
    public Implant()
    {
        UID = "";
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

    //TagFamily
    @ColumnInfo(name = "TagFamily")
    private TagFamily tagFamily;

    public TagFamily getTagFamily()
    {
        return tagFamily;
    }

    public void setTagFamily(TagFamily tagFamily)
    {
        this.tagFamily = tagFamily;
    }

    //Implant Model
    @ColumnInfo(name = "ImplantModel")
    private ImplantModel implantModel;

    public ImplantModel getImplantModel()
    {
        if(implantModel == null)
        {
            return ImplantModel.Unknown;
        }
        return implantModel;
    }

    public void setImplantModel(ImplantModel implantModel)
    {
        this.implantModel = implantModel;
    }

    public String getImplantModelAsString()
    {
        return implantModel.name().replace("_", " ");
    }

    public List<String> getImplantModelListAsString()
    {
        List<String> modelList = new ArrayList<>();
        switch (tagFamily)
        {
            case NTAG_Standard:
                modelList.add(ImplantModel.xNT.name());
                modelList.add(ImplantModel.flexNT.name());
                break;
            case DESFire:
                modelList.add(ImplantModel.flexDF.name());
                modelList.add(ImplantModel.flexDF2.name());
                modelList.add(ImplantModel.xDF2.name());
                break;
            case Vivokey:
                modelList.add(ImplantModel.Vivokey_Flex_One.name().replace("_", " "));
                modelList.add(ImplantModel.Vivokey_Apex_Flex.name().replace("_", " "));
                modelList.add(ImplantModel.Vivokey_Apex_Max.name().replace("_", " "));
                break;
            case NTAG_I2C:
                modelList.add(ImplantModel.xSIID.name());
                break;
            case UNKNOWN:
                modelList.add(ImplantModel.flexMN.name());
        }
        return modelList;
    }

    //Ndef Capacity
    @ColumnInfo(name = "NdefCapacity")
    private int ndefCapacity;

    public int getNdefCapacity()
    {
        return ndefCapacity;
    }

    public void setNdefCapacity(int ndefCapacity)
    {
        this.ndefCapacity = ndefCapacity;
    }

    //Tag writing
    public boolean writeToTag(NdefMessage message)
    {
        return false;
    }

    //Operations List
    public void getOperationsMenu(Menu menu)
    {
        menu.clear();
        switch(tagFamily)
        {
            case NTAG_Standard: case NTAG_I2C: case DESFire: case Vivokey: case UNKNOWN:
                //menu.addSubMenu(Menu.NONE, R.string.menu_ndef_submenu, Menu.NONE, "NDEF Operations");
                //TODO: Change the color of these submenus and add them back eventually
                //SubMenu ndefMenu = menu.findItem(R.string.menu_ndef_submenu).getSubMenu();
                //ndefMenu.add(0, R.string.menu_view_records, 0, "View NDEF Records");
                //menu.addSubMenu(Menu.NONE, R.string.menu_memory_submenu, Menu.NONE, "Memory Operations");
                //TODO: Change the color of these submenus and add them back eventually
                //SubMenu memoryMenu = menu.findItem(R.string.menu_memory_submenu).getSubMenu();
                //memoryMenu.add(0, R.string.menu_memory_management, 0, "Manage Implant Memory");
                menu.add(0, R.string.menu_view_records, 0, "View NDEF Records");
                menu.add(0, R.string.menu_memory_management, 0, "Manage Implant Memory");
        }
    }
}
