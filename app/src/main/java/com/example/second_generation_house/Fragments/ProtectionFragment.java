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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProtectionFragment extends Fragment implements View.OnClickListener{


    private int state = 0;
    private Context context;
    private ImageView btnProtection;
    private TextView textView;

    public ProtectionFragment() {
    }

    @SuppressLint("ValidFragment")
    public ProtectionFragment(Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_protection, container, false);
        btnProtection = view.findViewById(R.id.btnProtection);
        btnProtection.setOnClickListener(this);
        state = MenuActivity.protection_state;
        textView = view.findViewById(R.id.textViewInfoProtection);
        setProtection(state);
        return view;
    }

    private void setProtection(int state){
        if(state == 0){
            btnProtection.setImageResource(R.drawable.ic_icon_new_locked_off);
            textView.setText("System is off");
        }else{
            btnProtection.setImageResource(R.drawable.ic_icon_new_locked_on);
            textView.setText("System is working...");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnProtection:
                HashMap<String, String> params = new HashMap<>();
                if (state == 0) {
                    params.put(Keys.MenuActivity_getProtetionState, "1");
                    state = 1;
                } else {
                    params.put(Keys.MenuActivity_getProtetionState, "0");
                    state = 0;
                }
                params.put(Keys.MenuActivity_SmarthomeKey, Keys.MenuActivity_SmarthomeValue);
                AppDataBase appDataBase = Room.databaseBuilder(context,
                        AppDataBase.class, "userDB").allowMainThreadQueries().build();
                final List<User> l = appDataBase.myDao().getAll();
                params.put(Keys.RegisterActivity_KeyId, l.get(0).getKey());
                ConnectToBd connectToBd = new ConnectToBd(context);
                Toast.makeText(context, String.valueOf(state), Toast.LENGTH_LONG).show();
                setProtection(state);
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
