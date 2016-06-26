package com.android.guide.safety.cybersafetyguide;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class dodont_fragment extends Fragment {

    private static Context context;
    private LayoutInflater inflater;

    public dodont_fragment() {
        // Required empty public constructor
    }

    public void add_context(Context context){
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        final View rootView = inflater.inflate(R.layout.content_dodont, container, false);

        ArrayAdapter<String> adapter = new MyAdapter(getJson());
        ListView listView = (ListView) rootView.findViewById(R.id.dodont_ListView);
        listView.setAdapter(adapter);

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.dodont_swipe_layout);
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        boolean done = false;
                        final List<String> list = new ArrayList<String>();
                        try {
                            URL url = new URL("http://www.abboniss.com/test/dodont_get.php");
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStream in = new BufferedInputStream((urlConnection.getInputStream()));
                            int c;
                            String jsonText = "";
                            while ((c = in.read()) != -1){
                                jsonText += String.valueOf((char)c);
                            }
                            OutputStream file = context.openFileOutput("json_dodont.txt", context.MODE_PRIVATE);
                            file.write(jsonText.getBytes());
                            JSONObject obj = new JSONObject(jsonText);
                            JSONArray net = obj.getJSONArray("dodont");
                            for (int i=0; i<net.length(); i++){
                                list.add(String.valueOf(net.get(i)));
                            }
                            done = true;
                        }
                        catch (Exception e){
                            done = false;
                        }
                        final boolean finalDone = done;
                        new Handler(context.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (finalDone){
                                    ArrayAdapter<String> adapter = new MyAdapter(list);
                                    ListView listView = (ListView) rootView.findViewById(R.id.dodont_ListView);
                                    listView.setAdapter(adapter);
                                }
                                else {
                                    Toast.makeText(context, "Unable to refresh !", Toast.LENGTH_SHORT).show();
                                }
                                refreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }.start();
            }
        });

        return rootView;
    }

    private class MyAdapter extends ArrayAdapter<String>{
        List<String> list = new ArrayList<String>();
        MyAdapter(List<String> list){
            super(context, R.layout.guideline_card_item, list);
            this.list = list;
        }
        @Override
        public View getView(int position, View view, ViewGroup viewgroup){
            View itemview = view;
            if (itemview == null){
                itemview = inflater.inflate(R.layout.guideline_card_item, viewgroup, false);
            }

            TextView main = (TextView)itemview.findViewById(R.id.tv_text);
            main.setText(list.get(position));

            return itemview;
        }
    }

    private List<String> getJson(){
        List<String> empty_list = new ArrayList<>();
        try {
            List<String> list = new ArrayList<String>();
            InputStream file = context.openFileInput("json_dodont.txt");
            String jsonText = "";
            int c;
            while ((c = file.read()) != -1){
                jsonText += String.valueOf((char)c);
            }
            JSONObject obj = new JSONObject(jsonText);
            JSONArray net = obj.getJSONArray("dodont");
            for (int i=0; i<net.length(); i++){
                list.add(String.valueOf(net.get(i)));
            }
            return list;
        } catch (Exception ex){
            try {
                List<String> list = new ArrayList<String>();
                String jsonText = "";
                JSONObject obj = new JSONObject(jsonText);
                JSONArray net = obj.getJSONArray("dodont");
                for (int i = 0; i < net.length(); i++) {
                    list.add(String.valueOf(net.get(i)));
                }
                OutputStream out = context.openFileOutput("json_dodont.txt", context.MODE_PRIVATE);
                out.write(jsonText.getBytes());
                return list;
            } catch (Exception exe){
                Toast.makeText(context, "Error Occured !", Toast.LENGTH_SHORT).show();
            }
        }
        return empty_list;
    }

}
