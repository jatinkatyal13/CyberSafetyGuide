package com.android.guide.safety.cybersafetyguide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class cyber_cells extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_cyber_cells);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView)findViewById(R.id.cyber_cell_list);

        listView.setAdapter(new MyAdapter(getApplicationContext(), getJson()));

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);

        refreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        final List<state_list> list = new ArrayList<>();
                        List<String> states = new ArrayList<>();
                        boolean done =false;
                        try {
                            URL url = new URL("http://abboniss.com/test/cyber_cells_get.php");
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStream in = new BufferedInputStream((urlConnection.getInputStream()));
                            int c;
                            String jsonText = "";
                            while ((c = in.read()) != -1) {
                                jsonText += String.valueOf((char) c);
                            }
                            JSONObject obj = new JSONObject(jsonText);
                            FileOutputStream out = openFileOutput("cyber_cells.txt", MODE_PRIVATE);
                            out.write(jsonText.getBytes());

                            JSONArray state = obj.getJSONArray("states");
                            for (int i=0; i<state.length(); i++){
                                states.add(state.get(i).toString());
                            }

                            JSONObject info = obj.getJSONObject("info");
                            for (String s : states){
                                JSONArray st = info.getJSONArray(s);
                                List<Branch> branch = new ArrayList<>();
                                for (int i=0; i<st.length(); i++){
                                    JSONObject temp = st.getJSONObject(i);
                                    branch.add(new Branch(temp.getString("name"), temp.getString("desig"), temp.getString("number"), temp.getString("email")));
                                }
                                list.add(new state_list(s, branch));
                            }

                            done = true;
                        }
                        catch (Exception e){
                            done = false;
                        }

                        final boolean finalDone = done;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (finalDone) {
                                    ((ListView)findViewById(R.id.cyber_cell_list)).setAdapter(new MyAdapter(getApplicationContext(), list));
                                }
                                else {
                                    Toast.makeText(cyber_cells.this, "Unable to refresh !", Toast.LENGTH_SHORT).show();
                                }
                                refreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }.start();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), cyber_cells_display.class);
                List<state_list> list = getJson();
                intent.putExtra("state", list.get(position).state);
                intent.putExtra("name_list", (ArrayList<String>)list.get(position).get_name_list());
                intent.putExtra("desig_list", (ArrayList<String>)list.get(position).get_desig_list());
                intent.putExtra("number_list", (ArrayList<String>)list.get(position).get_number_list());
                intent.putExtra("email_list", (ArrayList<String>)list.get(position).get_email_list());
                startActivity(intent);
            }
        });
    }

    private List<state_list> getJson(){
            try {
                final List<state_list> list = new ArrayList<>();
                List<String> states = new ArrayList<>();
                InputStream in = openFileInput("cyber_cells.txt");
                int c;
                String jsonText = "";
                while ((c = in.read()) != -1) {
                    jsonText += String.valueOf((char) c);
                }
                JSONObject obj = new JSONObject(jsonText);
                FileOutputStream out = openFileOutput("cyber_cells.txt", MODE_PRIVATE);
                out.write(jsonText.getBytes());

                JSONArray state = obj.getJSONArray("states");
                for (int i=0; i<state.length(); i++){
                    states.add(state.get(i).toString());
                }

                JSONObject info = obj.getJSONObject("info");
                for (String s : states){
                    JSONArray st = info.getJSONArray(s);
                    List<Branch> branch = new ArrayList<>();
                    for (int i=0; i<st.length(); i++){
                        JSONObject temp = st.getJSONObject(i);
                        branch.add(new Branch(temp.getString("name"), temp.getString("desig"), temp.getString("number"), temp.getString("email")));
                    }
                    list.add(new state_list(s, branch));
                }
                return list;
            } catch (Exception ex){
                try {
                    final List<state_list> list = new ArrayList<>();
                    List<String> states = new ArrayList<>();
                    String jsonText = "";
                    JSONObject obj = new JSONObject(jsonText);
                    FileOutputStream out = openFileOutput("cyber_cells.txt", MODE_PRIVATE);
                    out.write(jsonText.getBytes());

                    JSONArray state = obj.getJSONArray("states");
                    for (int i=0; i<state.length(); i++){
                        states.add(state.get(i).toString());
                    }

                    JSONObject info = obj.getJSONObject("info");
                    for (String s : states){
                        JSONArray st = info.getJSONArray(s);
                        List<Branch> branch = new ArrayList<>();
                        for (int i=0; i<st.length(); i++){
                            JSONObject temp = st.getJSONObject(i);
                            branch.add(new Branch(temp.getString("name"), temp.getString("desig"), temp.getString("number"), temp.getString("email")));
                        }
                        list.add(new state_list(s, branch));
                    }
                    return list;
                } catch (Exception exe){
                    Toast.makeText(cyber_cells.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    return new ArrayList<>();
                }
            }
    }

    private class state_list{
        public String state;
        public List<Branch> branches;

        state_list(String state, List<Branch> branches){
            this.state = state;
            this.branches = branches;
        }

        public List<String> get_name_list(){
            List<String> list = new ArrayList<>();
            for (Branch b : branches){
                list.add(b.name);
            }
            return list;
        }

        public List<String> get_desig_list(){
            List<String> list = new ArrayList<>();
            for (Branch b : branches){
                list.add(b.desig);
            }
            return list;
        }

        public List<String> get_number_list(){
            List<String> list = new ArrayList<>();
            for (Branch b : branches){
                list.add(b.number);
            }
            return list;
        }

        public List<String> get_email_list(){
            List<String> list = new ArrayList<>();
            for (Branch b : branches){
                list.add(b.email);
            }
            return list;
        }
    }

    private class Branch {
        public String name;
        public String desig;
        public String number;
        public String email;

        Branch(String name, String desig, String number, String email){
            this.name = name;
            this.desig = desig;
            this.number = number;
            this.email = email;
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cyber_cells, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    */
    private class MyAdapter extends ArrayAdapter<state_list>{
        List<state_list> list = new ArrayList<>();

        public MyAdapter(Context context, List<state_list> list) {
            super(context, R.layout.cyber_cell_card_item, list);
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = convertView;
            if (rootView == null){
                rootView = getLayoutInflater().inflate(R.layout.cyber_cell_card_item, null);
            }

            ((TextView)rootView.findViewById(R.id.tv_text)).setText(list.get(position).state);

            return rootView;
        }
    }

}
