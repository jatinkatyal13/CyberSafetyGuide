package com.android.guide.safety.cybersafetyguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class threat_display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threat_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        getSupportActionBar().setTitle(intent.getStringExtra("heading"));
        ((TextView)findViewById(R.id.desc)).setText(intent.getStringExtra("description"));
        ((TextView)findViewById(R.id.threats)).setText(intent.getStringExtra("threats"));
    }

}
