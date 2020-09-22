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

public class RecyclerDialogAdapter extends RecyclerView.Adapter<RecyclerDialogAdapter.ViewHolder>
{
    private List<Implant> _data;
    private LayoutInflater _inflater;
    private ItemClickListener _clickListener;

    public RecyclerDialogAdapter(Context context, List<Implant> data)
    {
        _inflater = LayoutInflater.from(context);
        _data = data;
    }

    @Override
    public RecyclerDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = _inflater.inflate(R.layout.recycler_select_new_implant_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerDialogAdapter.ViewHolder holder, int position)
    {
        Implant implant = _data.get(position);
        holder.mTextView.setText(implant.getImplantType());
        holder.mImageView.setImageResource(implant.getImplantImage());
    }

    @Override
    public int getItemCount()
    {
        return _data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mTextView;
        ImageView mImageView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textview_recycler_row);
            mImageView = itemView.findViewById(R.id.imageview_recycler_row);
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

    public void setClickListener(ItemClickListener itemClickListener)
    {
        _clickListener = itemClickListener;
    }

    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }
}
