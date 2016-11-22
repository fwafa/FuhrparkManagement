package com.student.fahrtenbuchapp.logic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.student.fahrtenbuchapp.R;
import com.student.fahrtenbuchapp.database.DBHelper;
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
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowAllCarsActivity extends AppCompatActivity {

    private DBHelper db;
    private Session session;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_cars);

        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }

        listView = (ListView) findViewById(R.id.listViewCars);

        new JSONTaskGetCars().execute();
    }


    public void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(ShowAllCarsActivity.this,LoginActivity.class));
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
                Gson gson = new Gson();
                for(int i=0; i<parentArray.length(); i++)
                {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Model carModel = new Model(); //gson.fromJson(finalObject.toString(), Model.class);
                    carModel.setVendor(finalObject.getString("vendor"));
                    carModel.setModel(finalObject.getString("model"));
                    carModel.setAvailability(finalObject.getBoolean("available"));
                    carModel.setMileage(finalObject.getInt("mileage"));
                    carModel.setBattery(finalObject.getInt("battery"));

                    carModel.setImageButtonCar(getResources().getDrawable(R.drawable.car_1));
                    carModel.setImgBattery(getResources().getDrawable(R.drawable.charged_battery));
                    carModel.setThumbUp(getResources().getDrawable(R.drawable.ic_thumb_up_white));
                    carModel.setThumbDown(getResources().getDrawable(R.drawable.ic_thumb_down_white));

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

            CarAdapter carAdapter = new CarAdapter(getApplicationContext(), R.layout.row_item, result);
            listView.setAdapter(carAdapter);

            //for (int i = 0; i < result.size(); i++) {
                //strVendor1 = result.get(0).getVendor();
                //strModel1 = result.get(0).getModel();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowAllCarsActivity.this);
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



    public class CarAdapter extends ArrayAdapter {

        private LayoutInflater inflater;
        private List<Model> carModelList;
        private int resource;

        public CarAdapter(Context context, int resource, List<Model> objects) {
            super(context, resource, objects);

            carModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
            {
                convertView = inflater.inflate(resource, null);
            }


            Button buttonReservation;
            ImageButton imageButtonCar;
            TextView tvVendor, tvModel, tvMileage, tvMileageTitle, tvAvailability;
            TextView tvBatteryLife, tvBatteryPercentage;
            ImageView imageViewBattery, thumbUp, thumbDown;

            imageButtonCar = (ImageButton) convertView.findViewById(R.id.imageButtonCar);
            thumbUp = (ImageView) convertView.findViewById(R.id.thumb_up);
            thumbDown = (ImageView) convertView.findViewById(R.id.thumb_down);
            imageViewBattery = (ImageView) convertView.findViewById(R.id.imageViewBattery);
            tvVendor = (TextView) convertView.findViewById(R.id.textViewVendor);
            tvModel = (TextView) convertView.findViewById(R.id.textViewCarModel);
            tvMileage = (TextView) convertView.findViewById(R.id.textViewMileage);
            tvBatteryLife = (TextView) convertView.findViewById(R.id.textViewBatteryLife);
            buttonReservation = (Button) convertView.findViewById(R.id.bookingButtonCar);

            tvVendor.setText(carModelList.get(position).getVendor());
            tvModel.setText(carModelList.get(position).getModel());

            String mileage = String.valueOf(carModelList.get(position).getMileage());
            tvMileage.setText(mileage + " km");

            String batteryLife = String.valueOf(carModelList.get(position).getBattery());
            tvBatteryLife.setText(batteryLife);


            if(carModelList.get(position).isAvailability())
            {
                thumbDown.setVisibility(View.GONE);
            } else {
                thumbUp.setVisibility(View.GONE);
            }


            buttonReservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShowAllCarsActivity.this, ReservationActivity.class);
                    startActivity(intent);
                }
            });

            imageButtonCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShowAllCarsActivity.this, StartDrivingActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;


        }
    }
}
