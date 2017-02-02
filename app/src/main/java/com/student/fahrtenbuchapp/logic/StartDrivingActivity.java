package com.student.fahrtenbuchapp.logic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.student.fahrtenbuchapp.R;
import com.student.fahrtenbuchapp.login.LoginActivity;
import com.student.fahrtenbuchapp.models.AddressStart;
import com.student.fahrtenbuchapp.models.AddressStop;
import com.student.fahrtenbuchapp.models.Car;
import com.student.fahrtenbuchapp.models.Drive;
import com.student.fahrtenbuchapp.models.LocationStart;
import com.student.fahrtenbuchapp.models.LocationStop;
import com.student.fahrtenbuchapp.models.Notes;
import com.student.fahrtenbuchapp.models.PointOfInterests;
import com.student.fahrtenbuchapp.models.User;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class StartDrivingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvDriverName, textViewDateAndTime;
    private TextView tvStartPosition, tvDestinationPosition, textViewSetStartTime, textViewSetEndTime;
    private TextView tvStart, tvDestination, tvKiloElapsed;
    private Spinner spinnerStart, spinnerStop;
    private EditText etKiloStart, etKiloEnd, etNotes, etSetKwhStart, etSetKwhStop;
    private Switch switchStart, switchDestination;
    private Button startButton, stopButton;
    private String currentTimeString;
    private String startTimeNowAsISO;
    private String stopTimeNowAsISO;
    private String poiStart;
    private String poiStop;
    private String kwhStart, kwhStop;

    private AddressStart myStartAddress;
    private AddressStop myStopAddress;
    private LocationStart startCoord;
    private LocationStop endCoord;
    private Date startDate, stopDate;
    private GPSTracker gps;

    private List<String> arrayList;
    private ArrayAdapter<String> dataAdapter;

    long date = System.currentTimeMillis();

    private Realm realm;

    public static final String Name = "nameKey";
    static final int PICK_START_ADDRESS_REQUEST = 1;  // The request code
    static final int PICK_STOP_ADDRESS_REQUEST = 2;  // The request code

    private boolean ifStartAddress;
    private boolean ifStopAddress;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_start_driving);

        progressDialog = new ProgressDialog(this);

        //mRequestingLocationUpdates = false;
        //mLastUpdateTime = "";

        /*session = new Session(this);
        if (!session.loggedin()) {
            logout();
        }*/

        DigitalClock digital = (DigitalClock) findViewById(R.id.digital_clock);

        tvDriverName = (TextView) findViewById(R.id.nameTextView);

        realm = Realm.getDefaultInstance();
        final RealmResults<User> userRealmResults = realm.where(User.class).findAll();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString(Name, "");
        if(!name.equalsIgnoreCase(""))
        {
            for(int i=0; i<userRealmResults.size(); i++)
            {
                if(userRealmResults.get(i).getUsername().equals(name))
                {
                    tvDriverName.setText(userRealmResults.get(i).getFirstname() +
                            " " + userRealmResults.get(i).getLastname());
                }
            }
        }


        tvStart               = (TextView) findViewById(R.id.tvStart);
        tvDestination         = (TextView) findViewById(R.id.tvDestination);
        tvKiloElapsed         = (TextView) findViewById(R.id.tvSetKiloElapsed);
        textViewDateAndTime   = (TextView) findViewById(R.id.tvSetDate);
        textViewSetStartTime  = (TextView) findViewById(R.id.tvSetStartTime);
        textViewSetEndTime    = (TextView) findViewById(R.id.tvSetEndTime);
        tvStartPosition       = (TextView) findViewById(R.id.tvStartPosition);
        tvDestinationPosition = (TextView) findViewById(R.id.tvDestinationPosition);

        etKiloStart   = (EditText) findViewById(R.id.etSetKiloStart);
        etKiloEnd     = (EditText) findViewById(R.id.etSetKiloStop);
        etNotes       = (EditText) findViewById(R.id.etNotes);
        etSetKwhStart = (EditText) findViewById(R.id.etSetKwhStart);
        etSetKwhStop  = (EditText) findViewById(R.id.etSetKwhStop);

        switchStart         = (Switch) findViewById(R.id.switchStart);
        switchDestination   = (Switch) findViewById(R.id.switchDestination);

        spinnerStart = (Spinner) findViewById(R.id.spinnerStartPosition);
        spinnerStop  = (Spinner) findViewById(R.id.spinnerStopPosition);




        RealmResults<PointOfInterests> pointOfInterests = realm.where(PointOfInterests.class).findAll();
        arrayList = new ArrayList<>();

        if(!pointOfInterests.isEmpty())
        {
            for (int i = 0; i < pointOfInterests.size(); i++) {
                arrayList.add(pointOfInterests.get(i).getPoi());
            }
        }

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arrayList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        // attaching data adapter to spinner
        spinnerStart.setAdapter(dataAdapter);
        spinnerStop.setAdapter(dataAdapter);

        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView)view).setText(null);

                String item = parent.getItemAtPosition(position).toString();
                tvStartPosition.setText(item);

                String[] lines = item.split("\\r?\\n");

                myStartAddress = new AddressStart();
                myStartAddress.setStreet(lines[0]);
                myStartAddress.setZip(lines[1]);
                myStartAddress.setCity(lines[2]);
                myStartAddress.setCountry("Deutschland");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvStartPosition.setText("");
            }
        });

        spinnerStop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView)view).setText(null);

                String item = parent.getItemAtPosition(position).toString();
                tvDestinationPosition.setText(item);

                String[] lines = item.split("\\r?\\n");

                myStopAddress = new AddressStop();
                myStopAddress.setStreet(lines[0]);
                myStopAddress.setZip(lines[1]);
                myStopAddress.setCity(lines[2]);
                myStopAddress.setCountry("Deutschland");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvDestinationPosition.setText("");
            }
        });

        switchStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(tvStartPosition.getText() != null)
                    {
                        poiStart = tvStartPosition.getText().toString();

                        if(!arrayList.contains(tvStartPosition.getText().toString())) {
                            arrayList.add(tvStartPosition.getText().toString());

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {

                                    PointOfInterests poi = realm.createObject(PointOfInterests.class);
                                    poi.setPoi(tvStartPosition.getText().toString());
                                }
                            });
                        }
                    }

                    if(tvStartPosition.getText() == null)
                    {
                        switchStart.setChecked(false);

                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toastRelativeLayout));

                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setView(layout);
                        toast.setText("Leeres Eingabfeld!");
                        toast.show();
                    }
                }

                if(!isChecked)
                {
                    if(tvStartPosition.getText() != null) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(StartDrivingActivity.this);
                        builder.setTitle("POI löschen?").setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                arrayList.remove(tvStartPosition.getText().toString());

                                final RealmResults<PointOfInterests> pois = realm.where(PointOfInterests.class).findAll();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {

                                        for(int i=0; i<pois.size(); i++)
                                        {
                                            if(pois.get(i).getPoi().equals(tvStartPosition.getText().toString()))
                                            {
                                                pois.get(i).deleteFromRealm();
                                            }
                                        }
                                    }
                                });

                            }
                        }).setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
        });

        switchDestination.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(tvDestinationPosition.getText() != null)
                    {
                        poiStop = tvDestinationPosition.getText().toString();

                        if(!arrayList.contains(tvDestinationPosition.getText().toString())) {
                            arrayList.add(tvDestinationPosition.getText().toString());

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {

                                    PointOfInterests poi = realm.createObject(PointOfInterests.class);
                                    poi.setPoi(tvDestinationPosition.getText().toString());
                                }
                            });
                        }
                    }

                    if(tvDestinationPosition.getText() == null)
                    {
                        switchDestination.setChecked(false);

                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toastRelativeLayout));

                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setView(layout);
                        toast.setText("Leeres Eingabfeld!");
                        toast.show();
                    }
                }

                if(!isChecked)
                {
                    if(tvDestinationPosition.getText() != null)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StartDrivingActivity.this);
                        builder.setTitle("POI löschen?").setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                arrayList.remove(tvDestinationPosition.getText().toString());

                                final RealmResults<PointOfInterests> pois = realm.where(PointOfInterests.class).findAll();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {

                                        for(int i=0; i<pois.size(); i++)
                                        {
                                            if(pois.get(i).getPoi().equals(tvDestinationPosition.getText().toString()))
                                            {
                                                pois.get(i).deleteFromRealm();
                                            }
                                        }
                                    }
                                });

                            }
                        }).setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
        });


        //TimeZone tz = TimeZone.getTimeZone("CET");
        //DateFormat df = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSSZ"); // Quoted "Z" to indicate CET, no timezone offset
        //df.setTimeZone(tz);
        //String nowAsISO = df.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = sdf.format(date);
        textViewDateAndTime.setText(dateString);

        startButton = (Button) findViewById(R.id.buttonStart);
        stopButton = (Button) findViewById(R.id.buttonStop);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        tvStart.setOnClickListener(this);
        tvDestination.setOnClickListener(this);





        // new JSONTaskUser().execute();

        /*mLocationRequest = LocationRequest.create();
        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else {
            Toast.makeText(this, "Not connected to Google Services", Toast.LENGTH_SHORT).show();
        }*/

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.buttonStart:

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        AddressStart mAddress = realm.createObject(AddressStart.class);

                        mAddress.setStreet(myStartAddress.getStreet());
                        mAddress.setZip(myStartAddress.getZip());
                        mAddress.setCity(myStartAddress.getCity());
                        mAddress.setCountry(myStartAddress.getCountry());
                    }
                });

                final String stringKiloStart = etKiloStart.getText().toString();
                final String startAddress = tvStartPosition.getText().toString();

                kwhStart = etSetKwhStart.getText().toString();

                if(stringKiloStart.trim().length() == 0)
                {
                    etKiloStart.setError("Bitte Wert eingeben");
                }
                else if(startAddress.trim().length() == 0)
                {
                    tvStartPosition.setError("Bitte Adresse eingeben");
                }
                else if(kwhStart.trim().length() == 0)
                {
                    etSetKwhStart.setError("Bitte Wert eingeben");
                }
                else
                {
                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
                    LayoutInflater inflater = this.getLayoutInflater();

                    myBuilder.setView(inflater.inflate(R.layout.dialog_layout_new_drive, null))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog myDialog = myBuilder.create();
                    myDialog.show();


                    SimpleDateFormat startingTime = new SimpleDateFormat("HH:mm", Locale.GERMAN);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    TimeZone timeZone = TimeZone.getTimeZone("CET");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSSZ");
                    dateFormat.setTimeZone(timeZone);
                    startTimeNowAsISO = dateFormat.format(new Date());

                    try {
                       startDate = dateFormat.parse(startTimeNowAsISO);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //return number of milliseconds since January 1, 1970, 00:00:00 GMT
                    System.out.println(timestamp.getTime());

                    //format timestamp
                    currentTimeString = startingTime.format(timestamp);

                    //startingTime.setTimeZone(TimeZone.getTimeZone("CET"));
                    //currentTimeString = startingTime.format(date);
                    textViewSetStartTime.setText(currentTimeString);

                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);

                    gps = new GPSTracker(StartDrivingActivity.this);

                    if(gps.canGetLocation())
                    {
                        final double latitude = gps.getLatitude();
                        final double longitude = gps.getLongitude();

                        startCoord = new LocationStart();

                        startCoord.setLatitude(String.valueOf(latitude));
                        startCoord.setLongitude(String.valueOf(longitude));

                        Realm realm = Realm.getDefaultInstance();

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                LocationStart location = realm.createObject(LocationStart.class);

                                location.setLongitude(startCoord.getLongitude());
                                location.setLatitude(startCoord.getLatitude());
                            }
                        });
                    }
                    else
                    {
                        gps.showSettingsAlert();
                    }

                }

                break;


            case R.id.buttonStop:

                //stopLocationUpdates();

                String stringKiloEnd = etKiloEnd.getText().toString();
                String stopAddress = tvDestinationPosition.getText().toString();

                kwhStop = etSetKwhStop.getText().toString();

                if(stringKiloEnd.trim().length() == 0)
                {
                    etKiloEnd.setError("Bitte Wert eingeben");
                }
                else if(stopAddress.trim().length() == 0)
                {
                    tvDestinationPosition.setError("Bitte Adresse eingeben");
                }
                else if(kwhStop.trim().length() == 0)
                {
                    etSetKwhStop.setError("Bitte Wert eingeben");
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartDrivingActivity.this);
                    builder.setTitle("Fahrt beenden?").setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // GPS koordinaten holen von der Klasse GPSTracker
                            gps = new GPSTracker(StartDrivingActivity.this);

                            if(gps.canGetLocation())
                            {
                                final double latitude = gps.getLatitude();
                                final double longitude = gps.getLongitude();

                                endCoord = new LocationStop();
                                endCoord.setLatitude(String.valueOf(latitude));
                                endCoord.setLongitude(String.valueOf(longitude));

                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {

                                        LocationStop locationStop = realm.createObject(LocationStop.class);

                                        locationStop.setLatitude(endCoord.getLatitude());
                                        locationStop.setLongitude(endCoord.getLongitude());
                                    }
                                });
                            }
                            else
                            {
                                gps.showSettingsAlert();
                            }


                            stopButton.setEnabled(false);
                            startButton.setEnabled(true);

                            SimpleDateFormat startingTime = new SimpleDateFormat("HH:mm", Locale.GERMAN);
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            TimeZone timeZone = TimeZone.getTimeZone("CET");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sssZ");
                            dateFormat.setTimeZone(timeZone);
                            stopTimeNowAsISO = dateFormat.format(new Date());
                            try {
                                stopDate = dateFormat.parse(stopTimeNowAsISO);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //return number of milliseconds since January 1, 1970, 00:00:00 GMT
                            System.out.println(timestamp.getTime());

                            //format timestamp
                            currentTimeString = startingTime.format(timestamp);

                            //startingTime.setTimeZone(TimeZone.getTimeZone("CET"));
                            //currentTimeString = startingTime.format(date);
                            textViewSetEndTime.setText(currentTimeString);

                            //tvLatLonStop.setText(stopLocation.getLatitude() + "\n" + stopLocation.getLongitude());

                            final int ende = Integer.parseInt(etKiloEnd.getText().toString());
                            final int start = Integer.parseInt(etKiloStart.getText().toString());
                            int distanz = ende - start;

                            tvKiloElapsed.setText(String.valueOf(distanz));

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {

                                    AddressStop mAddress2 = realm.createObject(AddressStop.class);

                                    mAddress2.setStreet(myStopAddress.getStreet());
                                    mAddress2.setZip(myStopAddress.getZip());
                                    mAddress2.setCity(myStopAddress.getCity());
                                    mAddress2.setCountry(myStopAddress.getCountry());
                                }
                            });

                            final Intent intent = new Intent(StartDrivingActivity.this, SummaryActivity.class);

                            RealmQuery<User> queryUser = realm.where(User.class);

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(StartDrivingActivity.this);
                            String name = preferences.getString(Name, "");
                            if(!name.equalsIgnoreCase(""))
                            {
                                // get User
                                queryUser.equalTo("username", name);
                                RealmResults<User> users = queryUser.findAll();
                                final User currentUser = users.where().equalTo("username", name).findFirst();


                                // get Car
                                final Car car = realm.where(Car.class).findFirst();


                                // get LocationStart
                                RealmResults<LocationStart> locationStartRealmResults =
                                        realm
                                                .where(LocationStart.class)
                                                .findAll();

                                final LocationStart finalLocationStart =
                                        locationStartRealmResults
                                                .where()
                                                .equalTo("latitude", startCoord.getLatitude())
                                                .equalTo("longitude", startCoord.getLongitude())
                                                .findFirst();


                                // get LocationStop
                                final RealmResults<LocationStop> locationStopRealmResults = realm
                                        .where(LocationStop.class)
                                        .findAll();

                                final LocationStop finalLocationStop = locationStopRealmResults.where()
                                        .equalTo("latitude", endCoord.getLatitude())
                                        .equalTo("longitude", endCoord.getLongitude())
                                        .findFirst();




                                // get AddressStart
                                RealmResults<AddressStart> addressStartRealmResults =
                                        realm
                                                .where(AddressStart.class)
                                                .findAll();

                                final AddressStart finalAddressStart =
                                        addressStartRealmResults
                                                .where()
                                                .equalTo("street", myStartAddress.getStreet())
                                                .equalTo("zip", myStartAddress.getZip())
                                                .equalTo("city", myStartAddress.getCity())
                                                .equalTo("country", myStartAddress.getCountry())
                                                .findFirst();


                                // get AddressStop
                                RealmResults<AddressStop> addressStopRealmResults =
                                        realm
                                                .where(AddressStop.class)
                                                .findAll();

                                final AddressStop finalAddressStop =
                                        addressStopRealmResults
                                                .where()
                                                .equalTo("street", myStopAddress.getStreet())
                                                .equalTo("zip", myStopAddress.getZip())
                                                .equalTo("city", myStopAddress.getCity())
                                                .equalTo("country", myStopAddress.getCountry())
                                                .findFirst();


                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {

                                        final Drive drive = realm.createObject(Drive.class);

                                        drive.setUser(currentUser.get_id());
                                        drive.setCar(car.get_id());
                                        drive.setStartDate(startDate);
                                        drive.setEndDate(stopDate);
                                        drive.setStartCoord(finalLocationStart);
                                        drive.setEndCoord(finalLocationStop);
                                        drive.setStartAddress(finalAddressStart);
                                        drive.setEndAddress(finalAddressStop);
                                        drive.setStartPOI(poiStart);
                                        drive.setEndPOI(poiStop);
                                        drive.setStartMileage(Integer.parseInt(etKiloStart.getText().toString()));
                                        drive.setEndMileage(Integer.parseInt(etKiloEnd.getText().toString()));


                                        double kwhEnd = Double.parseDouble(etSetKwhStop.getText().toString());
                                        double kwhSta = Double.parseDouble(etSetKwhStart.getText().toString());
                                        double kwhFinal = kwhEnd - kwhSta;

                                        drive.setUsedkWh(kwhFinal);


                                        SimpleDateFormat finalStartingTime = new SimpleDateFormat("HH:mm", Locale.GERMAN);
                                        SimpleDateFormat finalStopingTime = new SimpleDateFormat("HH:mm", Locale.GERMAN);

                                        intent.putExtra("name", currentUser.getLastname());
                                        intent.putExtra("surname", currentUser.getFirstname());
                                        intent.putExtra("carVendor", car.getVendor());
                                        intent.putExtra("carModel", car.getModel());
                                        intent.putExtra("streetFrom", finalAddressStart.getStreet());
                                        intent.putExtra("zipFrom", finalAddressStart.getZip());
                                        intent.putExtra("cityFrom", finalAddressStart.getCity());
                                        intent.putExtra("streetTo", finalAddressStop.getStreet());
                                        intent.putExtra("zipTo", finalAddressStop.getZip());
                                        intent.putExtra("cityTo", finalAddressStop.getCity());
                                        intent.putExtra("startDate", finalStartingTime.format(startDate));
                                        intent.putExtra("stopDate", finalStopingTime.format(stopDate));
                                        intent.putExtra("kmStart", etKiloStart.getText().toString());
                                        intent.putExtra("kmStop", etKiloEnd.getText().toString());
                                        intent.putExtra("kwh", kwhFinal);
                                        intent.putExtra("notes", etNotes.getText().toString());


                                        String finalNotes = etNotes.getText().toString();
                                        if(finalNotes != null)
                                        {
                                            Notes notes = realm.createObject(Notes.class);
                                            notes.setNotes(finalNotes);
                                            notes.setDrive(drive);
                                        }
                                    }
                                });
                            }

                            android.os.Handler handler = new android.os.Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Toast toast = Toast.makeText(StartDrivingActivity.this, "Daten konnten nicht übermittelt werden", Toast.LENGTH_SHORT);
                                    View toastView = toast.getView(); //This'll return the default View of the Toast.

                                    /* And now you can get the TextView of the default View of the Toast. */
                                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextSize(25);
                                    toastMessage.setTextColor(Color.WHITE);
                                    toastMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastMessage.setCompoundDrawablePadding(16);
                                    toastView.setBackgroundColor(getResources().getColor(R.color.toast_color));
                                    toast.show();

                                    startActivity(intent);
                                    overridePendingTransition(R.anim.anim_slide_in_left, 0);
                                    finish();
                                }
                            }, 1000);

                        }
                    }).setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                break;


            case R.id.tvStart:
                ifStartAddress = true;
                Intent intent1 = new Intent(StartDrivingActivity.this, AddressActivity.class);
                intent1.putExtra("ifStartAddress", ifStartAddress);
                startActivityForResult(intent1, PICK_START_ADDRESS_REQUEST);
                break;

            case R.id.tvDestination:
                ifStopAddress = true;
                Intent intent2 = new Intent(StartDrivingActivity.this, AddressActivity.class);
                intent2.putExtra("ifStopAddress", ifStopAddress);
                startActivityForResult(intent2, PICK_STOP_ADDRESS_REQUEST);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case PICK_START_ADDRESS_REQUEST:

                final String city = data.getStringExtra("city");
                final String zip = data.getStringExtra("zip");
                final String street = data.getStringExtra("street");

                myStartAddress = new AddressStart();

                if(city != null)
                    myStartAddress.setCity(city);
                else
                    myStartAddress.setCity("");

                if(zip != null)
                    myStartAddress.setZip(zip);
                else
                    myStartAddress.setZip("");

                if(street != null)
                    myStartAddress.setStreet(street);
                else
                    myStartAddress.setStreet("");

                myStartAddress.setCountry("Deutschland");

                tvStartPosition.setText(street + "\n" +
                                zip + "\n" +
                                city);

                if(resultCode == Activity.RESULT_CANCELED)
                {
                    tvStartPosition.setText("");
                }

                break;


            case PICK_STOP_ADDRESS_REQUEST:

                String city2 = data.getStringExtra("city");
                String zip2 = data.getStringExtra("zip");
                String street2 = data.getStringExtra("street");

                myStopAddress = new AddressStop();

                if(city2 != null)
                    myStopAddress.setCity(city2);
                else
                    myStopAddress.setCity("");

                if(zip2 != null)
                    myStopAddress.setZip(zip2);
                else
                    myStopAddress.setZip("");

                if(street2 != null)
                    myStopAddress.setStreet(street2);
                else
                    myStopAddress.setStreet("");

                myStopAddress.setCountry("Deutschland");

                tvDestinationPosition.setText(street2 + "\n" +
                        zip2 + "\n" +
                        city2);

                if(resultCode == Activity.RESULT_CANCELED)
                {
                    tvDestinationPosition.setText("");
                }

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

    private void logout() {
        finish();
        startActivity(new Intent(StartDrivingActivity.this, LoginActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;

            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(StartDrivingActivity.this);
                builder.setTitle("Beenden?").setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }
                }).setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(StartDrivingActivity.this);
        builder.setTitle("Beenden?").setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                overridePendingTransition(R.anim.zoom_out, 0);
                System.exit(0);
            }
        }).setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
    }


    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }

    /*@Override
    public void onConnected(@Nullable Bundle bundle) {

        Toast.makeText(this, "Successfully connected to Google", Toast.LENGTH_SHORT).show();

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation != null) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mEndLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mEndLocation != null) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(mEndLocation.getLatitude(), mEndLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                addressEnd = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                cityEnd = addresses.get(0).getLocality();
                countryEnd = addresses.get(0).getCountryName();
                postalCodeEnd = addresses.get(0).getPostalCode();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    *//**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     *//*
    public void startUpdatesButtonHandler(View view) {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        }
    }

    *//**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     *//*
    public void stopUpdatesButtonHandler(View view) {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            stopLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mEndLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLatLong();
    }

    *//**
     * Requests location updates from the FusedLocationApi.
     *//*
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    *//**
     * Updates the latitude, the longitude, and the last location time in the UI.
     *//*
    private void updateLatLong() {
        mCurrentLatitude = mCurrentLocation.getLatitude();
        mCurrentLongitude = mCurrentLocation.getLongitude();

        mEndLatitude = mEndLocation.getLatitude();
        mEndLongitude = mEndLocation.getLongitude();
    }


    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }*/
}
