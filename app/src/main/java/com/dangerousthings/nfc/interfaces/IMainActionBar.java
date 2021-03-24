package com.dangerousthings.nfc.interfaces;

import com.dangerousthings.nfc.enums.MainActionBarState;

public interface IMainActionBar
{
    void drawerButtonClicked();
    void mainActionToggled(MainActionBarState state);
    void settingsButtonClicked();
}
