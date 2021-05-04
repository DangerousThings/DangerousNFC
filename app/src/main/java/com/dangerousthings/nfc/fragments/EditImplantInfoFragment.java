package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.models.Implant;

public class EditImplantInfoFragment extends Fragment
{

    private static final String ARG_IMPLANT_UID = "implant_uid";

    ImageButton mBackButton;
    ImageButton mSaveButton;
    TextView mUIDText;
    EditText mNameEdit;

    private Implant _implant;

    public EditImplantInfoFragment()
    {
    }

    public static EditImplantInfoFragment newInstance(String implantUID)
    {
        EditImplantInfoFragment fragment = new EditImplantInfoFragment();
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
        return inflater.inflate(R.layout.fragment_edit_implant_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mBackButton = view.findViewById(R.id.edit_implant_button_back);
        mSaveButton = view.findViewById(R.id.edit_implant_button_save);
        mUIDText = view.findViewById(R.id.edit_implant_text_uid);
        mNameEdit = view.findViewById(R.id.edit_implant_edittext_name);

        mBackButton.setOnClickListener(v -> requireActivity().onBackPressed());
        mSaveButton.setOnClickListener(v -> updateImplant());

        mUIDText.setText(_implant.getUID());
    }

    private void updateImplant()
    {
        _implant.setImplantName(mNameEdit.getText().toString());
        //TODO: pull in fragment's values into implant
        ImplantDatabase database = ImplantDatabase.getInstance(requireActivity());
        IImplantDAO implantDAO = database.implantDAO();
        implantDAO.updateImplant(_implant);
    }
}