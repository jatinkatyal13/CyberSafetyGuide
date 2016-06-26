package com.android.guide.safety.cybersafetyguide;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 10/6/16.
 */
public class Fragment3 extends Fragment {
    private static Context context;

    public Fragment3(){

    }

    public void addContext(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.guideline_fragment_3, container, false);

        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);
        MyAdapter adapter = new MyAdapter(getJson("json_guideline_corporate.txt"));
        rv.setAdapter(adapter);

        ((SwipeRefreshLayout)rootView.findViewById(R.id.guideline_refresh_layout_3)).setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        ((SwipeRefreshLayout)rootView.findViewById(R.id.guideline_refresh_layout_3)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final List<String> list = new ArrayList<>();
                        boolean done = false;
                        try {
                            URL url = new URL("http://www.abboniss.com/test/guideline_corporate_get.php");
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStream in = urlConnection.getInputStream();
                            String jsonText = "";
                            int c;
                            while ((c=in.read()) != -1){
                                jsonText += (char)c;
                            }
                            FileOutputStream file = context.openFileOutput("json_guideline_corporate.txt", Context.MODE_PRIVATE);
                            file.write(jsonText.getBytes());
                            JSONObject jsonObject = new JSONObject(jsonText);
                            JSONArray data = jsonObject.getJSONArray("info");
                            for (int i=0; i<data.length(); i++){
                                list.add(String.valueOf(data.get(i)));
                            }
                            done = true;
                        } catch (Exception e){
                            done = false;
                        }
                        final boolean finaldone = done;
                        new Handler(context.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (finaldone){
                                    MyAdapter adapter = new MyAdapter(list);
                                    rv.setAdapter(adapter);
                                } else {
                                    Toast.makeText(context, "Unable to refresh", Toast.LENGTH_SHORT).show();
                                }
                                ((SwipeRefreshLayout)rootView.findViewById(R.id.guideline_refresh_layout_3)).setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    private List<String> getJson(String fileName){
        List<String> emptyList = new ArrayList<>();
        try {
            List<String> list = new ArrayList<>();
            FileInputStream file = context.openFileInput(fileName);
            String JsonText = "";
            int c;
            while ((c = file.read()) != -1){
                JsonText += (char)c;
            }
            JSONObject jsonObject = new JSONObject(JsonText);
            JSONArray data = jsonObject.getJSONArray("info");
            for (int i=0; i<data.length(); i++){
                list.add(String.valueOf(data.get(i)));
            }
            return list;
        } catch (Exception e){
            // some default json encoded data
            List<String> list = new ArrayList<>();
            String JsonText = "{\"info\":[\"Use content filtering software on your PC to protect children from pornography, gambling, hate speech, drugs and alcohol.\",\"There is also software to establish time controls for individual users (for example blocking usage after a particular time at night) and log surfing activities allowing parents to see which site the child has visited. Use this software to keep track of the activities of your children.\",\"Don't delete harmful communications (emails, chat logs, posts etc). These may help provide vital information about the identity of the person behind these.\",\"If you feel any immediate physical danger of bodily harm, call your local police.\"]}";
            try {
                JSONObject jsonObject = new JSONObject(JsonText);
                JSONArray data = jsonObject.getJSONArray("info");
                for (int i=0; i<data.length(); i++){
                    list.add(String.valueOf(data.get(i)));
                }
                FileOutputStream file = context.openFileOutput(fileName, context.MODE_PRIVATE);
                file.write(JsonText.getBytes());
                return list;
            } catch (Exception ex){
                Toast.makeText(context, "Error Occured !", Toast.LENGTH_SHORT).show();
            }
        }
        return emptyList;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<String> data = new ArrayList<>();

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public CardView mCardView;
            public TextView mTextView;
            public MyViewHolder(View v) {
                super(v);

                mCardView = (CardView) v.findViewById(R.id.card_view);
                mTextView = (TextView) v.findViewById(R.id.tv_text);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<String> data) {
            this.data = data;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.guideline_card_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mTextView.setText(data.get(position));
        }


        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
