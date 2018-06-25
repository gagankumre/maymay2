package com.example.gagan.maymay_test1;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class Toolabar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void setContentView(int layoutResID)
    {
        // toolbar
        Toolbar toolbar1 = findViewById(R.id.toolbarnew);
        setSupportActionBar(toolbar1);
    }
    }