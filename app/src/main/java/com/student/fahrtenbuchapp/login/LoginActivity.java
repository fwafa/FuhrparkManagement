package com.student.fahrtenbuchapp.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.student.fahrtenbuchapp.database.DBHelper;
import com.student.fahrtenbuchapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = LoginActivity.class.getSimpleName();

    private Button login, register;
    private DBHelper db;
    private Session session;

    private String password, username;

    private EditText etUser, etPass;

    private String myToken;

    private Context context;
    private ProgressDialog dialog;

    private URL url;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;
    private String message;

    private AsyncTask.Status status;


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

        etUser = (EditText) findViewById(R.id.etUsername);
        etPass = (EditText) findViewById(R.id.etPassword);

        db = new DBHelper(this);
        session = new Session(this);

        login = (Button) findViewById(R.id.btnLogin);
        register = (Button) findViewById(R.id.btnRegister);

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
                username = etUser.getText().toString();
                password = etPass.getText().toString();

                JSONTaskPostLogin jsonTaskPostLogin = new JSONTaskPostLogin();
                jsonTaskPostLogin.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, username, password);

                //JSONTaskGetUser jsonTaskGetUser = new JSONTaskGetUser();
                //jsonTaskGetUser.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

                break;
            case R.id.btnRegister:
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                break;
            default:

        }
    }


    public class JSONTaskPostLogin extends AsyncTask<String , String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("http://10.18.2.151:3000/api/login");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept-Language", "en-US");
                connection.setRequestProperty("Content-type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(20000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", params[0]);
                jsonObject.put("password", params[1]);

                message = jsonObject.toString();

                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(message);
                wr.flush();


                StringBuffer sb = new StringBuffer();
                int HttpResult = connection.getResponseCode();

                if(HttpResult == HttpURLConnection.HTTP_OK)
                {
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                    String line = "";
                    while((line = bufferedReader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }

                    myToken = sb.toString();
                    System.out.println("JSONTaskPostLogin myToken: " + myToken);

                    bufferedReader.close();

                } else
                    System.out.println("connection response: " + connection.getResponseMessage());

                return sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String username = etUser.getText().toString();
            String password = etPass.getText().toString();

            if (username.equals("Admin")
                    && password.equals("1234567890")) {
                session.setLoggedin(true);
                Intent intent = new Intent(LoginActivity.this, WaitForLoginActivity.class);
                startActivity(intent);
                finish();
            } else if (username.length() == 0) {
                etUser.setError("Enter Username");
            } else if (password.length() == 0) {
                etPass.setError("Enter Password");
            } else {
                Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
            }

        }
    }


    /*public class JSONTaskGetUser extends AsyncTask<String , String, List<Model>> {

        @Override
        protected List<Model> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL("http://10.18.2.151:3000/api/user");
                connection = (HttpURLConnection) url.openConnection();
                String token = myToken;
                System.out.println("doInBackground: " + token);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setRequestProperty("Content-type", "application/json; charset=UTF-8");
                connection.connect();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED) {

                    InputStream inputStream = connection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }

                    String finalJSONText = stringBuffer.toString();
                    JSONArray parentArray = new JSONArray(finalJSONText);

                    List<Model> userList = new ArrayList<>();
                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        Model userModel = new Model();
                        userModel.setName(finalObject.getString("name"));
                        userModel.setRole(finalObject.getString("role"));
                        userModel.setUsername(finalObject.getString("username"));
                        userModel.setId(finalObject.getString("_id"));

                        userList.add(userModel);
                        //db.addUser(userList.get(i).getUsername(), userList.get(i).getPassword());
                    }

                    return userList;
                }
                else
                    System.out.println(connection.getResponseMessage() + connection.getResponseCode());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (bufferedReader != null)
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


            String username = etUser.getText().toString();
            String password = etPass.getText().toString();

            for (int i = 0; i < result.size(); i++) {

                System.out.println("JSONTaskGetUser result: " + result.get(i).toString());

                if (result.get(i).getUsername().equals(username)
                        && password.equals("1234567890")) {
                    session.setLoggedin(true);
                    Intent intent = new Intent(LoginActivity.this, WaitForLoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if (username.length() == 0) {
                    etUser.setError("Enter Username");
                } else if (password.length() == 0) {
                    etPass.setError("Enter Password");
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
                }
            }
        }

    } */
}