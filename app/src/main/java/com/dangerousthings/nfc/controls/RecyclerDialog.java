package com.dangerousthings.nfc.controls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.RecyclerDialogAdapter;
import com.dangerousthings.nfc.interfaces.IImplant;

import java.util.List;
import java.util.Objects;

public class RecyclerDialog extends DialogFragment
{
    RecyclerDialogAdapter _adapter;
    RecyclerView mRecyclerView;
    List<IImplant> _implantList;

    public RecyclerDialog(List<IImplant> implantList)
    {
        _implantList = implantList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_recycler_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recycler_implant_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new RecyclerDialogAdapter(getActivity(), _implantList);
        //TODO: set onClickListener here
        mRecyclerView.setAdapter(_adapter);
    }
}
