package com.android.guide.safety.cybersafetyguide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {


                try {
                    String jsonText = "";

                    InputStream get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/about_us_get.php").openConnection())).getInputStream();
                    int a;
                    jsonText = "";
                    while ((a = get.read()) != -1){
                        jsonText += (char)a;
                    }
                    openFileOutput("about_us.txt", MODE_PRIVATE).write(jsonText.getBytes());


                    get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/victim_get.php").openConnection())).getInputStream();

                    jsonText = "";
                    while ((a = get.read()) != -1){
                        jsonText += (char)a;
                    }
                    openFileOutput("victim.txt", MODE_PRIVATE).write(jsonText.getBytes());


                    get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/threats_get.php").openConnection())).getInputStream();

                    jsonText = "";
                    while ((a = get.read()) != -1){
                        jsonText += (char)a;
                    }
                    openFileOutput("threats.txt", MODE_PRIVATE).write(jsonText.getBytes());


                    get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/guideline_kids_get.php").openConnection())).getInputStream();

                    jsonText = "";
                    while ((a = get.read()) != -1){
                        jsonText += (char)a;
                    }
                    openFileOutput("json_guideline_kids.txt", MODE_PRIVATE).write(jsonText.getBytes());




                    get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/guideline_parents_get.php").openConnection())).getInputStream();

                    jsonText = "";
                    while ((a = get.read()) != -1){
                        jsonText += (char)a;
                    }
                    openFileOutput("json_guideline_parents.txt", MODE_PRIVATE).write(jsonText.getBytes());




                    get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/guideline_corporate_get.php").openConnection())).getInputStream();

                    jsonText = "";
                    while ((a = get.read()) != -1){
                        jsonText += (char)a;
                    }
                    openFileOutput("json_guideline_corporate.txt", MODE_PRIVATE).write(jsonText.getBytes());




                    get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/cyber_cells_get.php").openConnection())).getInputStream();

                    jsonText = "";
                    while ((a = get.read()) != -1){
                        jsonText += (char)a;
                    }
                    openFileOutput("cyber_cells.txt", MODE_PRIVATE).write(jsonText.getBytes());



                    get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/dodont_get.php").openConnection())).getInputStream();

                    jsonText = "";
                    while ((a = get.read()) != -1){
                        jsonText += (char)a;
                    }
                    openFileOutput("json_dodont.txt", MODE_PRIVATE).write(jsonText.getBytes());



                } catch (Exception e){

                }



                Intent main = new Intent(Splash.this, MainActivity.class);
                Intent first = new Intent(Splash.this, welcome.class);
                try {
                    InputStream in = openFileInput("secure.db");
                    String json = "";
                    int c;
                    while ((c = in.read()) != -1){
                        json += (char)c;
                    }
                    JSONObject object = new JSONObject(json);
                    String name = object.getString("name");
                    String user = object.getString("email");
                    String pass = object.getString("pass");

                    HttpURLConnection connection = (HttpURLConnection)((new URL("http://www.abboniss.com/test/isAuthentic.php?code=securepass@csao2016&user="+user+"&pass="+pass)).openConnection());
                    InputStream res = connection.getInputStream();
                    String message = "";
                    while ((c = res.read()) != -1){
                        message += (char)c;
                    }

                    if (message.equals("true")){
                        main.putExtra("name", name);
                        main.putExtra("email", user);
                        startActivity(main);
                        Splash.this.finish();
                    } else {
                        startActivity(first);
                        Splash.this.finish();
                    }

                } catch (FileNotFoundException e){
                    //exception in reading file
                    startActivity(first);
                    Splash.this.finish();

                } catch (IOException e) {
                    //exception in internet
                    try {
                        InputStream in = openFileInput("secure.db");
                        String json = "";
                        int c;
                        while ((c = in.read()) != -1){
                            json += (char)c;
                        }
                        JSONObject object = new JSONObject(json);
                        String name = object.getString("name");
                        String user = object.getString("email");
                        String pass = object.getString("pass");
                        main.putExtra("name", name);
                        main.putExtra("email", user);
                        startActivity(main);
                        Splash.this.finish();
                    } catch (Exception ex){
                        startActivity(first);
                        Splash.this.finish();
                    }

                } catch (JSONException e) {
                    //decoding error , corrupt secure.db file
                    startActivity(first);
                    Splash.this.finish();
                }
            }
        }, 500);

    }
}
