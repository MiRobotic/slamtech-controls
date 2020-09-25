package com.slamtec.simplecontrol.activities;

import android.app.Activity;
import android.widget.Toast;

public class MyMessage {

    static void showData(final Activity activity, final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
