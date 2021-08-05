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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IItemLongClickListener;
import com.dangerousthings.nfc.interfaces.ItemTouchHelperAdapter;
import com.dangerousthings.nfc.utilities.EncryptionUtils;
import com.dangerousthings.nfc.utilities.NdefUtils;

import java.util.Collections;
import java.util.List;

public class NdefMessageRecyclerAdapter extends RecyclerView.Adapter<NdefMessageRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter
{
    private final List<NdefRecord> _message;
    private final LayoutInflater _inflater;
    private IItemLongClickListener _clickListener;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition)
    {
        if(fromPosition < toPosition)
        {
            for(int i = fromPosition; i < toPosition; i++)
            {
                Collections.swap(_message, i, i + 1);
            }
        }
        else
        {
            for(int i = fromPosition; i > toPosition; i++)
            {
                Collections.swap(_message, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mLabelText;
        public final TextView mSizeText;
        public final ImageView mEncrypted;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
            mLabelText = itemView.findViewById(R.id.record_text_label);
            mSizeText = itemView.findViewById(R.id.record_text_size);
            mEncrypted = itemView.findViewById(R.id.record_image_encrypted);
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
        if (record.toMimeType().contains("$"))
        {
            if (EncryptionUtils.isRecordEncrypted(record))
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
        holder.mLabelText.setText(NdefUtils.getRecordLabel(record));
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