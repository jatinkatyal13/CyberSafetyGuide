package com.android.guide.safety.cybersafetyguide;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class background extends Service {
    public background() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void Notify(String ticker, String text){
        Intent intent = new Intent(background.this, MainActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pintent = PendingIntent.getActivity(background.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(background.this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Cyber Security Guide")
                .setContentText(text)
                .setTicker(ticker)
                .setContentIntent(pintent)
                .setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Log.w("jatin", "Service started");
                    URL url = new URL("http://www.abboniss.com/test/get_version.php");
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    InputStream in = connection.getInputStream();
                    String json = "";
                    int c;
                    while ((c = in.read()) != -1){
                        json+=(char)c;
                    }
                    JSONObject object = new JSONObject(json);
                    in = openFileInput("version.txt");
                    String jsonLocal = "";
                    while ((c = in.read()) != -1){
                        jsonLocal+=(char)c;
                    }
                    JSONObject objectLocal = new JSONObject(jsonLocal);

                    if (Integer.parseInt(object.getString("about_us")) > Integer.parseInt(objectLocal.getString("about_us"))){
                        //update about_us
                        InputStream get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/about_us_get.php").openConnection())).getInputStream();
                        int a;
                        String jsonText = "";
                        while ((a = get.read()) != -1){
                            jsonText += (char)a;
                        }
                        openFileOutput("about_us.txt", MODE_PRIVATE).write(jsonText.getBytes());
                        openFileOutput("version.txt", MODE_PRIVATE).write(json.getBytes());
                        Log.w("jatin", "about us");
                    }

                    if (Integer.parseInt(object.getString("victim")) > Integer.parseInt(objectLocal.getString("victim"))){
                        //update victim
                        InputStream get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/victim_get.php").openConnection())).getInputStream();
                        int a;
                        String jsonText = "";
                        while ((a = get.read()) != -1){
                            jsonText += (char)a;
                        }
                        openFileOutput("victim.txt", MODE_PRIVATE).write(jsonText.getBytes());
                        openFileOutput("version.txt", MODE_PRIVATE).write(json.getBytes());
                        Log.w("jatin", "victim");
                    }

                    if (Integer.parseInt(object.getString("threats")) > Integer.parseInt(objectLocal.getString("threats"))){
                        //update threats
                        InputStream get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/threats_get.php").openConnection())).getInputStream();
                        int a;
                        String jsonText = "";
                        while ((a = get.read()) != -1){
                            jsonText += (char)a;
                        }
                        openFileOutput("threats.txt", MODE_PRIVATE).write(jsonText.getBytes());
                        openFileOutput("version.txt", MODE_PRIVATE).write(json.getBytes());
                        Log.w("jatin", "threats");
                    }

                    if (Integer.parseInt(object.getString("kids")) > Integer.parseInt(objectLocal.getString("kids"))){
                        //update kids
                        InputStream get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/guideline_kids_get.php").openConnection())).getInputStream();
                        int a;
                        String jsonText = "";
                        while ((a = get.read()) != -1){
                            jsonText += (char)a;
                        }
                        openFileOutput("json_guideline_kids.txt", MODE_PRIVATE).write(jsonText.getBytes());
                        openFileOutput("version.txt", MODE_PRIVATE).write(json.getBytes());
                        Log.w("jatin", "kids");
                    }

                    if (Integer.parseInt(object.getString("parents")) > Integer.parseInt(objectLocal.getString("parents"))){
                        //update parents
                        InputStream get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/guideline_parents_get.php").openConnection())).getInputStream();
                        int a;
                        String jsonText = "";
                        while ((a = get.read()) != -1){
                            jsonText += (char)a;
                        }
                        openFileOutput("json_guideline_parents.txt", MODE_PRIVATE).write(jsonText.getBytes());
                        openFileOutput("version.txt", MODE_PRIVATE).write(json.getBytes());
                        Log.w("jatin", "parents");
                    }

                    if (Integer.parseInt(object.getString("corporates")) > Integer.parseInt(objectLocal.getString("corporates"))){
                        //update corporates
                        InputStream get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/guideline_corporate_get.php").openConnection())).getInputStream();
                        int a;
                        String jsonText = "";
                        while ((a = get.read()) != -1){
                            jsonText += (char)a;
                        }
                        openFileOutput("json_guideline_corporate.txt", MODE_PRIVATE).write(jsonText.getBytes());
                        openFileOutput("version.txt", MODE_PRIVATE).write(json.getBytes());
                        Log.w("jatin", "corporate");
                    }

                    if (Integer.parseInt(object.getString("cyber_cells")) > Integer.parseInt(objectLocal.getString("cyber_cells"))){
                        //update cyber_cells
                        InputStream get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/cyber_cells_get.php").openConnection())).getInputStream();
                        int a;
                        String jsonText = "";
                        while ((a = get.read()) != -1){
                            jsonText += (char)a;
                        }
                        openFileOutput("cyber_cells.txt", MODE_PRIVATE).write(jsonText.getBytes());
                        openFileOutput("version.txt", MODE_PRIVATE).write(json.getBytes());
                        Log.w("jatin", "cyber_cells");
                    }

                    if (Integer.parseInt(object.getString("dodont")) > Integer.parseInt(objectLocal.getString("dodont"))){
                        //update dodont
                        InputStream get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/dodont_get.php").openConnection())).getInputStream();
                        int a;
                        String jsonText = "";
                        while ((a = get.read()) != -1){
                            jsonText += (char)a;
                        }
                        openFileOutput("json_dodont.txt", MODE_PRIVATE).write(jsonText.getBytes());
                        openFileOutput("version.txt", MODE_PRIVATE).write(json.getBytes());
                        Log.w("jatin", "dodont");
                    }

                } catch (MalformedURLException e){
                    Log.w("jatin", "URL Exception");
                } catch (IOException e){
                    try {
                        String jsonText = "{\"about_us\":\"0\", \"victim\":\"0\", \"threats\":\"0\", \"kids\": \"0\", \"parents\":\"0\", \"corporates\":\"0\", \"cyber_cells\":\"0\", \"dodont\":\"0\"}";
                        openFileOutput("version.txt", MODE_PRIVATE).write(jsonText.getBytes());
                        Log.w("jatin", "new version.txt created");
                    } catch (Exception ex){
                        Log.w("jatin", "error in creating version.txt");
                    }
                } catch (JSONException e){
                    Log.w("jatin", "JSON Exception");
                    try {
                        Log.w("jatin", "new version.txt from json exception");
                        InputStream get = ((HttpURLConnection)(new URL("http://www.abboniss.com/test/get_version.php").openConnection())).getInputStream();
                        int a;
                        String jsonText = "";
                        while ((a = get.read()) != -1){
                            jsonText += (char)a;
                        }
                        openFileOutput("version.txt", MODE_PRIVATE).write(jsonText.getBytes());
                    } catch (Exception ex){
                        Log.w("jatin", "cannot create version.txt from json exception");
                    }
                }




                //check for notification
                try {
                    Log.w("jatin", "started getting from get_notification");
                    InputStream in = ((HttpURLConnection)new URL("http://www.abboniss.com/test/get_notification.php").openConnection()).getInputStream();
                    int c;
                    String json = "";
                    while ((c = in.read()) != -1){
                        json+=(char)c;
                    }
                    JSONObject object = new JSONObject(json);

                    in = openFileInput("notification.txt");
                    String jsonlocal = "";
                    while ((c = in.read()) != -1){
                        jsonlocal+=(char)c;
                    }
                    JSONObject objectLocal = new JSONObject(jsonlocal);

                    if (Integer.parseInt(object.getString("version")) > Integer.parseInt(objectLocal.getString("version"))){

                        Notify(object.getString("ticker"), object.getString("text"));

                        openFileOutput("notification.txt",MODE_PRIVATE).write(json.getBytes());


                    }

                } catch (FileNotFoundException e) {
                    Log.w("jatin", "notification file not found");
                    try{
                        InputStream in = ((HttpURLConnection)new URL("http://www.abboniss.com/test/get_notification.php").openConnection()).getInputStream();
                        int c;
                        String json = "";
                        while ((c = in.read()) != -1){
                            json+=(char)c;
                        }
                        openFileOutput("notification.txt", MODE_PRIVATE).write(json.getBytes());
                        JSONObject object = new JSONObject(json);
                        Notify(object.getString("ticker"), object.getString("text"));
                    } catch (Exception ex){
                        Log.w("jatin", "error");
                    }
                } catch (MalformedURLException e) {
                    Log.w("jatin", "url notification error");
                } catch (IOException e) {
                    try{
                        Log.w("jatin", "IO error notification");
                        InputStream in = ((HttpURLConnection)new URL("http://www.abboniss.com/test/get_notification.php").openConnection()).getInputStream();
                        int c;
                        String json = "";
                        while ((c = in.read()) != -1){
                            json+=(char)c;
                        }
                        openFileOutput("notification.txt", MODE_PRIVATE).write(json.getBytes());
                        JSONObject object = new JSONObject(json);
                        Notify(object.getString("ticker"), object.getString("text"));
                    } catch (Exception ex){
                        Log.w("jatin", "notification error 2");
                    }
                } catch (JSONException e) {
                    Log.w("jatin", "json exception");
                    String json = "";
                    try{
                        InputStream in = ((HttpURLConnection)new URL("http://www.abboniss.com/test/get_notification.php").openConnection()).getInputStream();
                        int c;
                        json = "";
                        while ((c = in.read()) != -1){
                            json+=(char)c;
                        }
                        openFileOutput("notification.txt", MODE_PRIVATE).write(json.getBytes());
                        JSONObject object = new JSONObject(json);
                        Notify(object.getString("ticker"), object.getString("text"));
                    } catch (Exception ex){
                        Log.w("jatin", ex.toString());
                        Log.w("jatin", json);
                    }
                }

            }
        }, 1000, 5000);
    }
}
