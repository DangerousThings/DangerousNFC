package com.dangerousthings.nfc.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.adapters.ThemeRecyclerAdapter;
import com.dangerousthings.nfc.interfaces.IItemClickListener;
import com.dangerousthings.nfc.models.Theme;
import com.dangerousthings.nfc.pages.MainActivity;
import com.dangerousthings.nfc.utilities.ColorUtils;

public class ThemeFragment extends Fragment implements IItemClickListener
{
    RecyclerView mRecyclerView;
    ThemeRecyclerAdapter _recyclerAdapter;
    ImageButton mBackButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_theme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mRecyclerView = view.findViewById(R.id.theme_recycler);
        mBackButton = view.findViewById(R.id.theme_button_back);

        _recyclerAdapter = new ThemeRecyclerAdapter(getActivity(), ColorUtils.getThemeList());
        _recyclerAdapter.setClickListener(this);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(_recyclerAdapter);

        mBackButton.setOnClickListener(v -> getActivity().onBackPressed());
    }

    @Override
    public void onItemClick(int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Changing theme requires a restart of the application, do you want to do this?");
        builder.setPositiveButton("Yes", (dialog, which) ->
        {
            SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(getString(R.string.saved_theme), _recyclerAdapter.getThemeId(position));
            editor.apply();

            Intent restartIntent = new Intent(getActivity(), MainActivity.class);
            restartIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(restartIntent);
        });
        builder.setNegativeButton("No", (dialog, which) ->
        {
            dialog.cancel();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}