package com.android.guide.safety.cybersafetyguide;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
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

public class credit_fragment extends Fragment {

    private static Context context;
    private LayoutInflater inflater;

    public credit_fragment() {
        // Required empty public constructor
    }

    public void add_context(Context context){
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        final View rootView = inflater.inflate(R.layout.content_credit, container, false);

        final List<link> list = new ArrayList<>();
        list.add(new link("Google", "https://www.google.com"));
        list.add(new link("Google", "https://www.google.com"));
        list.add(new link("Google", "https://www.google.com"));
        list.add(new link("Google", "https://www.google.com"));


        ((ListView)rootView.findViewById(R.id.list)).setAdapter(new MyAdapter(list));
        ((ListView)rootView.findViewById(R.id.list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent browser = new Intent(Intent.ACTION_VIEW);
                    browser.setData(Uri.parse(list.get(position).link));
                    startActivity(browser);
                } catch (Exception e){
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "No Browser Application Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;
    }

    private class link{
        public String heading;
        public String link;
        public link(String heading, String link){
            this.heading = heading;
            this.link = link;
        }
    }

    private class MyAdapter extends ArrayAdapter<link>{
        List<link> list = new ArrayList<>();
        MyAdapter(List<link> list){
            super(context, R.layout.credit_card_item, list);
            this.list = list;
        }
        @Override
        public View getView(int position, View view, ViewGroup viewgroup){
            View itemview = view;
            if (itemview == null){
                itemview = inflater.inflate(R.layout.credit_card_item, viewgroup, false);
            }

            ((TextView)itemview.findViewById(R.id.heading)).setText(list.get(position).heading);
            ((TextView)itemview.findViewById(R.id.link)).setText(list.get(position).link);

            return itemview;
        }
    }


}

