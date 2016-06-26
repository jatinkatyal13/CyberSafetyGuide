package com.android.guide.safety.cybersafetyguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class change_pass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_change_pass);


        final Button button = (Button)findViewById(R.id.change);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("Loading...");
                button.setEnabled(false);
                button.setBackgroundColor(Color.parseColor("#FF8B8888"));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String email = getIntent().getStringExtra("email");
                        String pass = ((EditText)findViewById(R.id.pass)).getText().toString();
                        String pass2 = ((EditText)findViewById(R.id.pass2)).getText().toString();


                        if (!pass.equals(pass2)){
                            Toast.makeText(change_pass.this, "Password Incorrect!", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                InputStream in = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/changePass.php?code=securepass@csao2016&user="+email+"&pass="+pass).openConnection())).getInputStream();
                                String text = "";
                                int c;
                                while ((c = in.read()) != -1){
                                    text+=(char)c;
                                }
                                if (text.equals("Success")){
                                    InputStream get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/get_data.php?code=securepass@csao2016&user="+email+"&pass="+pass).openConnection())).getInputStream();
                                    String json = "";
                                    int a;
                                    while ((a = get.read()) != -1){
                                        json+=(char)a;
                                    }
                                    JSONObject object = new JSONObject(json);
                                    String n = object.getString("name");
                                    String e = object.getString("email");
                                    String ph = object.getString("phone");
                                    String p = object.getString("pass");
                                    OutputStream out = openFileOutput("secure.db", MODE_PRIVATE);
                                    out.write(("{\"name\":\""+n+"\", \"phone\":\""+ph+"\", \"email\": \""+e+"\", \"pass\":\""+p+"\"}").getBytes());
                                    Intent main = new Intent(change_pass.this, MainActivity.class);
                                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(main);
                                    change_pass.this.finish();
                                } else {
                                    final String t = text;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(change_pass.this, t, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (Exception e){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(change_pass.this, "Error Occured !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
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
