package com.dangerousthings.nfc.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.TypedValue;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.models.Theme;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils
{
    public static int getPrimaryColor(Context context)
    {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] {R.attr.colorPrimary});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public static int getPrimaryVariantColor(Context context)
    {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] {R.attr.colorPrimaryVariant});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public static int getAccentColor(Context context)
    {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] {R.attr.colorAccent});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public static int getSecondaryColor(Context context)
    {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] {R.attr.colorSecondary});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public static List<Theme> getThemeList()
    {
        List<Theme> themes = new ArrayList<>();
        themes.add(new Theme(R.style.DT, "DT", Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"), Color.parseColor("#000000"), Color.parseColor("#F56528")));
        themes.add(new Theme(R.style.OPZ, "OPZ", Color.parseColor("#BCBDC2"), Color.parseColor("#BCBDC2"), Color.parseColor("#4B4E5B"), Color.parseColor("#E0B000")));
        themes.add(new Theme(R.style.Wog, "Wog", Color.parseColor("#FFF6D8"), Color.parseColor("#FFF6D8"), Color.parseColor("#167D7F"), Color.parseColor("#B5E5CF")));
        themes.add(new Theme(R.style.BlackOnWhite, "Black on White", Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"), Color.parseColor("#000000"), Color.parseColor("#7F7F7F")));
        themes.add(new Theme(R.style.MurderedOut, "Murdered Out", Color.parseColor("#BEBEBE"), Color.parseColor("#BEBEBE"), Color.parseColor("#000000"), Color.parseColor("#7F7F7F")));
        themes.add(new Theme(R.style.Zytel, "Zytel", Color.parseColor("#FBB518"), Color.parseColor("#26544E"), Color.parseColor("#0E1012"), Color.parseColor("#D3DCF0")));
        return themes;
    }
}