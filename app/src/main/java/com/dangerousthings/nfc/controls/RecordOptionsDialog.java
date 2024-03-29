package com.dangerousthings.nfc.controls;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.enums.OnClickActionType;
import com.dangerousthings.nfc.interfaces.IClickListener;
import com.dangerousthings.nfc.utilities.NdefUtils;

import java.util.Objects;

public class RecordOptionsDialog extends DialogFragment
{
    private static final String ARG_RECORD = "ndef_record";

    IClickListener _clickListener;
    NdefRecord _record;

    Button mEncryptButton;
    Button mDecryptButton;
    Button mLabelButton;
    Button mDeleteButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if(getArguments() != null)
        {
            _record = getArguments().getParcelable(ARG_RECORD);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_record_options, null);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        mEncryptButton = view.findViewById(R.id.record_options_button_encrypt);
        mDecryptButton = view.findViewById(R.id.record_options_button_decrypt);
        mLabelButton = view.findViewById(R.id.record_options_button_label);
        mDeleteButton = view.findViewById(R.id.record_options_button_delete);
        mDeleteButton.setOnClickListener(v ->
        {
            _clickListener.onClick(OnClickActionType.delete);
            Objects.requireNonNull(getDialog()).cancel();
        });
        mEncryptButton.setOnClickListener(v ->
        {
            _clickListener.onClick(OnClickActionType.prompt_encryption_password);
            Objects.requireNonNull(getDialog()).cancel();
        });
        mDecryptButton.setOnClickListener(v ->
        {
            _clickListener.onClick(OnClickActionType.prompt_decryption_password);
            Objects.requireNonNull(getDialog()).cancel();
        });
        mLabelButton.setOnClickListener(v ->
        {
            _clickListener.onClick(OnClickActionType.edit_label);
            Objects.requireNonNull(getDialog()).cancel();
        });

        if(NdefUtils.isRecordEncryptionLabelSupported(_record))
        {
            if(NdefUtils.isRecordEncrypted(_record))
            {
                mEncryptButton.setVisibility(View.GONE);
                mDecryptButton.setVisibility(View.VISIBLE);
            }
            else
            {
                mEncryptButton.setVisibility(View.VISIBLE);
                mDecryptButton.setVisibility(View.GONE);
            }
        }
        else
        {
            mEncryptButton.setVisibility(View.GONE);
            mDecryptButton.setVisibility(View.GONE);
            mLabelButton.setVisibility(View.GONE);
        }

        builder.setView(view);
        Dialog dialog = builder.create();
        Window window = dialog.getWindow();
        if(window != null)
        {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    public static RecordOptionsDialog newInstance(NdefRecord record)
    {
        RecordOptionsDialog dialog = new RecordOptionsDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECORD, record);
        dialog.setArguments(args);
        return dialog;
    }

    public void setClickListener(IClickListener listener)
    {
        _clickListener = listener;
    }
}
