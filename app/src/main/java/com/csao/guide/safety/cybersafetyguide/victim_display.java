package com.android.guide.safety.cybersafetyguide;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class victim_display extends AppCompatActivity {

    Thread thread = new Thread();

    @Override
    public void onBackPressed() {
        thread.interrupt();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victim_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        CustomGauge gauge =(CustomGauge)findViewById(R.id.gauge);
        gauge.setValue(score);
        ((TextView)findViewById(R.id.gaugeText)).setText(String.valueOf(score));

        final Button button = (Button)findViewById(R.id.send);

        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("Sending...");
                button.setEnabled(false);
                button.setBackgroundColor(Color.parseColor("#FF8B8888"));
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<String> list = intent.getStringArrayListExtra("probs");
                            String body = "";
                            for (int i =0; i<list.size(); i++){
                                body += list.get(i)+"\n";
                            }
                            GMailSender sender = new GMailSender("no.reply.csao@gmail.com", "securepass@csao2016");
                            sender.sendMail("Checking for", body, "no.reply.csao@gmail.com", "ask.csao@gmail.com");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(victim_display.this, R.style.myDialog));
                                    builder.setTitle("Request Sent");
                                    builder.setMessage("Our Experts will contact you within 48 hours to resolve your problem");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            victim_display.this.finish();
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });
                        } catch (Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(victim_display.this, "Unable To Send", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setText("CONTACT CYBER EXPERT");
                                button.setEnabled(true);
                                button.setBackgroundColor(Color.parseColor("#3fb568"));
                            }
                        });
                    }
                });
                thread.start();

            }
        });


    }

}
