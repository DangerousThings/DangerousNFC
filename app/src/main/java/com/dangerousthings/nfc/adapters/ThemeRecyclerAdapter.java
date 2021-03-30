package com.dangerousthings.nfc.adapters;

import android.content.Context;
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
        public final View mBoxPrimaryDark;
        public final View mBoxAccent;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
            mThemeName = itemView.findViewById(R.id.theme_text_name);
            mBoxPrimary = itemView.findViewById(R.id.theme_box_primary);
            mBoxPrimaryDark = itemView.findViewById(R.id.theme_box_primary_dark);
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
        holder.mBoxPrimary.setBackgroundColor(theme.getColorPrimary());
        holder.mBoxPrimaryDark.setBackgroundColor(theme.getColorPrimaryDark());
        holder.mBoxAccent.setBackgroundColor(theme.getColorAccent());
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