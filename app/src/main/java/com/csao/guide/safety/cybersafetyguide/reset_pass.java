package com.android.guide.safety.cybersafetyguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class reset_pass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_reset_pass);

        final Button button = (Button)findViewById(R.id.otp_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("Sending...");
                button.setEnabled(false);
                button.setBackgroundColor(Color.parseColor("#FF8B8888"));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String email = ((EditText)findViewById(R.id.email)).getText().toString();
                        try {
                            InputStream res = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/isEmailOk.php?email="+email).openConnection())).getInputStream();
                            String result = "";
                            int a;
                            while ((a = res.read()) != -1){
                                result += (char)a;
                            }
                            if (!email.equals("") && Patterns.EMAIL_ADDRESS.matcher(email).matches() || result.equals("false")){
                                try {
                                    String message = "";
                                    String code = "";
                                    URL url = new URL("http://www.abboniss.com/test/get_otp.php");
                                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                                    InputStream in = connection.getInputStream();
                                    int c;
                                    while ((c = in.read()) != -1){
                                        code += (char)c;
                                    }
                                    message += "Dear User,\r\nYour One Time Password for Login to CSAO (A Cyber Guide) is "+code+". This app will provide information about major cyber threats, security solutions, helpline numbers and free experts consultation. \r\n\n\n Disclaimer: This app is to provide information about cyber security and we are not responsible for any further communication between expert and end user.";

                                    GMailSender sender = new GMailSender("no.reply.csao@gmail.com", "securepass@csao2016");
                                    sender.sendMail("CSAO - OTP for verification", message, "no.reply.csao@gmail.com", email);

                                    Intent intent = new Intent (reset_pass.this, reset_pass_2.class);
                                    intent.putExtra("OTP", code);
                                    intent.putExtra("email", email);
                                    startActivity(intent);
                                    reset_pass.this.finish();
                                } catch (Exception e){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(reset_pass.this, "Error Occured !", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(reset_pass.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(reset_pass.this, "Connection Error!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setText("Send OTP");
                                button.setEnabled(true);
                                button.setBackgroundColor(Color.parseColor("#3fb568"));
                            }
                        });
                    }
                }).start();
            }
        });
    }


}
