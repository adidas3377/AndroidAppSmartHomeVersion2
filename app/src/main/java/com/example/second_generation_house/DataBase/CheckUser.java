package com.example.second_generation_house.DataBase;

import android.content.Context;
import android.content.SharedPreferences;



public class CheckUser {

    private SharedPreferences sharedPreferences;
    public static final String SP_NAME = "userDetails";

    public CheckUser(Context context){
        sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
    }

    public void setLoggedIn(boolean s){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putBoolean("check", s);
        spEditor.apply();
    }
    public boolean getLoggedIn(){
        return sharedPreferences.getBoolean("check",false);
    }
}
