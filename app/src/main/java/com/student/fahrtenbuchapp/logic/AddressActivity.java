package com.student.fahrtenbuchapp.logic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.student.fahrtenbuchapp.R;
import com.student.fahrtenbuchapp.models.User;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddressActivity extends AppCompatActivity {

    static final int START_ADDRESS_REQUEST = 1;  // The request code
    static final int STOP_ADDRESS_REQUEST = 2;  // The request code

    private List<String> arrayList;
    private ArrayAdapter<String> dataAdapter;

    private StartDrivingActivity startDrivingActivity;
    private Realm realm;

    private TextView tvDriverName;
    public static final String Name = "nameKey";

    private EditText etStreet, etZIP, etCity;
    private Button bCancel, bOK;

    private Boolean ifStartAddress, ifStopAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        etStreet = (EditText) findViewById(R.id.etStreet);
        etZIP    = (EditText) findViewById(R.id.etZIP);
        etCity   = (EditText) findViewById(R.id.etCity);

        bOK     = (Button) findViewById(R.id.buttonOK);
        bCancel = (Button) findViewById(R.id.buttonCancel);

        tvDriverName = (TextView) findViewById(R.id.nameTextView2);

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

        ifStartAddress = getIntent().getExtras().getBoolean("ifStartAddress");
        ifStopAddress = getIntent().getExtras().getBoolean("ifStopAddress");

        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String street = etStreet.getText().toString();
                String zip    = etZIP.getText().toString();
                String city   = etCity.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("street", street);
                intent.putExtra("zip", zip);
                intent.putExtra("city", city);

                if(ifStartAddress)
                {
                    setResult(START_ADDRESS_REQUEST, intent);
                    finish();
                }
                if(ifStopAddress)
                {
                    setResult(STOP_ADDRESS_REQUEST, intent);
                    finish();
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ifStartAddress)
                {
                    setResult(Activity.RESULT_CANCELED, new Intent());
                    finish();
                }

                if(ifStopAddress)
                {
                    setResult(Activity.RESULT_CANCELED, new Intent());
                    finish();
                }
            }
        });
    }
}
