package com.android.guide.safety.cybersafetyguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final List<String> list = new ArrayList<>();
        list.add("Email Hacking");
        list.add("Website / Domain Hacking");
        list.add("Social Profile Hacking");
        list.add("Abusive Email or Chat");
        list.add("EMail Threat");
        list.add("Pornography");
        list.add("Spoofing");
        list.add("Online Defamation");
        list.add("Net Extortion");
        list.add("Cyber Stalking");
        list.add("E-Commerce Frauds");
        list.add("Banking Frauds");
        list.add("Copyright Infringement");

        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(new ListAdapter(getApplicationContext(), list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(report.this, report_abuse.class);
                intent.putExtra("nature", list.get(position));
                startActivity(intent);
            }
        });
    }

    private class ListAdapter extends ArrayAdapter<String>{

        List<String> list = new ArrayList<>();

        public ListAdapter(Context context, List<String> list) {
            super(context, R.layout.cyber_cell_card_item, list);
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = getLayoutInflater().inflate(R.layout.cyber_cell_card_item, parent, false);

            ((TextView)rootView.findViewById(R.id.tv_text)).setText(list.get(position));

            return rootView;
        }
    }
}
