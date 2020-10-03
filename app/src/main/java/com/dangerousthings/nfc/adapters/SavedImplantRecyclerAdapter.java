package com.dangerousthings.nfc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.models.Implant;

import java.util.List;

public class SavedImplantRecyclerAdapter extends RecyclerView.Adapter<SavedImplantRecyclerAdapter.ViewHolder>
{
    private List<Implant> _data;
    private LayoutInflater _inflater;
    private RecyclerDialogAdapter.ItemClickListener _clickListener;

    public SavedImplantRecyclerAdapter(Context context, List<Implant> data)
    {
        _inflater = LayoutInflater.from(context);
        _data = data;
    }

    @Override
    public SavedImplantRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = _inflater.inflate(R.layout.recycler_select_saved_implant_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedImplantRecyclerAdapter.ViewHolder holder, int position)
    {
        Implant implant = _data.get(position);
        holder.mNameTextView.setText(implant.getImplantName());
        holder.mTypeTextView.setText(implant.getImplantType());
        holder.mImageView.setImageResource(implant.getImplantImage());
    }

    @Override
    public int getItemCount()
    {
        return _data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mNameTextView;
        public TextView mTypeTextView;
        ImageView mImageView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.textview_recycler_implant_name);
            mTypeTextView = itemView.findViewById(R.id.textview_recycler_implant_type);
            mImageView = itemView.findViewById(R.id.imageview_recycler_implant);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if(_clickListener != null)
            {
                _clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public Implant getItem(int id)
    {
        return _data.get(id);
    }

    public void setClickListener(RecyclerDialogAdapter.ItemClickListener itemClickListener)
    {
        _clickListener = itemClickListener;
    }

    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }
}
