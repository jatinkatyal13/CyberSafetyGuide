package com.android.guide.safety.cybersafetyguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class reset_pass_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_reset_pass_2);

        Button button = (Button)findViewById(R.id.verify);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP = getIntent().getStringExtra("OTP");
                if (((EditText)findViewById(R.id.otp)).getText().toString().equals(OTP)){
                    //verified
                    Intent intent = new Intent(reset_pass_2.this, change_pass.class);
                    intent.putExtra("email", getIntent().getStringExtra("email"));
                    startActivity(intent);
                    reset_pass_2.this.finish();
                } else {
                    Toast.makeText(reset_pass_2.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(reset_pass_2.this, reset_pass.class);
                    startActivity(intent);
                    reset_pass_2.this.finish();
                }
            }
        });
    }
}
