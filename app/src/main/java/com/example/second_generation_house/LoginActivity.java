package com.example.second_generation_house;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button btnLogin;
    private Button btnRegister;
    private ImageButton btnGoogle;
    private ConnectToBd connectToBd;
    private CheckUser checkUser;
    private AppDataBase appDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= 22) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        editTextLogin = findViewById(R.id.EditTextLogin);
        editTextPassword = findViewById(R.id.EditTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoogle = findViewById(R.id.btnGoogle);
        connectToBd = new ConnectToBd(this);
        checkUser = new CheckUser(this);
        appDataBase = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "userDB").allowMainThreadQueries().build();
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                HashMap<String, String> params = new HashMap<>();
                String entered_login = editTextLogin.getText().toString();
                String entered_password = editTextPassword.getText().toString();
                params.put(Keys.LoginActivity_Login, entered_login);
                params.put(Keys.LoginActivity_Password, entered_password);
                signIn(params, Links.LoginActivity_Login);
                break;
            case R.id.btnRegister:
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btnGoogle:
                break;
        }
    }

    private void signIn(final HashMap<String, String> params, final String URL) {
        if(!HasConnection.hasConnection(this)){
            toastText("Check your connection to the Internet!");
            return;
        }
        connectToBd.postToBd(URL, params, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getString(Keys.LoginActivity_Success).equals("true")) {
                        User user = new User();
                        user.setId(0);
                        user.setEmail(result.getString(Keys.LoginActivity_Email));
                        user.setKey(result.getString(Keys.RegisterActivity_KeyId));
                        user.setPassword(params.get(Keys.LoginActivity_Password));
                        appDataBase.myDao().addUser(user);
                        checkUser.setLoggedIn(true);
                        goToActivity(new Intent(LoginActivity.this, MenuActivity.class));
                    } else {
                        toastText("Incorrect login or password!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                toastText("Sorry, we have got a problem!");
            }
        });
    }

    private void toastText(String text) {
        Toast.makeText(LoginActivity.this, text, Toast.LENGTH_LONG).show();
    }

    private void goToActivity(Intent i) {
        startActivity(i);
        finish();
    }
}
