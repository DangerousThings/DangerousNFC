package com.dangerousthings.nfc.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.nfc.NdefRecord;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IItemClickListener;
import com.dangerousthings.nfc.interfaces.IItemLongClickListener;

import java.util.List;

public class NdefMessageRecyclerAdapter extends RecyclerView.Adapter<NdefMessageRecyclerAdapter.ViewHolder>
{
    private final List<NdefRecord> _message;
    private final LayoutInflater _inflater;
    private IItemLongClickListener _clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mRecordCountText;
        public final TextView mMimeTypeText;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
            mRecordCountText = itemView.findViewById(R.id.message_text_record_number);
            mMimeTypeText = itemView.findViewById(R.id.message_text_mime);
            mView.setOnClickListener(v -> _clickListener.onItemClick(getAdapterPosition()));
            mView.setOnLongClickListener(v -> _clickListener.onItemLongClick(getAdapterPosition()));
        }
    }

    public NdefMessageRecyclerAdapter(Context context, List<NdefRecord> message)
    {
        _inflater = LayoutInflater.from(context);
        _message = message;
    }

    @NonNull
    @Override
    public NdefMessageRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = _inflater.inflate(R.layout.item_ndef_record, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        NdefRecord record = _message.get(position);
        holder.mRecordCountText.setText("Record " + position);
        holder.mMimeTypeText.setText(record.toMimeType());
    }

    @Override
    public int getItemCount()
    {
        return _message.size();
    }

    public NdefRecord getRecord(int position)
    {
        return _message.get(position);
    }

    public void setClickListener(IItemLongClickListener listener)
    {
        _clickListener = listener;
    }
}