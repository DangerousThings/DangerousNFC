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
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.enums.OnClickType;
import com.dangerousthings.nfc.fragments.EditMarkdownFragment;
import com.dangerousthings.nfc.interfaces.IClickListener;

import java.util.Objects;

public class DecryptionPasswordDialog extends DialogFragment
{
    private static final String ARG_VIEW_DECRYPTION = "view_decryption";

    IClickListener _clickListener;
    boolean _showDecryption;

    EditText mPasswordEntry;

    public static DecryptionPasswordDialog newInstance(boolean showDecryption)
    {
        DecryptionPasswordDialog fragment = new DecryptionPasswordDialog();
        Bundle args = new Bundle();
        args.putBoolean(ARG_VIEW_DECRYPTION, showDecryption);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_decryption_password, null);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        if (getArguments() != null)
        {
            _showDecryption = getArguments().getBoolean(ARG_VIEW_DECRYPTION);
        }

        mPasswordEntry = view.findViewById(R.id.decryption_password_edittext);
        Button mOKButton = view.findViewById(R.id.decryption_password_button_ok);
        Button mCancelButton = view.findViewById(R.id.decryption_password_button_cancel);
        mOKButton.setOnClickListener(v -> okButtonClicked());
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

    private void okButtonClicked()
    {
        if(_showDecryption)
        {
            _clickListener.onClick(OnClickType.decrypt_and_view);
        }
        else
        {
            _clickListener.onClick(OnClickType.decrypt_record);
        }
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
