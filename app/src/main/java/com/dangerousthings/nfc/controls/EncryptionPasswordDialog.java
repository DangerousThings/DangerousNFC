package com.dangerousthings.nfc.controls;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.enums.OnClickType;
import com.dangerousthings.nfc.interfaces.IClickListener;

import java.util.Objects;

public class EncryptionPasswordDialog extends DialogFragment
{
    IClickListener _clickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_encryption_password, null);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        EditText mPasswordEntry = view.findViewById(R.id.encryption_password_edittext);
        Button mOKButton = view.findViewById(R.id.encryption_password_button_ok);
        Button mCancelButton = view.findViewById(R.id.encryption_password_button_cancel);
        mOKButton.setOnClickListener(v -> _clickListener.onClick(OnClickType.encrypt_record));
        mCancelButton.setOnClickListener(v -> Objects.requireNonNull(getDialog()).cancel());

        builder.setView(view);
        Dialog dialog = builder.create();
        Window window = dialog.getWindow();
        if(window != null)
        {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    public void setClickListener(IClickListener listener)
    {
        _clickListener = listener;
    }
}
