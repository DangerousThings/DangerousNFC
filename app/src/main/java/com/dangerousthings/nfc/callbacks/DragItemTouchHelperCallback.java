package com.dangerousthings.nfc.callbacks;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dangerousthings.nfc.adapters.NdefMessageRecyclerAdapter;

public class DragItemTouchHelperCallback extends ItemTouchHelper.Callback
{
    private final NdefMessageRecyclerAdapter _adapter;

    public DragItemTouchHelperCallback(NdefMessageRecyclerAdapter adapter)
    {
        _adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled()
    {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled()
    {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder)
    {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
    {
        _adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
    {
    }
}
