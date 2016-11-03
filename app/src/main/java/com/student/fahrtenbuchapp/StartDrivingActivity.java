package com.student.fahrtenbuchapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jibble.simpleftp.SimpleFTP;

import java.text.SimpleDateFormat;

public class StartDrivingActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "name_key";
    public static final String Surname = "surname_key";

    private TextView textViewDriverName, textViewDateAndTime;
    private TextView textViewSetStartTime, textViewSetEndTime;
    private TextView textViewDate;

    private Button startButton, stopButton;

    private Chronometer simpleChronometer;

    private SimpleDateFormat startingTime;
    private String currentTimeString;

    private long time = 0;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_driving);

        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }

        simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);

        textViewDriverName = (TextView) findViewById(R.id.nameTextView);
        textViewDateAndTime = (TextView) findViewById(R.id.tvSetDate);
        textViewSetStartTime = (TextView) findViewById(R.id.tvSetStartTime);
        textViewSetEndTime = (TextView) findViewById(R.id.tvSetEndTime);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String savedName = sharedPreferences.getString(Name, "");
        String savedSurname = sharedPreferences.getString(Surname, "");

        textViewDriverName.setText(savedSurname + " " + savedName);

        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm");
        String dateString = sdf.format(date);
        textViewDateAndTime.setText(dateString);

        startingTime = new SimpleDateFormat("h:mm");
        currentTimeString = startingTime.format(date);
        textViewSetStartTime.setText(currentTimeString);

        startButton = (Button) findViewById(R.id.buttonStart);
        stopButton = (Button) findViewById(R.id.buttonStop);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.buttonStart:
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                simpleChronometer.setBase(SystemClock.elapsedRealtime() + time);
                simpleChronometer.start();
                break;

            case R.id.buttonStop:
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
                time = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
                simpleChronometer.stop();

                long date = System.currentTimeMillis();
                startingTime = new SimpleDateFormat("h:mm");
                currentTimeString = startingTime.format(date);
                textViewSetEndTime.setText(currentTimeString);

                if(isConnected())
                    Toast.makeText(getApplicationContext(), "You are connected", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "You are NOT connected", Toast.LENGTH_SHORT).show();

                break;
        }
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

    private void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(StartDrivingActivity.this,LoginActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:
                logout();
                return true;

            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(StartDrivingActivity.this);
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


    public boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
