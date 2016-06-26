package com.android.guide.safety.cybersafetyguide;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;

public class report_abuse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_abuse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            InputStream in = openFileInput("secure.db");
            String json = "";
            int c;
            while ((c = in.read()) != -1){
                json += (char)c;
            }
            JSONObject object = new JSONObject(json);
            ((EditText) findViewById(R.id.name)).setText(object.getString("name"));
            ((EditText) findViewById(R.id.email)).setText(object.getString("email"));
            ((EditText) findViewById(R.id.phone)).setText(object.getString("phone"));
            ((EditText) findViewById(R.id.nature)).setText(getIntent().getStringExtra("nature"));

        } catch (Exception e){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.report_abuse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.send){
            String name = ((EditText)findViewById(R.id.name)).getText().toString();
            String email = ((EditText)findViewById(R.id.email)).getText().toString();
            String nature = ((EditText)findViewById(R.id.nature)).getText().toString();
            String phone = ((EditText)findViewById(R.id.phone)).getText().toString();
            String desc = ((EditText)findViewById(R.id.desc)).getText().toString();

            if (name.trim().equals("")){
                Toast.makeText(report_abuse.this, "Please fill your name", Toast.LENGTH_SHORT).show();
            } else if (email.trim().equals("") || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                Toast.makeText(report_abuse.this, "Please fill your E-Mail Address correctly", Toast.LENGTH_SHORT).show();
            } else if (phone.trim().equals("") || !(Patterns.PHONE.matcher(phone).matches())){
                Toast.makeText(report_abuse.this, "Please fill your Phone number correctly", Toast.LENGTH_SHORT).show();
            } else if (nature.trim().equals("")){
                Toast.makeText(report_abuse.this, "Please fill the nature of your problem", Toast.LENGTH_SHORT).show();
            } else if (desc.trim().equals("")){
                Toast.makeText(report_abuse.this, "Please Describe your Problem", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(report_abuse.this, "Sending...", Toast.LENGTH_SHORT).show();
                final String subject = "Expert Consult";
                String message = "";

                message += "<b>Name: </b>"+name+"\r\n";
                message += "<b>Email: </b>"+email+"\r\n";
                message += "<b>Phone: </b>"+phone+"\r\n";
                message += "<b>Nature Of Problem: </b>"+nature+"\r\n";
                message += "<b>Problem Explained: </b>"+desc+"\r\n";

                final String finalMessage = message;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GMailSender sender = new GMailSender("no.reply.csao@gmail.com", "securepass@csao2016");
                            sender.sendMail(subject, finalMessage, "no.reply.csao@gmail.com", "ask.csao@gmail.com");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(report_abuse.this, R.style.myDialog));
                                    builder.setTitle("Request Sent");
                                    builder.setMessage("Your query has been sent and our experts will get in contact with you within 48 hours");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            report_abuse.this.finish();
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });

                        } catch (Exception e){
                            Toast.makeText(report_abuse.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                            Log.w("jatin", e.toString());
                        }
                    }
                }).start();
            }
        }

        return true;
    }

}
