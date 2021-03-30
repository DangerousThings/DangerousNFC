package com.dangerousthings.nfc.models;

public class Theme
{
    private int themeId;

    public int getThemeId()
    {
        return themeId;
    }

    public void setThemeId(int themeId)
    {
        this.themeId = themeId;
    }

    private String themeTitle;

    public String getThemeTitle()
    {
        return themeTitle;
    }

    public void setThemeTitle(String themeTitle)
    {
        this.themeTitle = themeTitle;
    }

    private int colorPrimary;

    public int getColorPrimary()
    {
        return colorPrimary;
    }

    public void setColorPrimary(int colorPrimary)
    {
        this.colorPrimary = colorPrimary;
    }

    private int colorPrimaryDark;

    public int getColorPrimaryDark()
    {
        return colorPrimaryDark;
    }

    public void setColorPrimaryDark(int colorPrimaryDark)
    {
        this.colorPrimaryDark = colorPrimaryDark;
    }

    private int colorAccent;

    public int getColorAccent()
    {
        return colorAccent;
    }

    public void setColorAccent(int colorAccent)
    {
        this.colorAccent = colorAccent;
    }

    public Theme(int id, String themeName, int primary, int primaryDark, int accent)
    {
        themeId = id;
        themeTitle = themeName;
        colorPrimary = primary;
        colorPrimaryDark = primaryDark;
        colorAccent = accent;
    }
}
