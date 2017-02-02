package com.student.fahrtenbuchapp.logic;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.student.fahrtenbuchapp.R;

import java.text.SimpleDateFormat;

import io.realm.Realm;


public class SummaryActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvName, tvCar, tvFrom, tvTo, tvDate, tvKm, tvCurrentDate, tvKwh, tvNotes;
    private Button buttonNewDrive, buttonExit;
    private ProgressDialog progressDialog;
    private Realm realm;

    private String name, surname;
    private String carVendor, carModel;
    private String streetFrom, zipFrom, cityFrom;
    private String streetTo, zipTo, cityTo;
    private String startDate, stopDate;
    private String kmStart, kmStop;
    private String kWh;
    private String notes;

    private long date = System.currentTimeMillis();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        buttonExit      = (Button) findViewById(R.id.buttonExit);
        buttonNewDrive  = (Button) findViewById(R.id.buttonNewDrive);

        buttonExit.setOnClickListener(this);
        buttonNewDrive.setOnClickListener(this);

        tvName  = (TextView) findViewById(R.id.tvSummaryDriver);
        tvCar   = (TextView) findViewById(R.id.tvSummaryCar);
        tvFrom  = (TextView) findViewById(R.id.tvSummaryStartAddress);
        tvTo    = (TextView) findViewById(R.id.tvSummaryStopAddress);
        tvDate  = (TextView) findViewById(R.id.tvSummaryDate);
        tvKm    = (TextView) findViewById(R.id.tvSummaryKm);
        tvKwh   = (TextView) findViewById(R.id.tvkWh);
        tvNotes = (TextView) findViewById(R.id.tvNotes);

        tvCurrentDate = (TextView) findViewById(R.id.digital_clock_2);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_layout_summary, null))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = sdf.format(date);


        Intent intent = getIntent();

        name        = intent.getStringExtra("name");
        surname     = intent.getStringExtra("surname");
        carVendor   = intent.getStringExtra("carVendor");
        carModel    = intent.getStringExtra("carModel");
        streetFrom  = intent.getStringExtra("streetFrom");
        zipFrom     = intent.getStringExtra("zipFrom");
        cityFrom    = intent.getStringExtra("cityFrom");
        streetTo    = intent.getStringExtra("streetTo");
        zipTo       = intent.getStringExtra("zipTo");
        cityTo      = intent.getStringExtra("cityTo");
        startDate   = intent.getStringExtra("startDate");
        stopDate    = intent.getStringExtra("stopDate");
        kmStart     = intent.getStringExtra("kmStart");
        kmStop      = intent.getStringExtra("kmStop");
        kWh         = intent.getStringExtra("kwh");
        notes       = intent.getStringExtra("notes");

        tvName.setText(surname + " " + name);
        tvCar.setText(carVendor + " " + carModel);
        tvFrom.setText(streetFrom + "\n" + zipFrom + "\n" + cityFrom);
        tvTo.setText(streetTo + "\n" + zipTo + "\n" + cityTo);
        tvDate.setText(startDate + " - " + stopDate);
        tvKwh.setText(kWh);
        tvKm.setText(kmStop);

        if(notes != null) {
            tvNotes.setText(notes);
            tvNotes.setMovementMethod(new ScrollingMovementMethod());
        }
        else
            tvNotes.setText("");

        tvCurrentDate.setText(dateString);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.buttonNewDrive:
                Intent intent = new Intent(SummaryActivity.this, StartDrivingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_slide_out_right, 0);
                break;

            case R.id.buttonExit:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.dialog_layout_exit, null))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finishAffinity();
                                overridePendingTransition(R.anim.zoom_out, 0);
                                System.exit(0);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
    }
}
