package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.models.Implant;

public class EditImplantDetails extends Fragment
{
    Implant _implant;

    EditText mImplantNameEditText;
    TextView mImplantTypeTextView;
    TextView mImplantUidTextView;
    ImageView mImplantImageView;

    public EditImplantDetails(Implant implant)
    {
        _implant = implant;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_edit_implant_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {

    }
}