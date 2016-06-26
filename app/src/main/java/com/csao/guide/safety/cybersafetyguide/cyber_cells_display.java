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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class cyber_cells_display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyber_cells_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        List<String> name = intent.getStringArrayListExtra("name_list");
        List<String> desig = intent.getStringArrayListExtra("desig_list");
        List<String> number = intent.getStringArrayListExtra("number_list");
        List<String> email = intent.getStringArrayListExtra("email_list");

        List<Branch> list = new ArrayList<>();

        for (int i=0; i<name.size(); i++){
            list.add(new Branch(name.get(i), desig.get(i), number.get(i), email.get(i)));
        }

        ((ListView) findViewById(R.id.list)).setAdapter(new ListAdapter(getApplicationContext(), list));
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

    private class ListAdapter extends ArrayAdapter<Branch>{

        private List<Branch> list = new ArrayList<>();

        public ListAdapter(Context context, List<Branch> list) {
            super(context, R.layout.cyber_cell_display_card_item, list);
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = getLayoutInflater().inflate(R.layout.cyber_cell_display_card_item, parent, false);

            ((TextView)rootView.findViewById(R.id.name)).setText(list.get(position).name);
            ((TextView)rootView.findViewById(R.id.desig)).setText(list.get(position).desig);
            ((TextView)rootView.findViewById(R.id.number)).setText(list.get(position).number);
            ((TextView)rootView.findViewById(R.id.email)).setText(list.get(position).email);

            return rootView;
        }
    }

}
