package com.dangerousthings.nfc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IEditButton;

import java.util.Objects;

public class DetailsToolbar extends Fragment
{
    Toolbar mToolbar;
    ImageButton mToolbarWriteButton;
    IEditButton buttonInterface;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.toolbar_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        setTitleBar();
        setUpToolbarButton();
    }

    public void setInterface(IEditButton iEditButton)
    {
        this.buttonInterface = iEditButton;
    }

    private void setTitleBar()
    {
        mToolbar = Objects.requireNonNull(getView()).findViewById(R.id.toolbar_read);
        mToolbarWriteButton = Objects.requireNonNull(getView()).findViewById(R.id.toolbar_edit_button);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }

    private void setUpToolbarButton()
    {
        mToolbarWriteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buttonInterface.buttonClicked();
            }
        });
    }
}