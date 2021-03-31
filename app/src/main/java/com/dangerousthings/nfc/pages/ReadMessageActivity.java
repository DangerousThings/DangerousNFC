package com.dangerousthings.nfc.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dangerousthings.nfc.R;

public class ReadMessageActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);
    }
}