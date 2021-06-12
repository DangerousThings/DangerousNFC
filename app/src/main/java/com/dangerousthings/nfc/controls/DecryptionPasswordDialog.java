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

public class DecryptionPasswordDialog extends DialogFragment
{
    IClickListener _clickListener;

    EditText mPasswordEntry;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_decryption_password, null);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        mPasswordEntry = view.findViewById(R.id.decryption_password_edittext);
        Button mOKButton = view.findViewById(R.id.decryption_password_button_ok);
        Button mCancelButton = view.findViewById(R.id.decryption_password_button_cancel);
        mOKButton.setOnClickListener(v -> okButtonClicked());
        mCancelButton.setOnClickListener(v -> _clickListener.onClick(OnClickType.cancel));

        builder.setView(view);
        Dialog dialog = builder.create();
        Window window = dialog.getWindow();
        if(window != null)
        {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    private void okButtonClicked()
    {
        _clickListener.onClick(OnClickType.decrypt_record);
        Objects.requireNonNull(getDialog()).cancel();
    }

    public String getDecryptionPassword()
    {
        return mPasswordEntry.getText().toString();
    }

    public void setClickListener(IClickListener listener)
    {
        _clickListener = listener;
    }
}
