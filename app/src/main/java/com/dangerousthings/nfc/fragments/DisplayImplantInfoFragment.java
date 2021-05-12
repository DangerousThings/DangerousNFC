package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.models.Implant;

public class DisplayImplantInfoFragment extends Fragment
{
    private static final String ARG_IMPLANT_UID = "implant_uid";

    TextView mNameText;
    TextView mUIDText;
    TextView mFamilyText;
    TextView mModelText;
    ImageButton mBackButton;

    private Implant _implant;

    public DisplayImplantInfoFragment()
    {
    }

    public static DisplayImplantInfoFragment newInstance(String implantUID)
    {
        DisplayImplantInfoFragment fragment = new DisplayImplantInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMPLANT_UID, implantUID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            ImplantDatabase database = ImplantDatabase.getInstance(requireActivity());
            IImplantDAO implantDAO = database.implantDAO();
            _implant = implantDAO.getImplantByUID(getArguments().getString(ARG_IMPLANT_UID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_display_implant_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mNameText = view.findViewById(R.id.display_implant_text_name);
        mUIDText = view.findViewById(R.id.display_implant_text_uid);
        mFamilyText = view.findViewById(R.id.display_implant_text_family);
        mModelText = view.findViewById(R.id.display_implant_text_model);
        mBackButton = view.findViewById(R.id.dislpay_implant_button_back);

        mNameText.setText(_implant.getImplantName());
        mUIDText.setText(_implant.getUID());
        mFamilyText.setText(_implant.getTagFamily().toString());
        mModelText.setText(_implant.getImplantModelAsString());
        mBackButton.setOnClickListener(v -> requireActivity().onBackPressed());
    }
}