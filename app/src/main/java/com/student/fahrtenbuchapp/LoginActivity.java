package com.student.fahrtenbuchapp;

import android.content.Intent;
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
                login();
                break;
            case R.id.btnRegister:
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                break;
            default:

        }
    }

    private void login(){
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
    }
}
