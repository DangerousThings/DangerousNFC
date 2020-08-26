package com.dangerousthings.nfc.interfaces;

import android.media.Image;

public interface IImplant
{
    //implant type getter/setter
    String getImplantType();

    //implant image resource getter/setter
    Image getImplantImage();

    //TODO: add declarations of read write methods that will allow for unique tag requirements for both
    /*
    readData();
    writeData();
    */
}
