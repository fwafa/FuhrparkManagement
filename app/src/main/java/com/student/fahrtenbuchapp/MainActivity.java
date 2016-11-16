package com.student.fahrtenbuchapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.student.fahrtenbuchapp.com.student.fahrtenbuchapp.models.Model;

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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHelper db;
    private Session session;

    private Button bookAudi, bookBmw, bookVw, bookOpel;
    private ImageButton iButtonAudi, iButtonBmw, iButtonVw, iButtonOpel;
    private CardView cardViewAudi, cardViewBmw, cardViewVw, cardViewOpel;

    private TextView tvAuto1, tvAuto2, tvAuto1Model, tvAuto2Model;
    private String strMileage, strBattery, strAvailability;
    private String strVendor1, strModel1, strVendor2, strModel2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();

        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }

        tvAuto1 = (TextView) findViewById(R.id.textViewAuto1);
        tvAuto2 = (TextView) findViewById(R.id.textViewAuto2);
        tvAuto1Model = (TextView) findViewById(R.id.textViewModel1);
        tvAuto2Model = (TextView) findViewById(R.id.textViewModel2);

        bookAudi = (Button) findViewById(R.id.bookingButtonAuto1);
        bookBmw = (Button) findViewById(R.id.bookingButtonAuto2);
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

        cardViewAudi = (CardView) findViewById(R.id.cardViewAuto1);
        cardViewBmw = (CardView) findViewById(R.id.cardViewAuto2);
        cardViewVw = (CardView) findViewById(R.id.cardViewVw);
        cardViewOpel = (CardView) findViewById(R.id.cardViewOpel);

        iButtonAudi = (ImageButton) findViewById(R.id.imageButtonAuto);
        iButtonBmw = (ImageButton) findViewById(R.id.imageButtonAuto2);
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

        new JSONTaskGetCars().execute();
    }


    public void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }




    public class JSONTaskGetCars extends AsyncTask<String , String, List<Model>> {

        @Override
        protected List<Model> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL("http://10.18.2.151:3000/api/car");
                connection = (HttpURLConnection) url.openConnection();
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

                List<Model> carList = new ArrayList<>();
                for(int i=0; i<parentArray.length(); i++)
                {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Model carModel = new Model();
                    carModel.setVendor(finalObject.getString("vendor"));
                    carModel.setModel(finalObject.getString("model"));

                    carList.add(carModel);
                    //db.addUser(userModelList.get(i).getUsername(), userModelList.get(i).getPassword());
                }

                return carList;

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

            //for (int i = 0; i < result.size(); i++) {
                strVendor1 = result.get(0).getVendor();
                strModel1 = result.get(0).getModel();

                strVendor2 = result.get(1).getVendor();
                strModel2 = result.get(1).getModel();

                tvAuto1.setText(strVendor1);
                tvAuto1Model.setText(strModel1);
                tvAuto2.setText(strVendor2);
                tvAuto2Model.setText(strModel2);
           // }
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
