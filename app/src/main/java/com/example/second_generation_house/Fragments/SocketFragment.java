package com.example.second_generation_house.Fragments;


import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public class SocketFragment extends Fragment implements View.OnClickListener {


    private ImageView imageView;
    private int state = 0;
    private TextView textView;
    private Context context;

    public SocketFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SocketFragment(Context context) {
        this.context = context;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_socket, container, false);
        imageView = view.findViewById(R.id.btnPowerSocket);
        imageView.setOnClickListener(this);
        textView = view.findViewById(R.id.textViewInfoSocket);
        state = MenuActivity.power_socket_state;
        setPowerSocket(state);
        return view;
    }

    private void setPowerSocket(int state) {
        if (state == 0) {
            imageView.setImageResource(R.drawable.ic_icon_new_power_socket_state_off);
            textView.setText("The power socket is off");
        } else {
            imageView.setImageResource(R.drawable.ic_icon_new_power_socket_state_on);
            textView.setText("The power socket is active");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPowerSocket:
                HashMap<String, String> params = new HashMap<>();
                if (state == 0) {
                    params.put(Keys.MenuActivity_getDataSocket, "1");
                    state = 1;
                } else {
                    params.put(Keys.MenuActivity_getDataSocket, "0");
                    state = 0;
                }
                params.put(Keys.MenuActivity_SmarthomeKey, Keys.MenuActivity_SmarthomeValue);
                AppDataBase appDataBase = Room.databaseBuilder(context,
                        AppDataBase.class, "userDB").allowMainThreadQueries().build();
                final List<User> l = appDataBase.myDao().getAll();
                params.put(Keys.RegisterActivity_KeyId, l.get(0).getKey());
                ConnectToBd connectToBd = new ConnectToBd(context);
                Toast.makeText(context, String.valueOf(state), Toast.LENGTH_LONG).show();
                setPowerSocket(state);
                connectToBd.postToBd(Links.MenuActivity_Handler, params, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result) throws JSONException {
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                    }
                });
        }
    }
}
