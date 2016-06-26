package com.android.guide.safety.cybersafetyguide;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_registration);

        Button button = (Button)findViewById(R.id.register);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String name = ((EditText)findViewById(R.id.name)).getText().toString();
            String phone = ((EditText)findViewById(R.id.phone)).getText().toString();
            String email = getIntent().getStringExtra("email");
            String pass = ((EditText)findViewById(R.id.pass)).getText().toString();
            String pass2 = ((EditText)findViewById(R.id.pass2)).getText().toString();


            if (!pass.equals(pass2)) {
                Toast.makeText(registration.this, "Password Incorrect!", Toast.LENGTH_SHORT).show();
            } else if (name.trim().equals("")){
                Toast.makeText(registration.this, "Please Enter your name", Toast.LENGTH_SHORT).show();
            } else if (phone.trim().equals("") || !(Patterns.PHONE.matcher(phone).matches())){
                Toast.makeText(registration.this, "Please Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
            } else if (email.trim().equals("") || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
                Toast.makeText(registration.this, "Please Enter a valid E-Mail Address", Toast.LENGTH_SHORT).show();
            } else if (pass.trim().equals("")){
                Toast.makeText(registration.this, "Please Enter your Desired Password", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    OutputStream out = openFileOutput("secure.db", MODE_PRIVATE);
                    out.write(("{\"name\":\""+name+"\", \"phone\":\""+phone+"\", \"email\": \""+email+"\", \"pass\":\""+pass+"\"}").getBytes());

                    // Make a POST request.

                    URL url = new URL("http://www.abboniss.com/test/sign_up.php?code=securepass@csao2016&user="+email+"&pass="+pass+"&name="+name+"&phone="+phone);
                    HttpURLConnection connection = null;
                    connection = (HttpURLConnection) url.openConnection();

                    InputStream res = connection.getInputStream();
                    String result = "";
                    int c;
                    while ((c = res.read()) != -1){
                        result += (char)c;
                    }
                    if (result.equals("Success")){
                        Intent intent = new Intent(registration.this, MainActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(registration.this, result, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    Toast.makeText(registration.this, "Error Occured !", Toast.LENGTH_SHORT).show();
                    Toast.makeText(registration.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            }
        });

    }
}
