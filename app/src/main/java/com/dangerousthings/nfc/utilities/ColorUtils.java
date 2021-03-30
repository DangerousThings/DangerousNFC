package com.dangerousthings.nfc.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.TypedValue;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.models.Theme;
import com.dangerousthings.nfc.pages.SettingsActivity;

import org.xmlpull.v1.XmlPullParser;

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

    public static List<Theme> getThemeList()
    {
        List<Theme> themes = new ArrayList<>();
        themes.add(new Theme(R.style.DT, "DT", Color.parseColor("#FFFFFF"), Color.parseColor("#000000"), Color.parseColor("#F56528")));
        themes.add(new Theme(R.style.DEOT, "DEOT", Color.parseColor("#008F78"), Color.parseColor("#0A1017"), Color.parseColor("#BBC6C5")));
        themes.add(new Theme(R.style.OPZ, "OPZ", Color.parseColor("#BCBDC2"), Color.parseColor("#4B4E5B"), Color.parseColor("#E0B000")));
        return themes;
    }
}
