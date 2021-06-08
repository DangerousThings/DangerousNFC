package com.dangerousthings.nfc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IItemLongClickListener;
import com.dangerousthings.nfc.models.Implant;

import java.util.List;

public class SavedImplantRecyclerAdapter extends RecyclerView.Adapter<SavedImplantRecyclerAdapter.ViewHolder>
{
    private final LayoutInflater _inflater;
    private IItemLongClickListener _clickListener;
    private final List<Implant> _savedImplants;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mNameText;
        public final TextView mTypeText;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
            mNameText = itemView.findViewById(R.id.item_saved_implants_text_name);
            mTypeText = itemView.findViewById(R.id.item_saved_implants_text_type);
            mView.setOnClickListener(v -> _clickListener.onItemClick(getAdapterPosition()));
            mView.setOnLongClickListener(v -> _clickListener.onItemLongClick(getAdapterPosition()));
        }
    }

    public SavedImplantRecyclerAdapter(Context context, List<Implant> savedImplants)
    {
        _inflater = LayoutInflater.from(context);
        _savedImplants = savedImplants;
    }

    @NonNull
    @Override
    public SavedImplantRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = _inflater.inflate(R.layout.item_saved_implant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedImplantRecyclerAdapter.ViewHolder holder, int position)
    {
        Implant implant = _savedImplants.get(position);
        holder.mNameText.setText(implant.getImplantName());
        holder.mTypeText.setText(implant.getImplantModelAsString());
    }

    @Override
    public int getItemCount()
    {
        return _savedImplants.size();
    }

    public void setClickListener(IItemLongClickListener listener)
    {
        _clickListener = listener;
    }

    public Implant getImplant(int position)
    {
        return _savedImplants.get(position);
    }
}
