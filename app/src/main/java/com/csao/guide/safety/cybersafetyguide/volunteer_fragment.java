package com.android.guide.safety.cybersafetyguide;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;

public class volunteer_fragment extends Fragment {

    static Context context;

    public volunteer_fragment() {
        // Required empty public constructor
    }

    public void add_context(Context context){
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.content_volunteer, container, false);

        try {
            InputStream in = context.openFileInput("secure.db");
            int c;
            String json = "";
            while ((c = in.read()) != -1){
                json += (char)c;
            }
            JSONObject object = new JSONObject(json);
            ((TextView)rootView.findViewById(R.id.name)).setText(object.getString("name"));
            ((TextView)rootView.findViewById(R.id.email)).setText(object.getString("email"));
            ((TextView)rootView.findViewById(R.id.phone)).setText(object.getString("phone"));

        } catch (Exception e){

        }

        FloatingActionButton button = (FloatingActionButton)rootView.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText)rootView.findViewById(R.id.name)).getText().toString();
                String email = ((EditText)rootView.findViewById(R.id.email)).getText().toString();
                String phone = ((EditText)rootView.findViewById(R.id.phone)).getText().toString();
                String mess = ((EditText)rootView.findViewById(R.id.message)).getText().toString();

                if (name.trim().equals("")){
                    Toast.makeText(context, "Please fill in your Name", Toast.LENGTH_SHORT).show();
                } else if (email.trim().equals("") || (!email.contains("@") || !(email.indexOf("@") == email.lastIndexOf("@")) )){
                    Toast.makeText(context, "Please fill in your EMail address correctly", Toast.LENGTH_SHORT).show();
                } else if (phone.trim().equals("")){
                    Toast.makeText(context, "Please fill in your Phone Number", Toast.LENGTH_SHORT).show();
                } else if (!(phone.trim().length() == 10) && !(phone.trim().length() == 8)){
                    Toast.makeText(context, "Please fill in your Phone Number Correctly without STD code", Toast.LENGTH_SHORT).show();
                } else if (mess.trim().equals("")){
                    Toast.makeText(context, "Please write a message for us too !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Sending...", Toast.LENGTH_SHORT).show();
                    final String subject = "Volunteer request";
                    String message = "";

                    message += "<b>Name: </b>" + name + "\r\n";
                    message += "<b>Phone: </b>" + phone + "\r\n";
                    message += "<b>EMail: </b>" + email + "\r\n";
                    message += "<b>Message: </b>" + mess;

                    final String finalMessage = message;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{

                                GMailSender sender = new GMailSender("no.reply.csao@gmail.com","securepass@csao2016");
                                sender.sendMail(subject, finalMessage, "no.reply.csao@gmail.com", "volunteer.csao@gmail.com");

                                new Handler(context.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
                                        builder.setTitle("Request Sent");
                                        builder.setMessage("Thanks for showing interest with our Team. We will contact you soon !");
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                });
                            } catch (Exception e){
                                new Handler(context.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Error Occured !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        return rootView;
    }

}
