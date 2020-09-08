package com.dangerousthings.nfc.fragments;

import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.RecyclerDialogAdapter;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.models.Implant;
import com.dangerousthings.nfc.utilities.HexUtils;

import java.util.List;
import java.util.Objects;

public class ImplantSelectionRecycler extends Fragment implements RecyclerDialogAdapter.ItemClickListener
{
    RecyclerDialogAdapter _adapter;
    RecyclerView mRecyclerView;
    List<Implant> _implantList;
    View _selectedRecyclerItem;
    Button mCancelButton;
    Button mSelectButton;
    Tag _tag;
    int _position = -1;

    public ImplantSelectionRecycler(List<Implant> implantList, Tag tag)
    {
        _implantList = implantList;
        _tag = tag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_select_implant_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recycler_implant_list);
        mCancelButton = getView().findViewById(R.id.button_cancel_recycler);
        mSelectButton = getView().findViewById(R.id.button_select_recycler);

        setButtonActions();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new RecyclerDialogAdapter(getActivity(), _implantList);
        _adapter.setClickListener(this);
        mRecyclerView.setAdapter(_adapter);
    }

    private void setButtonActions()
    {
        mCancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().onBackPressed();
            }
        });
        mSelectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(_position != -1)
                {
                    Implant implant = _adapter.getItem(_position);
                    byte[] uidBytes = _tag.getId();
                    String hexString = HexUtils.bytesToHex(uidBytes);
                    implant.setUid(hexString);
                    ImplantDatabase implantDatabase = ImplantDatabase.getInstance(getActivity());
                    implantDatabase.implantDao().insertImplant(implant);
                    getActivity().onBackPressed();
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position)
    {
        if(_selectedRecyclerItem != null)
        {
            _selectedRecyclerItem.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.black_rounded_background));
        }
        view.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.green_rounded_background));
        _selectedRecyclerItem = view;
        _position = position;
    }
}
