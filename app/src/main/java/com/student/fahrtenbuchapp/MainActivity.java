package com.student.fahrtenbuchapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Session session;

    private Button bookAudi, bookBmw, bookVw, bookOpel;
    private ImageButton iButtonAudi, iButtonBmw, iButtonVw, iButtonOpel;
    private CardView cardViewAudi, cardViewBmw, cardViewVw, cardViewOpel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();

        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }

        bookAudi = (Button) findViewById(R.id.bookingButtonAudi);
        bookBmw = (Button) findViewById(R.id.bookingButtonBmw);
        bookVw = (Button) findViewById(R.id.bookingButtonVw);
        bookOpel = (Button) findViewById(R.id.bookingButtonOpel);

        bookAudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });

        bookBmw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });

        bookVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });

        bookOpel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });


        final Animation move_out_1 = AnimationUtils.loadAnimation(this, R.anim.move_out);
        final Animation move_out_2 = AnimationUtils.loadAnimation(this, R.anim.move_out);
        final Animation move_out_3 = AnimationUtils.loadAnimation(this, R.anim.move_out);
        final Animation move_out_4 = AnimationUtils.loadAnimation(this, R.anim.move_out);

        move_out_1.setDuration(500);
        move_out_2.setDuration(700);
        move_out_3.setDuration(900);
        move_out_4.setDuration(1100);

        cardViewAudi = (CardView) findViewById(R.id.cardViewAudi);
        cardViewBmw = (CardView) findViewById(R.id.cardViewBmw);
        cardViewVw = (CardView) findViewById(R.id.cardViewVw);
        cardViewOpel = (CardView) findViewById(R.id.cardViewOpel);

        iButtonAudi = (ImageButton) findViewById(R.id.imageButtonAudi);
        iButtonBmw = (ImageButton) findViewById(R.id.imageButtonBmw);
        iButtonVw = (ImageButton) findViewById(R.id.imageButtonVw);
        iButtonOpel = (ImageButton) findViewById(R.id.imageButtonOpel);

        iButtonAudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardViewAudi.startAnimation(move_out_1);
                cardViewBmw.startAnimation(move_out_2);
                cardViewVw.startAnimation(move_out_3);
                cardViewOpel.startAnimation(move_out_4);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(MainActivity.this, StartDrivingActivity.class));
                    }
                }, 400);
            }
        });

        iButtonBmw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardViewAudi.startAnimation(move_out_1);
                cardViewBmw.startAnimation(move_out_2);
                cardViewVw.startAnimation(move_out_3);
                cardViewOpel.startAnimation(move_out_4);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(MainActivity.this, StartDrivingActivity.class));
                    }
                }, 400);
            }
        });

        iButtonVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardViewAudi.startAnimation(move_out_1);
                cardViewBmw.startAnimation(move_out_2);
                cardViewVw.startAnimation(move_out_3);
                cardViewOpel.startAnimation(move_out_4);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(MainActivity.this, StartDrivingActivity.class));
                    }
                }, 400);
            }
        });

        iButtonOpel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardViewAudi.startAnimation(move_out_1);
                cardViewBmw.startAnimation(move_out_2);
                cardViewVw.startAnimation(move_out_3);
                cardViewOpel.startAnimation(move_out_4);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(MainActivity.this, StartDrivingActivity.class));
                    }
                }, 400);
            }
        });
    }


    public void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            int end = spanString.length();
            spanString.setSpan(new RelativeSizeSpan(1.5f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(spanString);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:
                logout();
                return true;

            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Exit App?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
