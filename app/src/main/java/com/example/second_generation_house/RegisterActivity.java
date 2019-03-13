package com.example.second_generation_house;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.second_generation_house.ConnectionToBd.ConnectToBd;
import com.example.second_generation_house.ConnectionToBd.ServerCallback;
import com.example.second_generation_house.DataBase.AppDataBase;
import com.example.second_generation_house.DataBase.CheckUser;
import com.example.second_generation_house.DataBase.User;
import com.example.second_generation_house.KeysAndLinks.Keys;
import com.example.second_generation_house.KeysAndLinks.Links;
import com.example.second_generation_house.NetWork.HasConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private Button btnRegisterUser;
    private CheckUser checkUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= 22) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        btnRegisterUser = findViewById(R.id.btnRegisterUser);
        btnRegisterUser.setOnClickListener(this);
        checkUser = new CheckUser(this);

    }

    private void register() {
        final String Email = editTextEmail.getText().toString();
        final String Password = editTextPassword.getText().toString();
        String RepeatPassword = editTextRepeatPassword.getText().toString();
        ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white));
        editTextEmail.setBackgroundTintList(colorStateList);
        editTextPassword.setBackgroundTintList(colorStateList);
        editTextRepeatPassword.setBackgroundTintList(colorStateList);
        colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.main_red));
        if (!HasConnection.hasConnection(this)) {
            toastText("Check your connection to the Internet!");
            return;
        } else if (Email.equals("")) {
            editTextEmail.setBackgroundTintList(colorStateList);
            toastText("Enter email!");
            return;
        } else if (Password.equals("")) {
            editTextPassword.setBackgroundTintList(colorStateList);
            toastText("Enter password!");
            return;
        } else if (RepeatPassword.equals("")) {
            editTextRepeatPassword.setBackgroundTintList(colorStateList);
            toastText("Enter password!");
            return;
        } else if (!Password.equals(RepeatPassword)) {
            editTextPassword.setBackgroundTintList(colorStateList);
            editTextRepeatPassword.setBackgroundTintList(colorStateList);
            toastText("Password are not the same!");
            return;
        }
        ConnectToBd connectToBd = new ConnectToBd(this);
        HashMap<String, String> params = new HashMap<>();
        params.put(Keys.LoginActivity_Email, Email);
        params.put(Keys.RegisterActivity_Password, Password);
        params.put(Keys.RegisterActivity_RepeatPassword, RepeatPassword);
        connectToBd.postToBd(Links.RegisterActivity_Register, params, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                String error = result.getString("error");
                if (error.equals("false")) {
                    User user = new User();
                    user.setId(0);
                    user.setPassword(Password);
                    user.setKey(result.getString(Keys.RegisterActivity_KeyId));
                    user.setEmail(Email);
                    AppDataBase appDataBase = Room.databaseBuilder(getApplicationContext(),
                            AppDataBase.class, "userDB").allowMainThreadQueries().build();
                    appDataBase.myDao().addUser(user);
                    checkUser.setLoggedIn(true);
                    Intent i = new Intent(RegisterActivity.this, MenuActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    toastText(error);
                }
            }

            @Override
            public void onFailure(String error) {
                toastText(error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegisterUser:
                register();
        }
    }

    private void toastText(String text) {
        Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_LONG).show();
    }
}
