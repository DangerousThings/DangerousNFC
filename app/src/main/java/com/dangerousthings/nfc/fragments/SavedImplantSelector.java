package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.SavedImplantRecyclerAdapter;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.models.Implant;

import java.util.List;

public class SavedImplantSelector extends Fragment implements SavedImplantRecyclerAdapter.ItemClickListener
{
    SavedImplantRecyclerAdapter _adapter;
    RecyclerView mRecyclerView;
    List<Implant> _implantList;
    View _selectedRecyclerItem;
    int _position = -1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_select_saved_implant_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        ImplantDatabase implantDatabase = ImplantDatabase.getInstance(getContext());
        _implantList = implantDatabase.implantDao().getImplantList();
        mRecyclerView = getView().findViewById(R.id.recycler_saved_implant_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new SavedImplantRecyclerAdapter(getActivity(), _implantList);
    }

    @Override
    public void onItemClick(View view, int position)
    {

    }
}