package com.android.guide.safety.cybersafetyguide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class threats extends AppCompatActivity {

    List<threat_data> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = getJson();

        final ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(new ListAdapter(getApplicationContext(), list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(threats.this, threat_display.class);
                intent.putExtra("heading", list.get(position).heading);
                intent.putExtra("description", list.get(position).description);
                intent.putExtra("threats", list.get(position).threats);
                startActivity(intent);
            }
        });

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);

        refreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            List<threat_data> rList = new ArrayList<threat_data>();
                            URL url = new URL("http://www.abboniss.com/test/threats_get.php");
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStream in = new BufferedInputStream((urlConnection.getInputStream()));
                            int c;
                            String jsonText = "";
                            while ((c = in.read()) != -1){
                                jsonText += String.valueOf((char)c);
                            }
                            OutputStream file = openFileOutput("threats.txt", MODE_PRIVATE);
                            file.write(jsonText.getBytes());
                            JSONObject object = new JSONObject(jsonText);
                            JSONArray array = object.getJSONArray("info");
                            for (int i=0; i<array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                rList.add(new threat_data(obj.getString("heading"), obj.getString("what_it_is"), obj.getString("what_it_can_do")));
                            }
                            list = rList;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        listView.setAdapter(new ListAdapter(getApplicationContext(), list));
                                    } catch (Exception e){
                                        Toast.makeText(threats.this, "Error occured !", Toast.LENGTH_SHORT).show();
                                    }
                                    refreshLayout.setRefreshing(false);
                                }
                            });
                        }
                        catch (Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(threats.this, "Unable to refresh", Toast.LENGTH_SHORT).show();
                                    refreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }
                }.start();
            }
        });

    }

    private class threat_data {
        String heading;
        String description;
        String threats;
        public threat_data(String h, String d, String t){
            heading = h;
            description = d;
            threats = t;
        }
    }

    private List<threat_data> getJson(){
        List<threat_data> empty = new ArrayList<>();

        try {
            List<threat_data> list = new ArrayList<>();
            InputStream file = openFileInput("threats.txt");
            String jsonText = "";
            int c;
            while ((c = file.read()) != -1){
                jsonText += (char)c;
            }
            JSONObject object = new JSONObject(jsonText);
            JSONArray array = object.getJSONArray("info");
            for (int i=0; i<array.length(); i++){
                JSONObject obj = ((JSONObject) array.get(i));
                list.add(new threat_data(obj.getString("heading"), obj.getString("what_it_is"), obj.getString("what_it_can_do")));
            }
            return list;
        } catch (Exception e){
            try {
                List<threat_data> list = new ArrayList<>();
                String jsonText = ""; // some default json encoded string in case the file is not found
                JSONObject object = new JSONObject(jsonText);
                JSONArray array = object.getJSONArray("info");
                for (int i=0; i<array.length(); i++){
                    JSONObject obj = ((JSONObject) array.get(i));
                    list.add(new threat_data(obj.getString("heading"), obj.getString("what_it_is"), obj.getString("what_it_can_do")));
                }
                return list;
            } catch (Exception ex){
                Toast.makeText(threats.this, "Error Occured", Toast.LENGTH_SHORT).show();
            }
        }
        return empty;
    }

    private class ListAdapter extends ArrayAdapter<threat_data> {

        private List<threat_data> list = new ArrayList<>();

        public ListAdapter(Context context, List<threat_data> list) {
            super(context, R.layout.threats_card_item, list);
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = convertView;

            if (rootView == null){
                rootView = getLayoutInflater().inflate(R.layout.threats_card_item, parent, false);
            }

            ((TextView)rootView.findViewById(R.id.tv_text)).setText(list.get(position).heading);

            return rootView;
        }
    }

}
