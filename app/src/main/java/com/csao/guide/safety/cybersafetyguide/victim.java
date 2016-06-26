package com.android.guide.safety.cybersafetyguide;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class victim extends AppCompatActivity {

    List<Victim_Data> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victim);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = getJson();

        MyAdapter adapter = new MyAdapter(list);
        final ListView listView = (ListView)findViewById(R.id.list_victim_content);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!list.get(position).selected){
                    list.set(position, new Victim_Data(list.get(position).title, list.get(position).score, true));
                    ((CardView)listView.getChildAt(position).findViewById(R.id.card_view)).setCardBackgroundColor(Color.parseColor("#b1e476"));
                } else {
                    list.set(position, new Victim_Data(list.get(position).title, list.get(position).score, false));
                    ((CardView)listView.getChildAt(position).findViewById(R.id.card_view)).setCardBackgroundColor(Color.parseColor("#ffffff"));

                }
            }
        });

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)findViewById(R.id.victim_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://www.abboniss.com/test/victim_get.php");
                            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                            InputStream in = urlConnection.getInputStream();
                            String Jsontext = "";
                            int c;
                            while ((c = in.read()) != -1){
                                Jsontext += (char)c;
                            }
                            FileOutputStream file = openFileOutput("victim.txt", MODE_PRIVATE);
                            file.write(Jsontext.getBytes());
                            JSONObject object = new JSONObject(Jsontext);
                            JSONArray data = object.getJSONArray("info");
                            final List<Victim_Data> listr = new ArrayList<>();
                            for (int i=0; i<data.length(); i++){
                                JSONArray dat = data.getJSONArray(i);
                                listr.add(new Victim_Data(dat.getString(0), Integer.parseInt(dat.getString(1))));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list = listr;
                                    ((ListView)findViewById(R.id.list_victim_content)).setAdapter(new MyAdapter(list));
                                    refreshLayout.setRefreshing(false);
                                }
                            });
                        } catch (Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }
                }).start();

            }
        });

    }

    private List<Victim_Data> getJson(){
        List<Victim_Data> emptyList = new ArrayList<>();
        try {
            List<Victim_Data> list = new ArrayList<>();
            FileInputStream in = openFileInput("victim.txt");
            String Jsontext = "";
            int c;
            while ((c = in.read()) != -1){
                Jsontext += (char)c;
            }
            JSONObject object = new JSONObject(Jsontext);
            JSONArray data = object.getJSONArray("info");
            for (int i=0; i<data.length(); i++){
                JSONArray dat = data.getJSONArray(i);
                list.add(new Victim_Data(dat.getString(0), Integer.parseInt(dat.getString(1))));
                //list.add(new Victim_Data("test", "test"));
            }
            Log.w("jatin", "file found");
            Log.w("jatin", Jsontext);
            return list;
        } catch (Exception e){
            //Some default json text
            Log.w("jatin", "file not found");
            List<Victim_Data> list = new ArrayList<>();
            String Jsontext = "";
            try {
                JSONObject object = new JSONObject(Jsontext);
                JSONArray data = object.getJSONArray("info");
                for (int i = 0; i < data.length(); i++) {
                    JSONArray dat = data.getJSONArray(i);
                    list.add(new Victim_Data(dat.getString(0), Integer.parseInt(dat.getString(1))));
                }
            } catch (Exception ex){
                Toast.makeText(victim.this, "Error Occured", Toast.LENGTH_SHORT).show();
            }
            return list;
        }
    }

    private class Victim_Data{
        public String title;
        public int score;
        public boolean selected;
        public Victim_Data(String title, int score){
            this.title = title;
            this.score = score;
            this.selected = false;
        }
        public Victim_Data(String title, int score, boolean selected){
            this.title = title;
            this.score = score;
            this.selected = selected;
        }
    }

    private class MyAdapter extends ArrayAdapter<Victim_Data> {
        List<Victim_Data> list = new ArrayList<>();
        MyAdapter(List<Victim_Data> list){
            super(victim.this, R.layout.guideline_card_item, list);
            this.list = list;
        }
        @Override
        public View getView(int position, View view, ViewGroup viewgroup){
            View itemview = getLayoutInflater().inflate(R.layout.guideline_card_item, viewgroup, false);

            TextView main = (TextView)itemview.findViewById(R.id.tv_text);
            main.setText(list.get(position).title);

            if (list.get(position).selected){
                ((CardView)itemview.findViewById(R.id.card_view)).setCardBackgroundColor(Color.parseColor("#b1e476"));
            } else {
                ((CardView)itemview.findViewById(R.id.card_view)).setCardBackgroundColor(Color.parseColor("#ffffff"));
            }


            return itemview;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.victim, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.next) {
            int score = 0;
            int cnt = 0;
            int sum = 0;
            List<String> probs = new ArrayList<>();
            for (int i =0; i<list.size(); i++){
                if (list.get(i).selected){
                    score += list.get(i).score;
                    probs.add(list.get(i).title);
                    cnt++;
                }
                sum += list.get(i).score;
            }
            if (cnt == 0){
                Toast.makeText(victim.this, "No vulnerability selected !", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent (victim.this, victim_display.class);
                int level = ((score*100)/sum);

                intent.putExtra("score", level);
                intent.putExtra("probs", (ArrayList<String>)probs);
                startActivity(intent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
