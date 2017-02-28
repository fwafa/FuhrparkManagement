package com.student.fahrtenbuchapp.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.student.fahrtenbuchapp.R;
import com.student.fahrtenbuchapp.dataSync.RestClient;
import com.student.fahrtenbuchapp.logic.StartDrivingActivity;
import com.student.fahrtenbuchapp.models.Car;
import com.student.fahrtenbuchapp.models.Credentials;
import com.student.fahrtenbuchapp.models.Token;
import com.student.fahrtenbuchapp.models.TokenData;
import com.student.fahrtenbuchapp.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import io.realm.Realm;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity {

    public static final String Name = "nameKey";

    private final static String TAG = LoginActivity.class.getSimpleName();

    private Button login;
    private String password, username;
    private EditText etUser, etPass;

    private boolean userAndPasswordAreTrue;

    private Realm realm;
    private TokenData tokenData = new TokenData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences carPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor carEditor = carPreferences.edit();

        Animation from_up_to_down = AnimationUtils.loadAnimation(this, R.anim.move_from_right);
        TextView textView = (TextView) findViewById(R.id.textViewLogin);
        textView.setAnimation(from_up_to_down);

        Animation from_right_to_left = AnimationUtils.loadAnimation(this, R.anim.move_from_left);
        ImageView imageView = (ImageView) findViewById(R.id.imageViewLoginCar);
        imageView.setAnimation(from_right_to_left);

        etUser = (EditText) findViewById(R.id.etUsername);
        etPass = (EditText) findViewById(R.id.etPassword);

        login = (Button) findViewById(R.id.btnLogin);

        realm = Realm.getDefaultInstance();
        RealmResults<Car> carRealmResults = realm.where(Car.class).findAll();
        if(!carRealmResults.isEmpty())
        {
            Car car = carRealmResults.get(0);

            carEditor.putString("vendor", car.getVendor());
            carEditor.putString("model", car.getModel());
            carEditor.apply();
        }

        new MyAsyncTask().execute();
    }


    public class MyAsyncTask extends AsyncTask<String, String, TokenData> {

        @Override
        protected TokenData doInBackground(String... params) {

            realm = Realm.getDefaultInstance();

            try {
                Token myToken = realm.where(Token.class).findFirst();

                decoded(myToken.getToken());

                JSONObject jsonObject = new JSONObject(tokenData.getBody());
                tokenData.setId(jsonObject.getString("id"));
                tokenData.setUsername(jsonObject.getString("username"));
                tokenData.setRole(jsonObject.getString("role"));
                tokenData.setExp(jsonObject.getString("exp"));
                tokenData.setIat(jsonObject.getString("iat"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return tokenData;
        }

        @Override
        protected void onPostExecute(final TokenData newTokenData) {
            super.onPostExecute(newTokenData);

            realm = Realm.getDefaultInstance();

            final RealmResults<User> userRealmResults = realm.where(User.class).findAll();

            long timeMilliSecExp = Long.parseLong(newTokenData.getExp());
            long timeMilliSecIat = Long.parseLong(newTokenData.getIat());

            long time = (timeMilliSecExp - timeMilliSecIat) * 1000;

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();

                    username = etUser.getText().toString();
                    password = etPass.getText().toString();

                    editor.putString(Name, username);
                    editor.apply();


                    String securedPasswordHash = null;

                    if (username.trim().length() == 0) {
                        etUser.setError("Enter Username");
                    }

                    else if (password.trim().length() == 0) {
                        etPass.setError("Enter Password");
                    }
                    else {
                        try {
                            for(int i=0; i<userRealmResults.size(); i++) {

                                securedPasswordHash = generatePasswordHash(password, userRealmResults.get(i).getSalt());

                                if (userRealmResults.get(i).getUsername().equals(username) &&
                                        userRealmResults.get(i).getHash().equals(securedPasswordHash))
                                {
                                    userAndPasswordAreTrue = true;
                                }

                                else
                                {
                                    userAndPasswordAreTrue = false;
                                }
                            }

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        }


                        if(userAndPasswordAreTrue)
                        {
                            Intent intent = new Intent(LoginActivity.this, WaitForLoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Wrong username/password", Toast.LENGTH_SHORT).show();
                        }

                        System.out.println(securedPasswordHash);
                    }
                }
            });

            /*tokenData.getHeader();
            tokenData.getBody();
            tokenData.getId();
            tokenData.getUsername();
            tokenData.getRole();
            tokenData.getExp();
            tokenData.getIat();
            TimeUnit.MILLISECONDS.toMinutes(time);*/

        }
    }


    public void decoded(String JWTEncoded) {

        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));

            tokenData = new TokenData();

            tokenData.setHeader(getJson(split[0]));
            tokenData.setBody(getJson(split[1]));
        } catch (UnsupportedEncodingException e) {
            //Error
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }


    private static String generatePasswordHash(String password, String saltString) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = saltString.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return toHex(hash);
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
}
