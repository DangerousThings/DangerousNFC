package com.dangerousthings.nfc.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.nfc.NdefRecord;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IItemLongClickListener;
import com.dangerousthings.nfc.utilities.EncryptionUtils;
import com.dangerousthings.nfc.utilities.NdefUtils;

import java.util.List;

public class NdefMessageRecyclerAdapter extends RecyclerView.Adapter<NdefMessageRecyclerAdapter.ViewHolder>
{
    private final List<NdefRecord> _message;
    private final LayoutInflater _inflater;
    private IItemLongClickListener _clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mMimeTypeText;
        public final TextView mSizeText;
        public final ImageView mEncrypted;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
            mMimeTypeText = itemView.findViewById(R.id.message_text_mimetype);
            mSizeText = itemView.findViewById(R.id.message_text_size);
            mEncrypted = itemView.findViewById(R.id.message_image_encrypted);
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
        if (record.toMimeType().contains("_"))
        {
            if (record.toMimeType().substring(0, record.toMimeType().indexOf("_")).trim().equals("encrypted"))
            {
                holder.mEncrypted.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.mEncrypted.setVisibility(View.GONE);
            }
        }
        else
        {
            holder.mEncrypted.setVisibility(View.GONE);
        }
        holder.mSizeText.setText(record.getPayload().length + " Bytes");
        holder.mMimeTypeText.setText(EncryptionUtils.getDecryptedMimeType(record.toMimeType()));
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