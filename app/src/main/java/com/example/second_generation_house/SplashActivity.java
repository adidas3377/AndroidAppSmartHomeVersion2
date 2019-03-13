package com.example.second_generation_house;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.second_generation_house.DataBase.AppDataBase;
import com.example.second_generation_house.DataBase.CheckUser;
import com.example.second_generation_house.DataBase.User;

import java.util.List;

public class SplashActivity extends AppCompatActivity {


    private TextView welcomeTextView;
    private TextView makeTextView;
    private CheckUser checkUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= 22) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        checkUser = new CheckUser(this);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        makeTextView = findViewById(R.id.makeTextView);
        welcomeTextView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_anim1));
        makeTextView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_anim2));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkUser.getLoggedIn()) {
                    goToActivity(new Intent(SplashActivity.this, MenuActivity.class));
                } else {
                    goToActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }

            }
        }, 5 * 1000);
    }

    private void goToActivity(Intent intent) {
        startActivity(intent);
        finish();
    }
}
