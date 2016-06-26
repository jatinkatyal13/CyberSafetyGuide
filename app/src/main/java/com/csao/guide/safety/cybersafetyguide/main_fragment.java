package com.android.guide.safety.cybersafetyguide;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class main_fragment extends Fragment {

    LayoutInflater inflater;
    static Context context;

    public main_fragment() {
        // Required empty public constructor
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void add_context(Context context){
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container,false);
        this.inflater = inflater;

        ListView myList = (ListView)rootView.findViewById(R.id.myListView);
        List<homeList> list = new ArrayList<homeList>();

        list.add(new homeList("Check whether You Are Victim or Not", "Form",R.drawable.victim));
        list.add(new homeList("Know About Cyber Threats", "Guide", R.drawable.know));
        list.add(new homeList("Cyber Complaint", "Report an Abuse", R.drawable. volunteer));
        list.add(new homeList("Cyber Guide", "Guide", R.drawable.guide));
        list.add(new homeList("Consult with Cyber Expert", "Contact Here", R.drawable.talk));
        list.add(new homeList("Cyber Cells Contact Number", "Contacts", R.drawable.numbers));

        ArrayAdapter<homeList> adapter = new MyAdapter(list);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent victim = new Intent(context, victim.class);
                        startActivity(victim);
                        break;
                    case 1:
                        Intent threats = new Intent(context, threats.class);
                        startActivity(threats);
                        break;
                    case 2:
                        Intent report = new Intent(context, report.class);
                        startActivity(report);
                        break;
                    case 3 :
                        Intent guide = new Intent(context, Guideline.class);
                        startActivity(guide);
                        break;
                    case 4:
                        Intent expert = new Intent(context, consult_expert.class);
                        startActivity(expert);
                        break;
                    case 5:
                        Intent cyber_cell = new Intent(context, cyber_cells.class);
                        startActivity(cyber_cell);
                        break;
                }
            }
        });

        return rootView;
    }

    private class MyAdapter extends ArrayAdapter<homeList> {
        private List<homeList> list;
        public MyAdapter(List<homeList> list){
            super(context, R.layout.main_list, list);
            this.list = list;
        }
        @Override
        public View getView(int position, View view, ViewGroup viewgroup){
            View itemview = view;
            if (itemview == null){
                itemview = inflater.inflate(R.layout.main_list, viewgroup, false);
            }

            TextView main = (TextView)itemview.findViewById(R.id.mainTitle);
            TextView sub = (TextView)itemview.findViewById(R.id.mainSub);
            LinearLayout back = (LinearLayout)itemview.findViewById(R.id.background);
            ImageButton image = (ImageButton)itemview.findViewById(R.id.image);

            main.setText(list.get(position).mainTitle);
            sub.setText(list.get(position).subTitle);
            image.setImageResource(list.get(position).image);

            return itemview;
        }
    }

    private class homeList{
        public String mainTitle;
        public String subTitle;
        public int image;
        public homeList(String m, String s, int i){
            mainTitle = m;
            subTitle = s;
            image = i;
        }
    }

}
