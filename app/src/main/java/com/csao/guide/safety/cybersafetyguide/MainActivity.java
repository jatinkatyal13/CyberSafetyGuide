package com.android.guide.safety.cybersafetyguide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    static String name;
    static String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent (MainActivity.this, background.class);
        startService(intent);
        Intent firstService = new Intent(MainActivity.this, first.class);
        stopService(firstService);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Home");

        main_fragment fragment = new main_fragment();
        fragment.add_context(getApplicationContext());
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new drawerAction((DrawerLayout) findViewById(R.id.drawer_layout), getSupportActionBar(), this, getSupportFragmentManager()));

        View header = navigationView.getHeaderView(0);

        try {
            InputStream in = openFileInput("secure.db");
            String json = "";
            int c;
            while ((c = in.read()) != -1){
                json += (char)c;
            }
            JSONObject object = new JSONObject(json);
            name = object.getString("name");
            email = object.getString("email");
        } catch (Exception e){
            if (name == null){
                name = "";
            }
            if (email == null){
                email = "";
            }
        }

        ((TextView)header.findViewById(R.id.name)).setText(name);
        ((TextView)header.findViewById(R.id.email)).setText(email);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press Back Again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            Intent intent = new Intent(getApplicationContext(), about_us.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends ArrayAdapter<homeList> {
        private List<homeList> list;
        public MyAdapter(List<homeList> list){
            super(MainActivity.this, R.layout.main_list, list);
            this.list = list;
        }
        @Override
        public View getView(int position, View view, ViewGroup viewgroup){
            View itemview = view;
            if (itemview == null){
                itemview = getLayoutInflater().inflate(R.layout.main_list, viewgroup, false);
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
