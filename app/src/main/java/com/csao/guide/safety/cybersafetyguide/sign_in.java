package com.android.guide.safety.cybersafetyguide;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import android.content.pm.Signature;

import org.json.JSONException;

public class sign_in extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_sign_in);

        Button button = (Button)findViewById(R.id.otp_button);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Button button = (Button) findViewById(R.id.otp_button);
                button.setText("Sending...");
                button.setEnabled(false);
                button.setBackgroundColor(Color.parseColor("#FF8B8888"));
                final String email = ((EditText)findViewById(R.id.email)).getText().toString();
                if (!email.equals("") && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                InputStream in = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/isEmailOk.php?email="+email).openConnection())).getInputStream();
                                String result = "";
                                int c;
                                while ((c = in.read()) != -1){
                                    result += (char)c;
                                }
                                if (result.equals("false")){
                                    try {
                                        String message = "";
                                        String code = "";
                                        URL url = new URL("http://www.abboniss.com/test/get_otp.php");
                                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                                        InputStream input = connection.getInputStream();
                                        while ((c = input.read()) != -1){
                                            code += (char)c;
                                        }
                                        message += "Dear User,\r\nYour One Time Password for Login to CSAO (A Cyber Guide) is "+code+". This app will provide information about major cyber threats, security solutions, helpline numbers and free experts consultation.\r\n  Disclaimer: This app is to provide information about cyber security and we are not responsible for any further communication between expert and end user.";

                                        GMailSender sender = new GMailSender("no.reply.csao@gmail.com", "securepass@csao2016");
                                        sender.sendMail("CSAO - OTP for verification", message, "no.reply.csao@gmail.com", email);

                                        Intent intent = new Intent (sign_in.this, sign_in_2.class);
                                        intent.putExtra("OTP", code);
                                        intent.putExtra("email", email);
                                        startActivity(intent);
                                    } catch (IOException e){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(sign_in.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } catch (Exception e){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(sign_in.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(sign_in.this, "Email Address Already Exists", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (MalformedURLException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(sign_in.this, "Unknown Error Occured!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(sign_in.this, "Connection Error", Toast.LENGTH_SHORT).show();
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
                } else {
                    Toast.makeText(sign_in.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    button.setText("Send OTP");
                    button.setEnabled(true);
                    button.setBackgroundColor(Color.parseColor("#3fb568"));
                }
            }
        });
    }


}
