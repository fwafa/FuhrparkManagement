package com.student.fahrtenbuchapp.logic;


import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DigitalClock;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.student.fahrtenbuchapp.R;

public class MyPopUp extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width  = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 1.0), (int)(height * 1.0));


        Animation anim = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        DigitalClock digitalClock = (DigitalClock) findViewById(R.id.clock);
        digitalClock.setAnimation(anim);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutPopUp);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
