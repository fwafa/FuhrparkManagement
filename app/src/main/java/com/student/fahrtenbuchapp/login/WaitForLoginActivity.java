package com.student.fahrtenbuchapp.login;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.student.fahrtenbuchapp.R;
import com.student.fahrtenbuchapp.logic.StartDrivingActivity;

public class WaitForLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_wait_for_login);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(WaitForLoginActivity.this, StartDrivingActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
