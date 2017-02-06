package com.student.fahrtenbuchapp.logic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.student.fahrtenbuchapp.R;
import com.student.fahrtenbuchapp.login.LoginActivity;
import com.student.fahrtenbuchapp.models.Car;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class ShowAllCarsActivity extends AppCompatActivity {

    private RealmRecyclerView realmRecyclerView;
    private CarRecyclerAdapter carRecyclerAdapter;
    private Realm realm;

    private TextView allCarsTextViewTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_cars);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_layout, null))
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

        AlertDialog dialog = builder.create();
        dialog.show();

        allCarsTextViewTitle = (TextView) findViewById(R.id.allCarsTextViewTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/SNAP.TTF");
        allCarsTextViewTitle.setTypeface(typeface);

        realmRecyclerView = (RealmRecyclerView) findViewById(R.id.recyclerView);

        realm = Realm.getDefaultInstance();
        RealmResults<Car> carRealmResults = realm.where(Car.class).findAll();
        carRecyclerAdapter = new CarRecyclerAdapter(getBaseContext(), carRealmResults, false, false);
        realmRecyclerView.addItemDecoration(new com.student.fahrtenbuchapp.logic.DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        realmRecyclerView.setAdapter(carRecyclerAdapter);
    }


    public class CarRecyclerAdapter extends RealmBasedRecyclerViewAdapter<Car, CarRecyclerAdapter.ViewHolder> {

        public CarRecyclerAdapter(Context context, RealmResults<Car> realmResults, boolean automaticUpdate, boolean animateResults) {
            super(context, realmResults, automaticUpdate, animateResults);
        }

        public class ViewHolder extends RealmViewHolder {

            public TextView tvVendor, tvModel;
            public ImageButton imageButton;

            public ViewHolder(LinearLayout container) {
                super(container);

                this.tvVendor    = (TextView) container.findViewById(R.id.tvVendor);
                this.tvModel     = (TextView) container.findViewById(R.id.tvModel);
                this.imageButton = (ImageButton) container.findViewById(R.id.imageViewCar);
            }
        }

        @Override
        public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
            View v = inflater.inflate(R.layout.row_item, viewGroup, false);
            ViewHolder vh = new ViewHolder((LinearLayout) v);
            return vh;
        }

        @Override
        public void onBindRealmViewHolder(final ViewHolder viewHolder, final int position) {

            final Car car = realmResults.get(position);

            viewHolder.tvVendor.setText(car.getVendor());
            viewHolder.tvModel.setText(car.getModel());

            viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    realm = Realm.getDefaultInstance();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            RealmResults<Car> carRealmResults = realm.where(Car.class).findAll();

                            for(int i=0; i<carRealmResults.size(); i++)
                            {
                                if(!carRealmResults.get(i).get_id().equals(car.get_id()))
                                {
                                    carRealmResults.get(i).deleteFromRealm();
                                }
                            }
                        }
                    });

                    startActivity(new Intent(ShowAllCarsActivity.this, LoginActivity.class));
                }
            });
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }
}
