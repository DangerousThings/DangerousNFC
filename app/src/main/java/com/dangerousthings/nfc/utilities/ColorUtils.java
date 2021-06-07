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

    public static int getPrimaryDarkColor(Context context)
    {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] {R.attr.colorPrimaryDark});
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
        themes.add(new Theme(R.style.DT, "DT", Color.parseColor("#FFFFFF"), Color.parseColor("#000000"), Color.parseColor("#FFFFFF"), Color.parseColor("#F56528")));
        themes.add(new Theme(R.style.DEOT, "DEOT", Color.parseColor("#008F78"), Color.parseColor("#0A1017"), Color.parseColor("#008F78"), Color.parseColor("#BBC6C5")));
        themes.add(new Theme(R.style.OPZ, "OPZ", Color.parseColor("#BCBDC2"), Color.parseColor("#4B4E5B"), Color.parseColor("#BCBDC2"), Color.parseColor("#E0B000")));
        themes.add(new Theme(R.style.Wog, "Wog", Color.parseColor("#FFF6D8"), Color.parseColor("#167D7F"), Color.parseColor("#FFF6D8"), Color.parseColor("#B5E5CF")));
        themes.add(new Theme(R.style.BlackOnWhite, "Black on White", Color.parseColor("#FFFFFF"), Color.parseColor("#000000"), Color.parseColor("#FFFFFF"), Color.parseColor("#7F7F7F")));
        themes.add(new Theme(R.style.MurderedOut, "Murdered Out", Color.parseColor("#BEBEBE"), Color.parseColor("#000000"), Color.parseColor("#BEBEBE"), Color.parseColor("#7F7F7F")));
        themes.add(new Theme(R.style.Zytel, "Zytel", Color.parseColor("#FBB518"), Color.parseColor("#143130"), Color.parseColor("#C08A8E"), Color.parseColor("#DFDCF0")));
        return themes;
    }
}