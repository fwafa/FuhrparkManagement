package com.student.fahrtenbuchapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.student.fahrtenbuchapp.com.student.fahrtenbuchapp.models.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login, register;
    private EditText etUser, etPass;
    private DBHelper db;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Animation from_up_to_down = AnimationUtils.loadAnimation(this, R.anim.move_from_right);
        TextView textView = (TextView) findViewById(R.id.textViewLogin);
        textView.setAnimation(from_up_to_down);

        Animation from_right_to_left = AnimationUtils.loadAnimation(this, R.anim.move_from_left);
        ImageView imageView = (ImageView) findViewById(R.id.imageViewLoginCar);
        imageView.setAnimation(from_right_to_left);

        db = new DBHelper(this);
        session = new Session(this);

        login = (Button) findViewById(R.id.btnLogin);
        register = (Button) findViewById(R.id.btnRegister);
        etUser = (EditText) findViewById(R.id.etUsername);
        etPass = (EditText) findViewById(R.id.etPassword);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        if(session.loggedin()){
            startActivity(new Intent(LoginActivity.this,WaitForLoginActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnLogin:
                new JSONTask().execute("http://localhost:8080/manager/Daten.txt");
                break;
            case R.id.btnRegister:
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                break;
            default:

        }
    }

    /*private void login(){
        String username = etUser.getText().toString();
        String password = etPass.getText().toString();

        if(db.getUser(username,password)){
            session.setLoggedin(true);
            startActivity(new Intent(LoginActivity.this, WaitForLoginActivity.class));
            finish();
        }
        else if (username.length() == 0)
        {
            etUser.setError("Enter Username");
        }
        else if(password.length() == 0)
        {
            etPass.setError("Enter Password");
        }
        else {
            Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
        }
    } */


    public class JSONTask extends AsyncTask<String , String, List<Model>> {

        @Override
        protected List<Model> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(params[0]);
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
                JSONObject parentObject = new JSONObject(finalJSONText);
                JSONArray parentArray = parentObject.getJSONArray("user");

                List<Model> userModelList = new ArrayList<>();
                for(int i=0; i<parentArray.length(); i++)
                {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Model userModel = new Model();
                    userModel.setName(finalObject.getString("name"));
                    userModel.setSurname(finalObject.getString("surname"));
                    userModel.setUsername(finalObject.getString("username"));
                    userModel.setPassword(finalObject.getString("password"));

                    userModelList.add(userModel);
                }

                return userModelList;

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

            String username = result.get(0).getUsername();
            String password = result.get(0).getPassword();

            if(etUser.equals(username) && etPass.equals(password)){
                session.setLoggedin(true);
                startActivity(new Intent(LoginActivity.this, WaitForLoginActivity.class));
                finish();
            }
            else if (username.length() == 0)
            {
                etUser.setError("Enter Username");
            }
            else if(password.length() == 0)
            {
                etPass.setError("Enter Password");
            }
            else {
                Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
