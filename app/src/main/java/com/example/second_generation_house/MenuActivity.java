package com.example.second_generation_house;

import android.arch.persistence.room.Room;
import android.media.Image;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.second_generation_house.Adapter.Adapter;
import com.example.second_generation_house.ConnectionToBd.ConnectToBd;
import com.example.second_generation_house.ConnectionToBd.ServerCallback;
import com.example.second_generation_house.DataBase.AppDataBase;
import com.example.second_generation_house.DataBase.User;
import com.example.second_generation_house.Fragments.HumFragment;
import com.example.second_generation_house.Fragments.LightFragment;
import com.example.second_generation_house.Fragments.ProtectionFragment;
import com.example.second_generation_house.Fragments.SocketFragment;
import com.example.second_generation_house.Fragments.TempFragment;
import com.example.second_generation_house.KeysAndLinks.Keys;
import com.example.second_generation_house.KeysAndLinks.Links;
import com.example.second_generation_house.NetWork.HasConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mPager;
    private Fragment[] fragmentPages;
    private ImageView tempButton;
    private ImageView humButton;
    private ImageView lightButton;
    private ImageView socketButton;
    private ImageView protectionButton;
    private ImageView btnRefresh;
    private ConnectToBd connectToBd;
    private TextView textViewName;
    public static int temp = 0;
    public static int hum = 0;
    public static int power_socket_state = 0;
    public static int protection_state = 0;
    private int p = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (Build.VERSION.SDK_INT >= 22) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        tempButton = (ImageView) findViewById(R.id.tempButton);
        humButton = (ImageView) findViewById(R.id.humButton);
        lightButton = (ImageView) findViewById(R.id.lightButton);
        socketButton = (ImageView) findViewById(R.id.socketButton);
        protectionButton = (ImageView) findViewById(R.id.protectionButton);
        btnRefresh = (ImageView) findViewById(R.id.btnRefresh);
        tempButton.setOnClickListener(this);
        humButton.setOnClickListener(this);
        socketButton.setOnClickListener(this);
        lightButton.setOnClickListener(this);
        protectionButton.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        connectToBd = new ConnectToBd(this);
        mPager = findViewById(R.id.mPager);
        textViewName = findViewById(R.id.textViewName);
        getData();

    }

    private void getData() {
        if (!HasConnection.hasConnection(MenuActivity.this)) {
            toastText("Check the connection to the internet");
            LinearLayout le1 = findViewById(R.id.barLayout);
            le1.setVisibility(View.VISIBLE);
            mPager.setVisibility(View.GONE);
            LinearLayout le = findViewById(R.id.IconsLayouts);
            le.setVisibility(View.GONE);
            return;
        }

        AppDataBase appDataBase = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "userDB").allowMainThreadQueries().build();
        final List<User> l = appDataBase.myDao().getAll();
        HashMap<String, String> params = new HashMap<>();
        params.put(Keys.LoginActivity_Email, l.get(0).getEmail());
        params.put(Keys.LoginActivity_KeyId, l.get(0).getKey());
        connectToBd.postToBd(Links.MenuActivity_GetData, params, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                LinearLayout le1 = findViewById(R.id.barLayout);
                le1.setVisibility(View.GONE);
                mPager.setVisibility(View.VISIBLE);
                LinearLayout le = findViewById(R.id.IconsLayouts);
                le.setVisibility(View.VISIBLE);
                temp = Integer.parseInt(result.getString(Keys.MenuActivity_getTemp));
                hum = Integer.parseInt(result.getString(Keys.MenuActivity_getHum));
                // power_socket_state = Integer.parseInt(result.getString(Keys.MenuActivity_getDataSocket));
                // protection_state = Integer.parseInt(result.getString(MenuActivity_getProtectionState));
                TempFragment tempFragment = new TempFragment();
                HumFragment humFragment = new HumFragment();
                LightFragment lightFragment = new LightFragment();
                SocketFragment socketFragment = new SocketFragment(getApplicationContext());
                ProtectionFragment protectionFragment = new ProtectionFragment(getApplicationContext());
                fragmentPages = new Fragment[]{tempFragment, humFragment, lightFragment, socketFragment, protectionFragment};
                Adapter adapter = new Adapter(getSupportFragmentManager(), fragmentPages);
                mPager.setAdapter(adapter);
                currentTheme(p);
                mPager.setCurrentItem(p);
                mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        currentTheme(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }

            @Override
            public void onFailure(String error) {
                toastText(error + "????");
            }
        });
    }

    private void currentTheme(int p) {
        tempButton.setImageResource(R.drawable.ic_icon_new_temp_off);
        humButton.setImageResource(R.drawable.ic_icon_new_hum_off);
        lightButton.setImageResource(R.drawable.ic_icon_new_light_off);
        socketButton.setImageResource(R.drawable.ic_icon_new_power_socket_off);
        protectionButton.setImageResource(R.drawable.ic_icon_new_protect_off);
        if (p == 0) {
            tempButton.setImageResource(R.drawable.ic_icon_new_temp_on);
            textViewName.setText("Temperature");

        } else if (p == 1) {
            humButton.setImageResource(R.drawable.ic_new_hum_on);
            textViewName.setText("Humidity");

        } else if (p == 2) {
            lightButton.setImageResource(R.drawable.ic_icon_new_light_on);
            textViewName.setText("Light");
        } else if (p == 3) {
            socketButton.setImageResource(R.drawable.ic_icon_new_power_socket_on);
            textViewName.setText("Power socket");

        } else if (p == 4) {
            protectionButton.setImageResource(R.drawable.ic_icon_new_protect_on);
            textViewName.setText("Protection");

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tempButton:
                mPager.setCurrentItem(0);
                return;
            case R.id.humButton:
                mPager.setCurrentItem(1);
                return;
            case R.id.lightButton:
                mPager.setCurrentItem(2);
                return;
            case R.id.socketButton:
                mPager.setCurrentItem(3);
                return;
            case R.id.protectionButton:
                mPager.setCurrentItem(4);
                return;
            case R.id.btnRefresh:
                p = mPager.getCurrentItem();
                getData();
        }
    }

    private void toastText(String text) {
        Toast.makeText(MenuActivity.this, text, Toast.LENGTH_LONG).show();
    }

    public void socketChange(HashMap<String, String> params) {

        connectToBd.postToBd(Links.MenuActivity_Handler, params, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {

            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

}
