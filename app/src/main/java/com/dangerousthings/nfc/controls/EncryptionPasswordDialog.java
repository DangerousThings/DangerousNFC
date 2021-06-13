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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.enums.OnClickType;
import com.dangerousthings.nfc.interfaces.IClickListener;

import java.util.Objects;

public class EncryptionPasswordDialog extends DialogFragment
{
    IClickListener _clickListener;

    EditText mPasswordEntry;
    EditText mPasswordConfirmEntry;
    TextView mPasswordMismatchText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_encryption_password, null);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        mPasswordEntry = view.findViewById(R.id.encryption_password_edittext);
        mPasswordConfirmEntry = view.findViewById(R.id.encryption_password_edittext_confirm);
        mPasswordMismatchText = view.findViewById(R.id.encryption_password_text_password_mismatch);
        Button mOKButton = view.findViewById(R.id.encryption_password_button_ok);
        Button mCancelButton = view.findViewById(R.id.encryption_password_button_cancel);
        mOKButton.setOnClickListener(v -> checkPasswords());
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

    private void checkPasswords()
    {
        if(!(mPasswordEntry.getText().toString().equals(mPasswordConfirmEntry.getText().toString())))
        {
            mPasswordMismatchText.setVisibility(View.VISIBLE);
        }
        else
        {
            _clickListener.onClick(OnClickType.encrypt_record);
            Objects.requireNonNull(getDialog()).cancel();
        }
    }

    public String getEncryptionPassword()
    {
        return mPasswordEntry.getText().toString();
    }

    public void setClickListener(IClickListener listener)
    {
        _clickListener = listener;
    }
}
