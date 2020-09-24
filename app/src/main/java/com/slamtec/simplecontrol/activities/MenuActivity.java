package com.slamtec.simplecontrol.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.slamtec.simplecontrol.config.AppData;
import com.slamtec.simplecontrol.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Context context = this;
        AppData data = new AppData(context);

        String ip = data.getIpAddress();

        TextView tvIp = findViewById(R.id.tvIp);
        tvIp.setText(ip);

        Button button_change_ip = findViewById(R.id.btnChange);
        button_change_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ConnectActivity.class));
            }
        });

        Button btnBasic, btnGoTo;

        btnBasic = findViewById(R.id.btnBasic);
        btnGoTo = findViewById(R.id.btnGoTo);

        btnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, BasicControlsActivity.class));
            }
        });

        btnGoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, PointsActivity.class));
            }
        });

    }
}