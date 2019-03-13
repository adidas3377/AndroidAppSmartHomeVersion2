package com.example.second_generation_house.Fragments;


import android.annotation.SuppressLint;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.second_generation_house.ConnectionToBd.ConnectToBd;
import com.example.second_generation_house.ConnectionToBd.ServerCallback;
import com.example.second_generation_house.DataBase.AppDataBase;
import com.example.second_generation_house.DataBase.User;
import com.example.second_generation_house.KeysAndLinks.Keys;
import com.example.second_generation_house.KeysAndLinks.Links;
import com.example.second_generation_house.MenuActivity;
import com.example.second_generation_house.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


public class TempFragment extends Fragment {


    public TempFragment() {
    }


    private int temp = 0;
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private static TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temp, container, false);
        progressBar = view.findViewById(R.id.progressBar1);
        textView = view.findViewById(R.id.textViewTemp);
        temp = MenuActivity.temp;
        Temp temp1 = new Temp();
        temp1.execute(temp);
        return view;
    }


    private static class Temp extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... voids) {
            for (int i = 0; i <= voids[0]; i++) {
                publishProgress(i);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "a";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            String s = values[0] + "*C";
            textView.setText(s);
            progressBar.setProgress(values[0]);
        }
    }

}
