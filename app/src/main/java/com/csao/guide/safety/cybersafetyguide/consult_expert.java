package com.android.guide.safety.cybersafetyguide;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
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

public class consult_expert extends AppCompatActivity {

    Boolean sending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_expert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            InputStream in = openFileInput("secure.db");
            int c;
            String json = "";
            while ((c = in.read()) != -1){
                json += (char)c;
            }
            JSONObject object = new JSONObject(json);
            ((TextView)findViewById(R.id.name)).setText(object.getString("name"));
            ((TextView)findViewById(R.id.email)).setText(object.getString("email"));
            ((TextView)findViewById(R.id.number)).setText(object.getString("phone"));

        } catch (Exception e){

        }


        List<String> know = new ArrayList<>();
        know.add("Social Media");
        know.add("Print Media");
        know.add("Google");
        know.add("Friends");
        know.add("Other");

        MyAdapter adapter = new MyAdapter(getApplicationContext(), know);
        MaterialBetterSpinner know_spinner = (MaterialBetterSpinner)findViewById(R.id.know);
        know_spinner.setAdapter(adapter);
        know_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4){
                    ((LinearLayout)findViewById(R.id.know_layout)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout)findViewById(R.id.know_layout)).setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.consult_expert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.send) {
            String name = ((EditText)findViewById(R.id.name)).getText().toString();
            String number = ((EditText)findViewById(R.id.number)).getText().toString();
            String email = ((EditText)findViewById(R.id.email)).getText().toString();
            String occupation = ((EditText)findViewById(R.id.occupation)).getText().toString();
            String nature = ((EditText)findViewById(R.id.nature)).getText().toString();
            String explain = ((EditText)findViewById(R.id.explain)).getText().toString();
            String know = ((MaterialBetterSpinner)findViewById(R.id.know)).getText().toString();
            String know_other = ((EditText)findViewById(R.id.know_other)).getText().toString();

            if (name.trim().equals("")){
                Toast.makeText(consult_expert.this, "Please Fill in your Name", Toast.LENGTH_SHORT).show();
            } else if (number.trim().equals("")){
                Toast.makeText(consult_expert.this, "Please Fill in your Contact Number", Toast.LENGTH_SHORT).show();
            } else if (email.trim().equals("") || (!email.contains("@") || !(email.indexOf("@") == email.lastIndexOf("@")) )){
                Toast.makeText(consult_expert.this, "Please Fill in your EMail Address Correctly", Toast.LENGTH_SHORT).show();
            } else if (occupation.trim().equals("")){
                Toast.makeText(consult_expert.this, "Please Fill in your Occupation", Toast.LENGTH_SHORT).show();
            } else if (!(number.trim().length() == 10) && !(number.trim().length() == 8)){
                Toast.makeText(consult_expert.this, "Please Fill in your Contact Number correctly without STD code", Toast.LENGTH_SHORT).show();
            } else if (nature.trim().equals("")){
                Toast.makeText(consult_expert.this, "Please Tell the Nature of the Problem", Toast.LENGTH_SHORT).show();
            } else if (explain.trim().equals("")){
                Toast.makeText(consult_expert.this, "Please Explain your problem", Toast.LENGTH_SHORT).show();
            } else if (know.equals("")){
                Toast.makeText(consult_expert.this, "Please Select Valid Option", Toast.LENGTH_SHORT).show();
            } else if (know.equals("Other") && know_other.trim().equals("")){
                Toast.makeText(consult_expert.this, "Please Specify", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(consult_expert.this, "Sending...", Toast.LENGTH_SHORT).show();
                sending = true;
                final String subject = "Expert Consult";
                String message = "";

                message += "<b>Name: </b>"+name+"\r\n";
                message += "<b>Email: </b>"+email+"\r\n";
                message += "<b>Phone: </b>"+number+"\r\n";
                message += "<b>Nature Of Problem: </b>"+nature+"\r\n";
                message += "<b>Problem Explained: </b>"+explain+"\r\n";
                message += "<b>Where got to know: </b>"+know+"\r\n";
                message += "<b>Other: </b>"+know_other;

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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(consult_expert.this, R.style.myDialog));
                                    builder.setTitle("Request Sent");
                                    builder.setMessage("Your query has been sent and our experts will get in contact with you within 48 hours");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            consult_expert.this.finish();
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });

                        } catch (Exception e){
                            Toast.makeText(consult_expert.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                            Log.w("jatin", e.toString());
                        }
                    }
                }).start();
                sending = false;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends ArrayAdapter<String> {

        private List<String> list;

        public MyAdapter(Context context, List<String> list) {
            super(context, R.layout.states_list, list);
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = getLayoutInflater().inflate(R.layout.states_list, parent, false);

            ((TextView)rootView.findViewById(R.id.text)).setText(list.get(position));

            return rootView;
        }
    }

}
