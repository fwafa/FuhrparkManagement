package com.student.fahrtenbuchapp.logic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.student.fahrtenbuchapp.R;
import com.student.fahrtenbuchapp.login.LoginActivity;
import com.student.fahrtenbuchapp.login.Session;
import com.student.fahrtenbuchapp.models.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.widget.TextClock.DEFAULT_FORMAT_24_HOUR;

public class StartDrivingActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String MyPREFERENCES = "MyPrefs";

    private TextView tvRoleName, tvDriverName, textViewDateAndTime;
    private TextView textViewSetStartTime, textViewSetEndTime;
    private EditText etStart, etDestination;
    private Spinner spinnerStart, spinnerDestination;
    private Button startButton, stopButton;
    private Chronometer simpleChronometer;
    private SimpleDateFormat startingTime;
    private TextClock textClock;
    private String currentTimeString;
    private String roleName, driverName, savedToken, id;
    private long time = 0;

    private Session session;

    long date = System.currentTimeMillis();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_driving);

        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }

        etStart = (EditText) findViewById(R.id.etStart);
        etDestination = (EditText) findViewById(R.id.etDestination);

        spinnerStart = (Spinner) findViewById(R.id.spinnerStart);
        spinnerDestination = (Spinner) findViewById(R.id.spinnerDestination);

        textClock = (TextClock) findViewById(R.id.textClock);
        simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);

        tvDriverName = (TextView) findViewById(R.id.nameTextView);

        textViewDateAndTime = (TextView) findViewById(R.id.tvSetDate);
        textViewSetStartTime = (TextView) findViewById(R.id.tvSetStartTime);
        textViewSetEndTime = (TextView) findViewById(R.id.tvSetEndTime);


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("FRA-UAS");
        categories.add("Frankfurt Hbf");
        categories.add("Frankfurt Airport");
        categories.add("Palmengarten");
        categories.add("EZB");
        categories.add("Römer");
        categories.add("Senckenberg Museum");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        // attaching data adapter to spinner
        spinnerStart.setAdapter(dataAdapter);
        spinnerDestination.setAdapter(dataAdapter);

        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                etStart.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                etStart.setText("");
            }
        });

        spinnerDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                etDestination.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                etDestination.setText("");
            }
        });


        //TimeZone tz = TimeZone.getTimeZone("CET");
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); // Quoted "Z" to indicate CET, no timezone offset
        //df.setTimeZone(tz);
        //String nowAsISO = df.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = sdf.format(date);
        textViewDateAndTime.setText(dateString);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textClock.setTimeZone("CET");
        }

        startButton = (Button) findViewById(R.id.buttonStart);
        stopButton = (Button) findViewById(R.id.buttonStop);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        // new JSONTaskUser().execute();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.buttonStart:

                startingTime = new SimpleDateFormat("h:mm");
                currentTimeString = startingTime.format(date);
                textViewSetStartTime.setText(currentTimeString);

                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                simpleChronometer.setBase(SystemClock.elapsedRealtime() + time);
                simpleChronometer.start();
                break;

            case R.id.buttonStop:


                AlertDialog.Builder builder = new AlertDialog.Builder(StartDrivingActivity.this);
                builder.setTitle("Stop route?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        stopButton.setEnabled(false);
                        startButton.setEnabled(true);
                        time = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
                        simpleChronometer.stop();

                        long date = System.currentTimeMillis();
                        startingTime = new SimpleDateFormat("h:mm");
                        currentTimeString = startingTime.format(date);
                        textViewSetEndTime.setText(currentTimeString);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }
    }




    /*public class JSONTaskUser extends AsyncTask<String , String, List<Model>> {

        @Override
        protected List<Model> doInBackground(String... params) {

            SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            savedToken = sharedPreferences.getString("myToken", "");

            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL("http://10.18.2.151:3000/api/user");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept-Language", "en-US");
                connection.setRequestProperty("Content-type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Authorization", "Bearer " + savedToken);
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(20000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                String finalJSONText = stringBuffer.toString();
                JSONArray parentArray = new JSONArray(finalJSONText);

                List<Model> userList = new ArrayList<>();
                for(int i=0; i<parentArray.length(); i++)
                {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Model userModel = new Model();
                    userModel.setName(finalObject.getString("name"));
                    userModel.setRole(finalObject.getString("role"));
                    userModel.setId(finalObject.getString("_id"));

                    userList.add(userModel);
                    //db.addUser(userModelList.get(i).getUsername(), userModelList.get(i).getPassword());
                }

                return userList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();
                try {
                    if(bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }



        @Override
        protected void onPostExecute(List<Model> result) {
            super.onPostExecute(result);

            for (int i=0; i<result.size(); i++)
            {
                driverName = result.get(i).getName();
                roleName   = result.get(i).getRole();
                id = result.get(i).getId();


                tvDriverName.setText(driverName);
                tvRoleName.setText(roleName);
            }
        }
    } */




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
