package com.slamtec.simplecontrol.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.slamtec.simplecontrol.config.AppData;
import com.slamtec.simplecontrol.R;

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        TextView button = findViewById(R.id.btnSave);
        final EditText etIp = findViewById(R.id.etIpAddress);

        final Context context = this;
        final AppData appData = new AppData(context);

        etIp.setText(appData.getIpAddress());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = etIp.getText().toString().trim();
                if (ip.isEmpty()) {
                    Toast.makeText(context, "Please enter IP Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                appData.setIpAddress(ip);
                Intent intent = new Intent(context, BasicControlsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ConnectActivity.this.finish();

            }
        });

    }
}