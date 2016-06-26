package com.csao.guide.safety.cybersafetyguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((TextView)findViewById(R.id.forgot)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, reset_pass.class);
                startActivity(intent);
            }
        });

        final Button button = (Button)findViewById(R.id.login);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setText("Signing In...");
                button.setEnabled(false);
                button.setBackgroundColor(Color.parseColor("#FF8B8888"));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String email = ((EditText)findViewById(R.id.email)).getText().toString();
                        String pass = ((EditText)findViewById(R.id.pass)).getText().toString();
                        try {
                            URL url = new URL("http://www.abboniss.com/test/isAuthentic.php?code=securepass@csao2016&user="+email+"&pass="+pass);
                            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                            InputStream in = connection.getInputStream();
                            String reply = "";
                            int c;
                            while ((c = in.read()) != -1){
                                reply += (char)c;
                            }
                            if (reply.equals("true")){
                                URL u = new URL("http://www.abboniss.com/test/get_data.php?code=securepass@csao2016&user="+email+"&pass="+pass);
                                HttpURLConnection co = (HttpURLConnection)u.openConnection();
                                InputStream i = co.getInputStream();
                                String result = "";
                                int a;
                                while ((a = i.read()) != -1){
                                    result += (char)a;
                                }
                                try {
                                    JSONObject object = new JSONObject(result);
                                    String name = object.getString("name");
                                    String phone = object.getString("phone");
                                    String e = object.getString("email");
                                    String p = object.getString("pass");
                                    OutputStream out = openFileOutput("secure.db", MODE_PRIVATE);
                                    out.write(result.getBytes());
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", e);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    final String res = result;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(login.this, res, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(login.this, "Wrong Email ID or Password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(login.this, "Unable to Connect !", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setText("Sign In");
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
