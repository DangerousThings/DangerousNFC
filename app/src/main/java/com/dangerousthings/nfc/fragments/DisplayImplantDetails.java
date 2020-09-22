package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.models.Implant;

public class DisplayImplantDetails extends Fragment
{
    Implant _implant;

    TextView mImplantNameTextView;
    TextView mImplantTypeTextView;
    TextView mImplantUidTextView;
    ImageView mImplantImageView;

    public DisplayImplantDetails(Implant implant)
    {
        _implant = implant;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_display_implant_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mImplantNameTextView = getView().findViewById(R.id.textview_recycler_implant_name);
        mImplantTypeTextView = getView().findViewById(R.id.textview_recycler_implant_type);
        mImplantImageView = getView().findViewById(R.id.imageview_recycler_implant);
        mImplantUidTextView = getView().findViewById(R.id.textview_uid);

        mImplantNameTextView.setText(_implant.getImplantName());
        mImplantTypeTextView.setText(_implant.getImplantType());
        mImplantUidTextView.setText(_implant.getUid());
        mImplantImageView.setImageResource(_implant.getImplantImage());
    }
}