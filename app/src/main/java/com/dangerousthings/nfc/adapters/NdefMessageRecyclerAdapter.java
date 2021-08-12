package com.dangerousthings.nfc.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.nfc.NdefRecord;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IItemLongClickListener;
import com.dangerousthings.nfc.interfaces.IOnStartDragListener;
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
    private final IOnStartDragListener _dragStartListener;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition)
    {
        Collections.swap(_message, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mLabelText;
        public final TextView mSizeText;
        public final ImageView mEncrypted;
        public final ImageView mHandle;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
            mLabelText = itemView.findViewById(R.id.record_text_label);
            mSizeText = itemView.findViewById(R.id.record_text_size);
            mEncrypted = itemView.findViewById(R.id.record_image_encrypted);
            mHandle = itemView.findViewById(R.id.record_image_handle);
            mView.setOnClickListener(v -> _clickListener.onItemClick(getAdapterPosition()));
            mView.setOnLongClickListener(v -> _clickListener.onItemLongClick(getAdapterPosition()));
        }
    }

    public NdefMessageRecyclerAdapter(Context context, List<NdefRecord> message, IOnStartDragListener dragStartListener)
    {
        _inflater = LayoutInflater.from(context);
        _message = message;
        _dragStartListener = dragStartListener;
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
        holder.mHandle.setOnTouchListener((v, event) ->
        {
            if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
            {
                _dragStartListener.onStartDrag(holder);
            }
            return false;
        });
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