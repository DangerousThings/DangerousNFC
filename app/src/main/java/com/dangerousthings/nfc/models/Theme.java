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

    private int colorPrimaryVariant;

    public int getColorPrimaryVariant()
    {
        return colorPrimaryVariant;
    }

    public void setColorPrimaryVariant(int colorPrimaryVariant)
    {
        this.colorPrimaryVariant = colorPrimaryVariant;
    }

    private int colorSecondary;

    public int getColorSecondary()
    {
        return colorSecondary;
    }

    public void setColorSecondary(int colorSecondary)
    {
        this.colorSecondary = colorSecondary;
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

    public Theme(int id, String themeName, int primary,int secondary, int primaryVariant, int accent)
    {
        themeId = id;
        themeTitle = themeName;
        colorPrimary = primary;
        colorPrimaryVariant = primaryVariant;
        colorSecondary = secondary;
        colorAccent = accent;
    }
}
