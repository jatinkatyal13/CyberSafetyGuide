package com.android.guide.safety.cybersafetyguide;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class workshop_fragment extends Fragment {

    static Context context;
    static LayoutInflater inflater;

    public workshop_fragment() {
        // Required empty public constructor
    }

    public void add_context(Context context){
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        final View rootView = inflater.inflate(R.layout.content_workshop, container, false);

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

        List<String> states = new ArrayList<>();
        states.add("Arunachal Pradesh");
        states.add("Andhra Pradesh");
        states.add("Assam");
        states.add("Bihar");
        states.add("Chandigarh");
        states.add("Delhi");
        states.add("Goa");
        states.add("Gujarat");
        states.add("Haryana");
        states.add("Himachal Pradesh");
        states.add("Jammu & Kashmir");
        states.add("Jharkhand");
        states.add("Karnataka");
        states.add("Kerala");
        states.add("Madhya Pradesh");
        states.add("Maharashtra");
        states.add("Manipur");
        states.add("Meghalaya");
        states.add("Mizoram");
        states.add("Nagaland");
        states.add("Odisha");
        states.add("Punjab");
        states.add("Rajasthan");
        states.add("Sikkim");
        states.add("Tamil Nadu");
        states.add("Telangana");
        states.add("Tripura");
        states.add("Uttar Pradesh");
        states.add("Uttarakhand");
        states.add("West Bengal");

        MyAdapter adapter = new MyAdapter(context, states);

        ((MaterialBetterSpinner)rootView.findViewById(R.id.state)).setAdapter(adapter);

        List<String> know = new ArrayList<>();
        know.add("Social Media");
        know.add("Print Media");
        know.add("Google");
        know.add("Friends");
        know.add("Other");

        adapter = new MyAdapter(context, know);
        MaterialBetterSpinner know_spinner = (MaterialBetterSpinner)rootView.findViewById(R.id.know);
        know_spinner.setAdapter(adapter);
        know_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4){
                    ((LinearLayout)rootView.findViewById(R.id.know_layout)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout)rootView.findViewById(R.id.know_layout)).setVisibility(View.GONE);
                }
            }
        });

        ((FloatingActionButton)rootView.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate content
                String name = ((EditText)rootView.findViewById(R.id.name)).getText().toString();
                String org = ((EditText)rootView.findViewById(R.id.org)).getText().toString();
                String email = ((EditText)rootView.findViewById(R.id.email)).getText().toString();
                String phone = ((EditText)rootView.findViewById(R.id.phone)).getText().toString();
                String state = ((MaterialBetterSpinner)rootView.findViewById(R.id.state)).getText().toString();
                String city = ((EditText)rootView.findViewById(R.id.city)).getText().toString();
                String know = ((MaterialBetterSpinner)rootView.findViewById(R.id.know)).getText().toString();
                String know_other = ((EditText)rootView.findViewById(R.id.know_other)).getText().toString();
                String query = ((EditText)rootView.findViewById(R.id.query)).getText().toString();

                if (name.trim().equals("")){
                    Toast.makeText(context, "Please Fill in your Name", Toast.LENGTH_SHORT).show();
                } else if (org.trim().equals("")){
                    Toast.makeText(context, "Please Fill in your Organisation, Institution or School name", Toast.LENGTH_SHORT).show();
                } else if (email.trim().equals("") || (!email.contains("@") || !(email.indexOf("@") == email.lastIndexOf("@")) )){
                    Toast.makeText(context, "Please Fill in your E-Mail Address Correctly", Toast.LENGTH_SHORT).show();
                } else if (phone.trim().equals("")) {
                    Toast.makeText(context, "Please Fill in your Contact Number", Toast.LENGTH_SHORT).show();
                } else if (!(phone.trim().length() == 10) && !(phone.trim().length() == 8)){
                    Toast.makeText(context, "Please Fill in valid phone number without STD code", Toast.LENGTH_SHORT).show();
                } else if (state.trim().equals("")){
                    Toast.makeText(context, "Please Select your State", Toast.LENGTH_SHORT).show();
                } else if (city.trim().equals("")){
                    Toast.makeText(context, "Please Fill in your City", Toast.LENGTH_SHORT).show();
                } else if (know.trim().equals("")){
                    Toast.makeText(context, "Please Select the valid option", Toast.LENGTH_SHORT).show();
                } else if (know.equals("Other") && know_other.trim().equals("")){
                    Toast.makeText(context, "Please Specify", Toast.LENGTH_SHORT).show();
                } else {
                    // send form
                    Toast.makeText(context, "Sending...", Toast.LENGTH_SHORT).show();

                    final String subject = "Workshop Request";
                    String message = "";

                    message += "<b>Name: </b>"+name+"\r\n";
                    message += "<b>Organisation/Insitution/School:</b> "+org+"\r\n";
                    message += "<b>Email: </b>"+email+"\r\n";
                    message += "<b>Phone: </b>"+phone+"\r\n";
                    message += "<b>State: </b>"+state+"\r\n";
                    message += "<b>City: </b>"+city+"\r\n";
                    message += "<b>Where got to know: </b>"+know+"\r\n";
                    message += "<b>Other: </b>"+know_other+"\r\n";
                    message += "<b>Query: </b>"+query;

                    final String finalMessage = message;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                GMailSender sender = new GMailSender("no.reply.csao@gmail.com", "securepass@csao2016");
                                sender.sendMail(subject, finalMessage, "no.reply.csao@gmail.com", "workshop.csao@gmail.com");


                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
                                        builder.setTitle("Request Sent");
                                        builder.setMessage("Thanks for requesting a workshop our representative will call you shortly");
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(context, MainActivity.class);
                                                startActivity(intent);
                                                ((Activity)context).finish();
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                });

                            } catch (Exception e){
                                final Exception ex = e;
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Error Occured while Sending your Request", Toast.LENGTH_SHORT).show();
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

    private class MyAdapter extends ArrayAdapter<String> {

        private List<String> list;

        public MyAdapter(Context context, List<String> list) {
            super(context, R.layout.states_list, list);
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = inflater.inflate(R.layout.states_list, parent, false);

            ((TextView)rootView.findViewById(R.id.text)).setText(list.get(position));

            return rootView;
        }
    }

}
