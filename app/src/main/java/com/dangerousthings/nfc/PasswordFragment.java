package com.dangerousthings.nfc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.charset.Charset;
import java.util.Arrays;

public class PasswordFragment extends DialogFragment {
    public static PasswordFragment newInstance(byte[] preset_bytes) {
        PasswordFragment fragment = new PasswordFragment();

        String preset_string;
        if (preset_bytes == null) preset_string = "";
        else preset_string = new String(preset_bytes, Charset.forName("UTF-8"));

        Bundle bundle = new Bundle();
        bundle.putString("preset_password", preset_string);
        fragment.setArguments(bundle);

        return fragment;
    }

    public interface OnPasswordListener {
        public void onPasswordInput(byte[] password);
    }

    class BytesLengthFilter implements InputFilter {
        Charset mCharset;
        int mBytesLengthLimit;

        BytesLengthFilter(Charset charset, int bytesLengthLimit) {
            mCharset = charset;
            mBytesLengthLimit = bytesLengthLimit;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int length = dest.toString().getBytes(mCharset).length + source.toString().getBytes(mCharset).length;
            return (length > mBytesLengthLimit) ? "" : null;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.password, null);

        final EditText password = (EditText) view.findViewById(R.id.password);
        password.setFilters(new InputFilter[]{ new BytesLengthFilter(Charset.forName("UTF-8"), 4) });

        String preset_password = getArguments().getString("preset_password");
        password.setText(preset_password, TextView.BufferType.EDITABLE);
        password.setSelection(password.getText().length());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
               .setMessage("Password?")
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                   }
               }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       byte[] password_bytes;
                       String password_string = password.getText().toString();
                       if (password_string.length() == 0) password_bytes = null;
                       else password_bytes = password_string.getBytes(Charset.forName("UTF-8"));

                       ((OnPasswordListener) getActivity()).onPasswordInput(password_bytes);

                       dialog.dismiss();
                   }
               });
        return builder.create();
    }
}
