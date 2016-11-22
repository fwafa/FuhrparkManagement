package com.student.fahrtenbuchapp.login;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.student.fahrtenbuchapp.logic.ShowAllCarsActivity;
import com.student.fahrtenbuchapp.R;

public class WaitForLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_login);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent1 = new Intent(WaitForLoginActivity.this, ShowAllCarsActivity.class);
                startActivity(intent1);
                finish();
            }
        }, 3000);
    }
}
