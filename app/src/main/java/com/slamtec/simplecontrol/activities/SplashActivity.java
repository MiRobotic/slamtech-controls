package com.slamtec.simplecontrol.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.slamtec.simplecontrol.config.AppData;
import com.slamtec.simplecontrol.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        final Context context = this;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppData data = new AppData(context);
                if (data.getIpAddress().isEmpty()) {
                    startActivity(new Intent(context, ConnectActivity.class));
                }else {
                    startActivity(new Intent(context, MenuActivity.class));
                }
                SplashActivity.this.finish();
            }
        }, 1000);

    }
}