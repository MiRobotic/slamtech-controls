package com.slamtec.simplecontrol.config;

import android.content.Context;
import android.content.SharedPreferences;

import static com.slamtec.simplecontrol.config.Constants.ROBOT_IP;

public class AppData {

    private SharedPreferences preferences;

    public AppData(Context context) {
        preferences = context.getSharedPreferences("conf", Context.MODE_PRIVATE);
    }

    public String getIpAddress(){
        return preferences.getString("ip", ROBOT_IP);
    }

    public void setIpAddress(String ip) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ip", ip);
        editor.apply();
    }

}
