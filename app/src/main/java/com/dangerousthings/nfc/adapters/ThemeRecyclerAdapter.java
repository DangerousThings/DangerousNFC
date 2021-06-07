package com.dangerousthings.nfc.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IItemClickListener;
import com.dangerousthings.nfc.models.Theme;

import java.util.List;

public class ThemeRecyclerAdapter extends RecyclerView.Adapter<ThemeRecyclerAdapter.ViewHolder>
{
    private final List<Theme> _themes;
    private final LayoutInflater _inflater;
    private IItemClickListener _clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mThemeName;
        public final View mBoxPrimary;
        public final View mBoxPrimaryVariant;
        public final View mBoxSecondary;
        public final View mBoxAccent;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
            mThemeName = itemView.findViewById(R.id.theme_text_name);
            mBoxPrimary = itemView.findViewById(R.id.theme_box_primary);
            mBoxPrimaryVariant = itemView.findViewById(R.id.theme_box_primary_variant);
            mBoxSecondary = itemView.findViewById(R.id.theme_box_secondary);
            mBoxAccent = itemView.findViewById(R.id.theme_box_accent);
            itemView.setOnClickListener(v -> _clickListener.onItemClick(getAdapterPosition()));
        }
    }

    public ThemeRecyclerAdapter(Context context, List<Theme> themes)
    {
        _inflater = LayoutInflater.from(context);
        _themes = themes;
    }

    @NonNull
    @Override
    public ThemeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = _inflater.inflate(R.layout.item_theme, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Theme theme = _themes.get(position);

        holder.mThemeName.setText(theme.getThemeTitle());

        GradientDrawable primaryShape = new GradientDrawable();
        primaryShape.setCornerRadii(new float[]{30,30,0,0,0,0,0,0});
        primaryShape.setColor(theme.getColorPrimary());
        holder.mBoxPrimary.setBackground(primaryShape);

        GradientDrawable primaryVariantShape = new GradientDrawable();
        primaryVariantShape.setCornerRadii(new float[]{0,0,0,0,0,0,30,30});
        primaryVariantShape.setColor(theme.getColorPrimaryVariant());
        holder.mBoxPrimaryVariant.setBackground(primaryVariantShape);

        GradientDrawable accentShape = new GradientDrawable();
        accentShape.setCornerRadii(new float[]{0,0,0,0,30,30,0,0});
        accentShape.setColor(theme.getColorAccent());
        holder.mBoxAccent.setBackground(accentShape);

        GradientDrawable secondaryShape = new GradientDrawable();
        secondaryShape.setCornerRadii(new float[]{0,0,30,30,0,0,0,0});
        secondaryShape.setColor(theme.getColorSecondary());
        holder.mBoxSecondary.setBackground(secondaryShape);
    }

    @Override
    public int getItemCount()
    {
        return _themes.size();
    }

    public int getThemeId(int position)
    {
        return _themes.get(position).getThemeId();
    }

    public void setClickListener(IItemClickListener listener)
    {
        _clickListener = listener;
    }
}