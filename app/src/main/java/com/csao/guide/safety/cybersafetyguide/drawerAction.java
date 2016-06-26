package com.android.guide.safety.cybersafetyguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Study Abacus on 06-May-16.
 */
public class  drawerAction implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ActionBar bar;
    static Context context;
    FragmentManager fragmentManager;

    drawerAction(DrawerLayout d, ActionBar a, Context c, FragmentManager f){
        drawer = d;
        bar = a;
        context = c;
        fragmentManager = f;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            bar.setTitle("Home");
            //Intent intent = new Intent(context, MainActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(intent);
            //((Activity)context).finish();
            main_fragment fragment = new main_fragment();
            fragment.add_context(context);
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_dodont) {
            bar.setTitle("Do's & Don't");
            //Intent intent = new Intent(context, dodont.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(intent);
            //((Activity)context).finish();
            dodont_fragment fragment = new dodont_fragment();
            fragment.add_context(context);
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_volunteer){
            bar.setTitle("Be a Volunteer");
            //Intent intent = new Intent(context, volunteer.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(intent);
            //((Activity)context).finish();
            volunteer_fragment fragment = new volunteer_fragment();
            fragment.add_context(context);
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_workshop){
            bar.setTitle("Workshop");
            workshop_fragment fragment = new workshop_fragment();
            fragment.add_context(context);
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_credits){
            bar.setTitle("Credits");
            credit_fragment fragment = new credit_fragment();
            fragment.add_context(context);
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_share){
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Cyber Security Guide");
                String sAux = "\nBe secure and safe online try this Cyber Security App\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                context.startActivity(Intent.createChooser(i, "Choose One"));
            } catch(Exception e) {
            }
        } else if (id == R.id.logout){
            try {
                OutputStream in = context.openFileOutput("secure.db", Context.MODE_PRIVATE);
                in.write("".getBytes());
                Intent intent = new Intent(context, welcome.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            } catch (Exception e){

            }
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
