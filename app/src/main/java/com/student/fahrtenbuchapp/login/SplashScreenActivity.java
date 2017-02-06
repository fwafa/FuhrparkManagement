package com.student.fahrtenbuchapp.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.student.fahrtenbuchapp.R;
import com.student.fahrtenbuchapp.logic.ShowAllCarsActivity;
import com.student.fahrtenbuchapp.logic.StartDrivingActivity;
import com.student.fahrtenbuchapp.models.Car;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;

public class SplashScreenActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String vendor = preferences.getString("vendor", "");
        String model = preferences.getString("model", "");


        Animation anim = AnimationUtils.loadAnimation(this, R.anim.move_up);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setAnimation(anim);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.move_from_right);
        TextView textViewCarModel = (TextView) findViewById(R.id.tvCarModel);

        if(!vendor.equalsIgnoreCase("") && !model.equalsIgnoreCase(""))
        {
            textViewCarModel.setText(vendor + " " + model);
            textViewCarModel.setAnimation(animation);
        }
        else
        {
            textViewCarModel.setText("");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreenActivity.this, SliderActivity.class));
                finish();
            }
        }, 4000);
    }
}
