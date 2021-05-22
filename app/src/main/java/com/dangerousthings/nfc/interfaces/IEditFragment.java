package com.dangerousthings.nfc.interfaces;

import android.nfc.NdefRecord;

public interface IEditFragment
{
    NdefRecord getNdefRecord();
    int getPayloadSize();
    void setPayloadTrackingInterface(ITracksPayloadSize tracker);
}
