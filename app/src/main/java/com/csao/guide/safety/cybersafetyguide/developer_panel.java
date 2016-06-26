package com.android.guide.safety.cybersafetyguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class developer_panel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_developer_panel);
        getSupportActionBar().setTitle("Developers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
