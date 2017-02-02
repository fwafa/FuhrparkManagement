package com.student.fahrtenbuchapp.dataSync;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.student.fahrtenbuchapp.R;
import com.student.fahrtenbuchapp.login.PrefManager;
import com.student.fahrtenbuchapp.models.Car;
import com.student.fahrtenbuchapp.models.Credentials;
import com.student.fahrtenbuchapp.models.Drive;
import com.student.fahrtenbuchapp.models.Token;
import com.student.fahrtenbuchapp.models.User;

import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;


public class RestClient {

    private Realm realm;

    private static final String TAG = RestClient.class.getSimpleName();
    private static final String BASE_URL = "https://10.18.2.151/api/";
    private static Retrofit retrofit = null;


    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:sssZ")
            .create();

    public static Retrofit getClient() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    BackEndService mApi = getClient().create(BackEndService.class);


    public void setMyToken(final Context context, Credentials credentials){

        Call<Token> callToken = mApi.getToken(credentials);
        callToken.enqueue(new Callback<Token>(){
            @Override
            public void onResponse(Call<Token> call, final Response<Token> response) {

                int status = response.code();

                if(status == 200) {

                    Log.d(TAG, "Token hat geklappt");

                    final Token token = response.body();

                    realm = Realm.getDefaultInstance();
                    final RealmResults<Token> tokenRealmQuery = realm.where(Token.class).findAll();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            if (!tokenRealmQuery.isEmpty())
                                tokenRealmQuery.deleteAllFromRealm();

                        /*int seconds = (int) (milliseconds / 1000) % 60 ;
                        int minutes = (int) ((milliseconds / (1000*60)) % 60);
                        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);*/

                            Token myToken = realm.createObject(Token.class);
                            myToken.setToken(token.getToken());
                        }
                    });
                }

                else
                {

                    Toast toast = Toast.makeText(context, "Fehler bei der Übertragung!", Toast.LENGTH_SHORT);
                    View toastView = toast.getView(); //This'll return the default View of the Toast.

                    /* And now you can get the TextView of the default View of the Toast. */
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(25);
                    toastMessage.setTextColor(Color.WHITE);
                    toastMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(16);
                    toastView.setBackgroundColor(context.getResources().getColor(R.color.toast_color));
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

                Log.d(TAG, "Token hat nicht geklappt");

                Toast toast = Toast.makeText(context, "Fehler. Es hat nicht geklappt!\n" + t.getMessage(), Toast.LENGTH_SHORT);
                View toastView = toast.getView(); //This'll return the default View of the Toast.

                    /* And now you can get the TextView of the default View of the Toast. */
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(25);
                toastMessage.setTextColor(Color.WHITE);
                toastMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(16);
                toastView.setBackgroundColor(context.getResources().getColor(R.color.toast_color));
                toast.show();
            }

        });
    }


    public void getAllUser(final Context context, final ProgressDialog progressDialog, Token token){

        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<ArrayList<User>> callUser = mApi.listAllUsers("Bearer " + token.getToken());
        callUser.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {

                int status = response.code();

                if(status == 200) {

                    final ArrayList<User> userArrayList = response.body();
                    Log.d(TAG, "User hat geklappt!");

                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            for (int i = 0; i < userArrayList.size(); i++) {

                                User userRealm = new User();

                                userRealm.set_id(userArrayList.get(i).get_id());
                                userRealm.setUsername(userArrayList.get(i).getUsername());
                                userRealm.setFirstname(userArrayList.get(i).getFirstname());
                                userRealm.setLastname(userArrayList.get(i).getLastname());
                                userRealm.setHash(userArrayList.get(i).getHash());
                                userRealm.setSalt(userArrayList.get(i).getSalt());

                                realm.insertOrUpdate(userRealm);
                            }
                        }
                    });

                    progressDialog.dismiss();
                }

                else if(status == 401)
                {
                    Toast.makeText(context, "Unauthorized: " + status, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                else if(status == 403)
                {
                    Toast.makeText(context, "Forbidden: " + status, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                else if(status == 500)
                {
                    Toast.makeText(context, "Error: " + status, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                Log.d(TAG, "User hat nicht geklappt!");

                Toast toast = Toast.makeText(context, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT);
                View toastView = toast.getView(); //This'll return the default View of the Toast.

                    /* And now you can get the TextView of the default View of the Toast. */
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(25);
                toastMessage.setTextColor(Color.WHITE);
                toastMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(16);
                toastView.setBackgroundColor(context.getResources().getColor(R.color.toast_color));
                toast.show();
            }
        });
    }


    public void getAllCars(final Context context, final ProgressDialog progressDialog){

        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<ArrayList<Car>> callCar = mApi.listCars("");
        callCar.enqueue(new Callback<ArrayList<Car>>() {
            @Override
            public void onResponse(Call<ArrayList<Car>> call, Response<ArrayList<Car>> response) {

                int status = response.code();

                if(status == 200) {

                    final ArrayList<Car> carArrayList = response.body();

                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            for (int i = 0; i < carArrayList.size(); i++) {

                                Car realmCar = new Car();

                                realmCar.set_id(carArrayList.get(i).get_id());
                                realmCar.setVendor(carArrayList.get(i).getVendor());
                                realmCar.setModel(carArrayList.get(i).getModel());
                                realmCar.setLicenseNumber(carArrayList.get(i).getLicenseNumber());
                                realmCar.setMileage(carArrayList.get(i).getMileage());
                                realmCar.setBattery(carArrayList.get(i).getBattery());
                                realmCar.setCharging(carArrayList.get(i).getCharging());
                                realmCar.setChargedkWh(carArrayList.get(i).getChargedkWh());

                                realm.insertOrUpdate(realmCar);
                            }

                        }
                    });

                    progressDialog.dismiss();
                }

                else if(status == 401)
                {
                    Toast.makeText(context, "Unauthorized: " + status, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                else if(status == 403)
                {
                    Toast.makeText(context, "Forbidden: " + status, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                else
                {
                    Toast.makeText(context, "Error: " + status, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Car>> call, Throwable t) {

                Toast toast = Toast.makeText(context, "Failed: Cars not synchronized!" + t.getMessage(), Toast.LENGTH_SHORT);
                View toastView = toast.getView(); //This'll return the default View of the Toast.

                    /* And now you can get the TextView of the default View of the Toast. */
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(25);
                toastMessage.setTextColor(Color.WHITE);
                toastMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(16);
                toastView.setBackgroundColor(context.getResources().getColor(R.color.toast_color));
                toast.show();
            }
        });
    }

    public void postDrive(final Context context, Drive drive, Token token){

        final Call<Drive> postDrives = mApi.postDrive("Bearer " + token.getToken(), drive);
        postDrives.enqueue(new Callback<Drive>() {
            @Override
            public void onResponse(Call<Drive> call, Response<Drive> response) {

                int postStatus = response.code();

                Log.d(TAG, "Es hat mit dem POST geklappt!");
                Log.d("PostDrive", "onResponse: " + postStatus);

                realm = Realm.getDefaultInstance();

                if(postStatus == 200)
                {
                    Toast toast = Toast.makeText(context, "Daten erfolgreich übermittelt", Toast.LENGTH_SHORT);
                    View toastView = toast.getView(); //This'll return the default View of the Toast.

                    /* And now you can get the TextView of the default View of the Toast. */
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(25);
                    toastMessage.setTextColor(Color.WHITE);
                    toastMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(16);
                    toastView.setBackgroundColor(context.getResources().getColor(R.color.toast_color));
                    toast.show();
                }
                else
                {
                    Toast toast = Toast.makeText(context, "Übermittlung nicht erfolgreich: " + response.message(), Toast.LENGTH_SHORT);
                    View toastView = toast.getView(); //This'll return the default View of the Toast.

                    /* And now you can get the TextView of the default View of the Toast. */
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(25);
                    toastMessage.setTextColor(Color.WHITE);
                    toastMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(16);
                    toastView.setBackgroundColor(context.getResources().getColor(R.color.toast_color));
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Drive> call, Throwable t) {

                Log.d(TAG, "POST hat nicht geklappt: " + t.getMessage());

                Toast toast = Toast.makeText(context, "Daten konnten nicht übermittelt werden", Toast.LENGTH_SHORT);
                View toastView = toast.getView(); //This'll return the default View of the Toast.

                    /* And now you can get the TextView of the default View of the Toast. */
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(25);
                toastMessage.setTextColor(Color.WHITE);
                toastMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(16);
                toastView.setBackgroundColor(context.getResources().getColor(R.color.toast_color));
                toast.show();
            }
        });
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            builder.addNetworkInterceptor(new StethoInterceptor());

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
