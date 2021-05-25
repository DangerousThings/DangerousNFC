package com.dangerousthings.nfc.fragments;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.interfaces.IEditFragment;
import com.dangerousthings.nfc.interfaces.ITracksPayloadSize;
import com.dangerousthings.nfc.utilities.NdefUtils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;

public class EditMarkdownFragment extends Fragment implements IEditFragment
{
    private static final String ARG_RECORD = "record";

    EditText mEditText;

    LinearLayout mLinearLayout;

    ImageButton mPreviewButton;
    ImageButton mBoldButton;
    ImageButton mItalicizeButton;
    ImageButton mCodeButton;
    ImageButton mLinkButton;
    ImageButton mStrikethroughButton;
    ImageButton mBulletButton;
    ImageButton mQuoteButton;
    ImageButton mThematicBreakButton;

    NdefRecord _record;
    ITracksPayloadSize _tracker;

    public EditMarkdownFragment()
    {
    }

    public static EditMarkdownFragment newInstance(NdefRecord record)
    {
        EditMarkdownFragment fragment = new EditMarkdownFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECORD, record);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditMarkdownFragment newInstance()
    {
        return new EditMarkdownFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            _record = getArguments().getParcelable(ARG_RECORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_edit_markdown, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        mEditText = view.findViewById(R.id.edit_markdown_edittext);

        mLinearLayout = view.findViewById(R.id.edit_markdown_linear);

        mPreviewButton = view.findViewById(R.id.edit_markdown_button_preview);
        mBoldButton = view.findViewById(R.id.edit_markdown_button_bold);
        mItalicizeButton = view.findViewById(R.id.edit_markdown_button_italicize);
        mCodeButton = view.findViewById(R.id.edit_markdown_button_code);
        mLinkButton = view.findViewById(R.id.edit_markdown_button_link);
        mStrikethroughButton = view.findViewById(R.id.edit_markdown_button_strikethrough);
        mBulletButton = view.findViewById(R.id.edit_markdown_button_bullet);
        mQuoteButton = view.findViewById(R.id.edit_markdown_button_quote);
        mThematicBreakButton = view.findViewById(R.id.edit_markdown_button_break);

        mBoldButton.setOnClickListener(v -> insertTextAroundSelection("**", "**"));
        mItalicizeButton.setOnClickListener(v -> insertTextAroundSelection("_", "_"));
        mCodeButton.setOnClickListener(v -> insertTextAroundSelection("`", "`"));
        mLinkButton.setOnClickListener(v ->
        {
            int selectionStart = mEditText.getSelectionStart();
            int selectionEnd = mEditText.getSelectionEnd();

            if(selectionStart == selectionEnd)
            {
                insertTextAroundSelection("[", "]()");
            }
            else
            {
                String urlRegex = "(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?";
                if(isRegexMatch(mEditText.getText().toString().substring(selectionStart, selectionEnd), urlRegex))
                {
                    insertTextAroundSelection("[](", ")");
                }
                else
                {
                    insertTextAroundSelection("[", "]()");
                }
            }
        });
        mStrikethroughButton.setOnClickListener(v -> insertTextAroundSelection("~~", "~~"));
        mBulletButton.setOnClickListener(v -> insertTextAroundSelection("- ", ""));
        mQuoteButton.setOnClickListener(v -> insertTextAroundSelection("> ", ""));
        mThematicBreakButton.setOnClickListener(v -> insertTextAroundSelection("***\n", ""));
        mLinearLayout.setOnClickListener(v -> focusEntry());

        if(_record != null)
        {
            mEditText.setText(NdefUtils.getStringFromBytes(_record.getPayload()));
            _tracker.payloadChanged();
        }

        final Markwon markwon = Markwon.create(requireActivity());
        final MarkwonEditor editor = MarkwonEditor.create(markwon);
        mEditText.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));
        setupTextChangedEvent();
    }

    private boolean isRegexMatch(String text, String regex)
    {
        try
        {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            return matcher.matches();
        }
        catch(RuntimeException e)
        {
            return false;
        }
    }

    private void insertTextAroundSelection(String textLeft, String textRight)
    {
        int selectionStart = mEditText.getSelectionStart();
        int selectionEnd = mEditText.getSelectionEnd();

        mEditText.getText().insert(selectionStart, textLeft);
        mEditText.getText().insert(selectionEnd+textLeft.length(), textRight);

        Selection.setSelection(mEditText.getText(), selectionEnd+textLeft.length());
    }

    private void focusEntry()
    {
        mEditText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
        int position = mEditText.length();
        Editable editable = mEditText.getText();
        Selection.setSelection(editable, position);
    }

    public void setupTextChangedEvent()
    {
        mEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                _tracker.payloadChanged();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    public NdefRecord getNdefRecord()
    {
        return NdefRecord.createMime("text/markdown", mEditText.getText().toString().getBytes());
    }

    @Override
    public int getPayloadSize()
    {
        if(mEditText.getText() == null)
        {
            return 0;
        }
        else
        {
            return new NdefMessage(getNdefRecord()).getByteArrayLength();
        }
    }

    @Override
    public void setPayloadTrackingInterface(ITracksPayloadSize tracker)
    {
        _tracker = tracker;
    }
}