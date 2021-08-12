package com.dangerousthings.nfc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.databases.ImplantDatabase;
import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.models.Implant;
import com.dangerousthings.nfc.pages.ManageImplantActivity;
import com.dangerousthings.nfc.utilities.ColorUtils;
import com.dangerousthings.nfc.utilities.Converters;

public class EditImplantInfoFragment extends Fragment
{

    private static final String ARG_IMPLANT_UID = "implant_uid";

    ImageButton mBackButton;
    ImageButton mSaveButton;
    TextView mUIDText;
    TextView mFamilyText;
    EditText mNameEdit;
    Spinner mModelSpinner;

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
        mFamilyText = view.findViewById(R.id.edit_implant_text_family);

        mBackButton.setOnClickListener(v -> requireActivity().onBackPressed());
        mSaveButton.setOnClickListener(v -> saveImplant());

        mUIDText.setText(_implant.getUID());
        mFamilyText.setText(_implant.getTagFamily().toString());

        mModelSpinner = view.findViewById(R.id.edit_implant_spinner_model);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, _implant.getImplantModelListAsString());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mModelSpinner.setAdapter(adapter);
        mModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                ((TextView) adapterView.getChildAt(0)).setTextColor(ColorUtils.getPrimaryColor(requireActivity()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void saveImplant()
    {
        //Pull values from page into implant and update
        _implant.setImplantName(mNameEdit.getText().toString());
        if(mModelSpinner.getSelectedItem() != null)
        {
            _implant.setImplantModel(Converters.getImplantModelFromString(mModelSpinner.getSelectedItem().toString().replace(" ", "_")));
        }
        ImplantDatabase database = ImplantDatabase.getInstance(requireActivity());
        IImplantDAO implantDAO = database.implantDAO();
        implantDAO.updateImplant(_implant);
        ((ManageImplantActivity)requireActivity()).switchToDisplayFragment(_implant.getUID());
    }
}