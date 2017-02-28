package com.student.fahrtenbuchapp.logic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.student.fahrtenbuchapp.R;
import com.student.fahrtenbuchapp.dataSync.RestClient;
import com.student.fahrtenbuchapp.models.AddressStart;
import com.student.fahrtenbuchapp.models.AddressStop;
import com.student.fahrtenbuchapp.models.Drive;
import com.student.fahrtenbuchapp.models.LocationStart;
import com.student.fahrtenbuchapp.models.LocationStop;
import com.student.fahrtenbuchapp.models.Token;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;


public class SummaryActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvName, tvCar, tvFrom, tvTo, tvDate, tvKm, tvCurrentDate, tvKwh, tvNotes;
    private Button buttonNewDrive, buttonExit;

    private Realm realm;
    private RestClient restClient = new RestClient();

    private String name, surname;
    private String carVendor, carModel;
    private String streetFrom, zipFrom, cityFrom;
    private String streetTo, zipTo, cityTo;
    private String startDate, stopDate;
    private String kmStart, kmStop;
    private String kWhUsed;
    private String notes;

    private boolean readyToExit;

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

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button positiveButton = ((android.app.AlertDialog) dialog)
                        .getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextSize(25);
            }
        });

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
        kWhUsed     = intent.getStringExtra("kwhUsed");
        notes       = intent.getStringExtra("notes");

        tvName.setText(surname + " " + name);
        tvCar.setText(carVendor + " " + carModel);
        tvFrom.setText(streetFrom + "\n" + zipFrom + "\n" + cityFrom);
        tvTo.setText(streetTo + "\n" + zipTo + "\n" + cityTo);
        tvDate.setText(startDate + " - " + stopDate);
        tvKwh.setText(kWhUsed);
        tvKm.setText(kmStop);

        if(notes != null) {
            tvNotes.setText(notes);
            tvNotes.setMovementMethod(new ScrollingMovementMethod());
        }
        else
            tvNotes.setText("");

        tvCurrentDate.setText(dateString);


        ISO8601DateFormat iso8601DateFormat = new ISO8601DateFormat();
        String startTimeNowAsISO = iso8601DateFormat.format(new Date());
        try
        {
            Date date = iso8601DateFormat.parse(startTimeNowAsISO);
            System.out.println("Date: " + date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

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

                realm = Realm.getDefaultInstance();

                final Token myToken = realm.where(Token.class).findFirst();
                final RealmResults<Drive> drives = realm.where(Drive.class).findAll();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_layout_exit, null))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                                if (wifi.isConnected()) {

                                    if (!drives.isEmpty()) {

                                        for (int i = 0; i < drives.size(); i++) {
                                            if (drives.get(i).getUser() != null
                                                    && drives.get(i).getCar() != null
                                                    && drives.get(i).getStartDate() != null
                                                    && drives.get(i).getEndDate() != null
                                                    && drives.get(i).getStartCoord() != null
                                                    && drives.get(i).getEndCoord() != null
                                                    && drives.get(i).getStartAddress() != null
                                                    && drives.get(i).getEndAddress() != null
                                                    && drives.get(i).getStartMileage() != null
                                                    && drives.get(i).getEndMileage() != null
                                                    && drives.get(i).getUsedkWh() != null) {
                                                String userID = drives.get(i).getUser();
                                                String carId = drives.get(i).getCar();
                                                String startDate = drives.get(i).getStartDate();
                                                String endDate = drives.get(i).getEndDate();
                                                String startPOI = drives.get(i).getStartPOI();
                                                String endPOI = drives.get(i).getEndPOI();
                                                int startMileage = drives.get(i).getStartMileage();
                                                int endMileage = drives.get(i).getEndMileage();
                                                double usedkWh = drives.get(i).getUsedkWh();

                                                String startCoordLatitude = drives.get(i).getStartCoord().getLatitude();
                                                String startCoordLongitude = drives.get(i).getStartCoord().getLongitude();

                                                String endCoordLatitude = drives.get(i).getEndCoord().getLatitude();
                                                String endCoordLongitude = drives.get(i).getEndCoord().getLongitude();

                                                String startAddressCountry = drives.get(i).getStartAddress().getCountry();
                                                String startAddressCity = drives.get(i).getStartAddress().getCity();
                                                String startAddressZip = drives.get(i).getStartAddress().getZip();
                                                String startAddressStreet = drives.get(i).getStartAddress().getStreet();

                                                String endAddressCountry = drives.get(i).getEndAddress().getCountry();
                                                String endAddressCity = drives.get(i).getEndAddress().getCity();
                                                String endAddressZip = drives.get(i).getEndAddress().getZip();
                                                String endAddressStreet = drives.get(i).getEndAddress().getStreet();

                                                AddressStart addressStart = new AddressStart(startAddressCountry, startAddressCity, startAddressZip, startAddressStreet);
                                                AddressStop addressStop = new AddressStop(endAddressCountry, endAddressCity, endAddressZip, endAddressStreet);
                                                LocationStart locationStart = new LocationStart(startCoordLatitude, startCoordLongitude);
                                                LocationStop locationStop = new LocationStop(endCoordLatitude, endCoordLongitude);

                                                final Drive drive = new Drive(userID, carId, startDate, endDate, locationStart, locationStop,
                                                        addressStart, addressStop, startPOI, endPOI, startMileage, endMileage, usedkWh);


                                                restClient.postNewDrives(getApplicationContext(), myToken, drive);

                                                readyToExit = true;
                                            }
                                        }
                                    }
                                    else
                                    {
                                        finishAffinity();
                                        overridePendingTransition(R.anim.zoom_out, 0);
                                        System.exit(0);
                                    }
                                }
                                else
                                {
                                    finishAffinity();
                                    overridePendingTransition(R.anim.zoom_out, 0);
                                    System.exit(0);
                                }

                                if(readyToExit)
                                {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            finishAffinity();
                                            overridePendingTransition(R.anim.zoom_out, 0);
                                            System.exit(0);
                                        }
                                    }, 2000);
                                }
                            }
                        });

                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {

                        Button positiveButton = ((android.app.AlertDialog) dialog)
                                .getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setTextSize(25);

                        Button negativeButton = ((android.app.AlertDialog) dialog)
                                .getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
                        negativeButton.setTextSize(25);
                    }
                });

                dialog.show();
                break;
        }
    }
}
