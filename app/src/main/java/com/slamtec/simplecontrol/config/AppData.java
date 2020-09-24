package com.slamtec.simplecontrol.config;

import android.content.Context;
import android.content.SharedPreferences;

public class AppData {

    private SharedPreferences preferences;

    public AppData(Context context) {
        preferences = context.getSharedPreferences("conf", Context.MODE_PRIVATE);
    }

    public String getIpAddress(){
        return preferences.getString("ip", "192.168.11.1");
    }

    public void setIpAddress(String ip) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ip", ip);
        editor.apply();
    }

}
