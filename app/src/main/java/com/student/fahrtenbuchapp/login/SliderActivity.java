package com.student.fahrtenbuchapp.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.student.fahrtenbuchapp.R;
import com.student.fahrtenbuchapp.dataSync.RestClient;
import com.student.fahrtenbuchapp.logic.ShowAllCarsActivity;
import com.student.fahrtenbuchapp.models.AddressStart;
import com.student.fahrtenbuchapp.models.AddressStop;
import com.student.fahrtenbuchapp.models.Credentials;
import com.student.fahrtenbuchapp.models.Drive;
import com.student.fahrtenbuchapp.models.LocationStart;
import com.student.fahrtenbuchapp.models.LocationStop;
import com.student.fahrtenbuchapp.models.Token;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class SliderActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;

    private Realm realm;
    private RestClient restClient = new RestClient();

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        // Checking for first time launch - before calling setContentView()
        /*prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }*/


        prefManager = new PrefManager(this);


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_slider);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {

        if (!prefManager.isFirstTimeLaunch()) {
            startActivity(new Intent(SliderActivity.this, LoginActivity.class));
        }
        else
        {
            RealmResults<Token> tokenRealmResults = realm.where(Token.class).findAll();
            if(tokenRealmResults.isEmpty())
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.dialog_layout_synchronize, null))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {

                        Button positiveButton = ((AlertDialog) dialog)
                                .getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setTextSize(25);

                        Button negativeButton = ((AlertDialog) dialog)
                                .getButton(AlertDialog.BUTTON_NEGATIVE);
                        negativeButton.setTextSize(25);
                    }
                });
                dialog.show();
            }
            else
            {
                prefManager.setFirstTimeLaunch(false);
                startActivity(new Intent(SliderActivity.this, ShowAllCarsActivity.class));
            }
        }
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
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
            case R.id.refresh:

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (wifi.isConnected()) {
                    progressDialog = new ProgressDialog(this);

                    Credentials credentials = new Credentials();
                    credentials.setUsername("CarDevice");
                    credentials.setPassword("1234567890");

                    restClient.setMyToken(this, credentials);

                    if(prefManager.isFirstTimeLaunch())
                    {
                        restClient.getAllCars(this, progressDialog);
                    }

                    realm = Realm.getDefaultInstance();
                    RealmResults<Token> myToken = realm.where(Token.class).findAll();

                    RealmChangeListener realmChangeListener = new RealmChangeListener<RealmResults<Token>>() {
                        @Override
                        public void onChange(RealmResults<Token> element) {

                            restClient.getAllUser(SliderActivity.this, progressDialog, element.get(0));
                        }
                    };

                    myToken.addChangeListener(realmChangeListener);


                    final RealmResults<Drive> drives = realm.where(Drive.class).findAll();
                    if(!drives.isEmpty()) {

                        for(int i=0; i<drives.size(); i++)
                        {
                            if(        drives.get(i).getUser() != null
                                    && drives.get(i).getCar() != null
                                    && drives.get(i).getStartDate() != null
                                    && drives.get(i).getEndDate() != null
                                    && drives.get(i).getStartCoord() != null
                                    && drives.get(i).getEndCoord() != null
                                    && drives.get(i).getStartAddress() != null
                                    && drives.get(i).getEndAddress() != null
                                    && drives.get(i).getStartMileage() != null
                                    && drives.get(i).getEndMileage() != null
                                    && drives.get(i).getUsedkWh() != null)
                            {
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

                                AddressStart addressStart   = new AddressStart(startAddressCountry, startAddressCity, startAddressZip, startAddressStreet);
                                AddressStop addressStop     = new AddressStop(endAddressCountry, endAddressCity, endAddressZip, endAddressStreet);
                                LocationStart locationStart = new LocationStart(startCoordLatitude, startCoordLongitude);
                                LocationStop locationStop   = new LocationStop(endCoordLatitude, endCoordLongitude);

                                final Drive drive = new Drive(userID, carId, startDate, endDate, locationStart, locationStop,
                                        addressStart, addressStop, startPOI, endPOI, startMileage, endMileage, usedkWh);

                                RealmChangeListener tokenChangeListener = new RealmChangeListener<RealmResults<Token>>() {
                                    @Override
                                    public void onChange(RealmResults<Token> element) {

                                        System.out.println("Token element: " + element.get(0));
                                        restClient.postNewDrives(getApplicationContext(), element.get(0), drive);
                                    }
                                };

                                myToken.addChangeListener(tokenChangeListener);
                            }
                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(SliderActivity.this, "Es sind noch keine Farhten vorhanden!", Toast.LENGTH_SHORT);
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
                    }
                }

                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LayoutInflater inflater = this.getLayoutInflater();

                    builder.setView(inflater.inflate(R.layout.dialog_layout_wifi, null))
                            .setPositiveButton("Einstellungen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                    startActivity(intent);
                                }
                            })

                            .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });

                    AlertDialog dialog = builder.create();

                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {

                            Button positiveButton = ((AlertDialog) dialog)
                                    .getButton(AlertDialog.BUTTON_POSITIVE);
                            positiveButton.setTextSize(25);

                            Button negativeButton = ((AlertDialog) dialog)
                                    .getButton(AlertDialog.BUTTON_NEGATIVE);
                            negativeButton.setTextSize(25);
                        }
                    });

                    dialog.show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
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
}
