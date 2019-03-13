package com.example.second_generation_house.ConnectionToBd;


import org.json.JSONException;
import org.json.JSONObject;

public interface ServerCallback {
    void onSuccess(JSONObject result) throws JSONException;

    void onFailure(String error);
}
