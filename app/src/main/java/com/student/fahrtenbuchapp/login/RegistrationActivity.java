package com.student.fahrtenbuchapp.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.student.fahrtenbuchapp.database.DBHelper;
import com.student.fahrtenbuchapp.R;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "name_key";
    public static final String Surname = "surname_key";
    SharedPreferences sharedPreferences;

    private Button reg;
    private TextView tvLogin;
    private EditText etName, etSurname, etUser, etPass, etRePass;
    private DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        db = new DBHelper(this);

        reg = (Button) findViewById(R.id.btnRegisterReg);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        etName = (EditText) findViewById(R.id.etNameReg);
        etSurname = (EditText) findViewById(R.id.etSurnameReg);
        etUser = (EditText) findViewById(R.id.etUsernameReg);
        etPass = (EditText) findViewById(R.id.etPasswordReg);
        etRePass = (EditText) findViewById(R.id.etRepeatPasswordReg);

        reg.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnRegisterReg:
                register();
                break;
            case R.id.tvLogin:
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
                break;
            default:
        }
    }

    private void register()
    {
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String username = etUser.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        String re_password = etRePass.getText().toString().trim();

        if(name.length() == 0)
        {
            etUser.setError("Enter username");
        }
        if(surname.length() == 0)
        {
            etUser.setError("Enter username");
        }
        if(username.length() == 0)
        {
            etUser.setError("Enter username");
        }
        if(password.length() == 0)
        {
            etPass.setError("Enter password");
        }
        if(re_password.length() == 0)
        {
            etPass.setError("Enter password");
        }
        if(!password.equals(re_password))
        {
            displayToast("Password repetition doesn't match");
        }
        else
        {
            db.addUser(username, password);

            sharedPreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Name, name);
            editor.putString(Surname, surname);
            editor.commit();

            displayToast("User registered");
            finish();
        }
    }

    private void displayToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }
}
