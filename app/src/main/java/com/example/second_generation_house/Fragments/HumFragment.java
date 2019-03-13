package com.example.second_generation_house.Fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.second_generation_house.MenuActivity;
import com.example.second_generation_house.R;

public class HumFragment extends Fragment {


    public HumFragment() {
        // Required empty public constructor
    }

    private int hum = 0;
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private static TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hum, container, false);
        progressBar = view.findViewById(R.id.progressBar2);
        textView = view.findViewById(R.id.textViewHum);
        Temp temp1 = new Temp();
        hum = MenuActivity.hum;
        temp1.execute(hum);
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
            String s = values[0]+ "%";
            textView.setText(s);
            progressBar.setProgress(values[0]);
        }
    }

}
