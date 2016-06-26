package com.android.guide.safety.cybersafetyguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class about_us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((CardView)findViewById(R.id.experts)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(about_us.this, expert_panel.class);
                startActivity(intent);
            }
        });

        ((CardView)findViewById(R.id.developers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(about_us.this, developer_panel.class);
                startActivity(intent);
            }
        });

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String text = "";
                        boolean done =false;
                        try {
                            URL url = new URL("http://abboniss.com/test/about_us_get.php");
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStream in = new BufferedInputStream((urlConnection.getInputStream()));
                            int c;
                            String jsonText = "";
                            while ((c = in.read()) != -1) {
                                jsonText += String.valueOf((char) c);
                            }
                            FileOutputStream out = openFileOutput("about_us.txt", MODE_PRIVATE);
                            out.write(jsonText.getBytes());
                            text = jsonText;
                            done = true;
                        }
                        catch (Exception e){
                            done = false;
                        }
                        final String finalText = text;
                        final boolean finalDone = done;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (finalDone) {
                                    TextView textv = (TextView) findViewById(R.id.aboutus_text);
                                    textv.setText(finalText);
                                }
                                else {
                                    Toast.makeText(about_us.this, "Unable to refresh !", Toast.LENGTH_SHORT).show();
                                }
                                refreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }.start();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView text = (TextView)findViewById(R.id.aboutus_text);
        text.setText(new updateList().doInBackground());
    }

    private String setTextBar(){
        String text = "";
        try {
            FileInputStream file = openFileInput("about_us.txt");
            int a;
            while ((a = file.read()) != -1){
                text+=String.valueOf((char)a);
            }
        } catch (Exception ex) {
            String about = "Some Default text to be entered in the case that file doesnt exist";
            text+=about;
            try {
                FileOutputStream out = openFileOutput("about_us.txt", MODE_PRIVATE);
                out.write(about.getBytes());
            } catch (Exception exe){
                Toast.makeText(about_us.this, "Error occured", Toast.LENGTH_SHORT).show();
            }
        }
        return text;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class updateList extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            return setTextBar();
        }
    }
}
