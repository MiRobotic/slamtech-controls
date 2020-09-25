package com.slamtec.simplecontrol.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.slamtec.simplecontrol.config.AppData;
import com.slamtec.simplecontrol.R;
import com.slamtec.slamware.AbstractSlamwarePlatform;
import com.slamtec.slamware.action.ActionStatus;
import com.slamtec.slamware.action.IMoveAction;
import com.slamtec.slamware.discovery.DeviceManager;
import com.slamtec.slamware.exceptions.ConnectionFailException;
import com.slamtec.slamware.exceptions.ConnectionTimeOutException;
import com.slamtec.slamware.exceptions.InvalidArgumentException;
import com.slamtec.slamware.exceptions.OperationFailException;
import com.slamtec.slamware.exceptions.ParseInvalidException;
import com.slamtec.slamware.exceptions.RequestFailException;
import com.slamtec.slamware.exceptions.UnauthorizedRequestException;
import com.slamtec.slamware.exceptions.UnsupportedCommandException;
import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.geometry.PointF;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.MoveOption;

import static com.slamtec.simplecontrol.config.Constants.PORT;
import static com.slamtec.slamware.robot.ArtifactUsage.ArtifactUsageVirtualTrack;

public class GoToActivity extends AppCompatActivity {

    private AbstractSlamwarePlatform robotPlatform;

    private final String TAG = "GoToAction";
    private Context context;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_point);

        context = this;
        progressBar = findViewById(R.id.progressBar);

        AppData data = new AppData(context);

        String ip = data.getIpAddress();

        connectRobot(ip);

    }

    private void connectRobot(final String ip) {
        Log.e(TAG, "Connect on "+ip);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        /* 连接底盘 */
                        try {
                            robotPlatform = DeviceManager.connect(ip, PORT);
                            Log.e(TAG,"Robot Connected");
                            MyMessage.showData(GoToActivity.this, "Robot Connected");
                            hideProgressBar(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressBar(false);
                            Log.e(TAG, "Cannot Connect to robot. please check ip address and connect robot in same network");
                            MyMessage.showData(GoToActivity.this, "Cannot Connect to robot. please check ip address and connect robot in same network");

                        }
                    }
                }
        ).start();
    }

    private void hideProgressBar(final Boolean isConnected){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                if (!isConnected) {
                    return;
                }
                init();
            }
        });
    }

    private void init() {

        try {
            MoveOption moveOption = new MoveOption();

            moveOption.setPrecise(true);
            moveOption.setMilestone(true);

            Location location1 = new Location(0, 1, 0);
            Location location2 = new Location(1, 0, 0);
            Location location3 = new Location(0, 0, 0);

            IMoveAction moveAction = robotPlatform.moveTo(location3, moveOption, 0);
            moveAction.waitUntilDone();

            moveAction = robotPlatform.moveTo(location1, moveOption, 0);
            moveAction.waitUntilDone();

            moveAction = robotPlatform.moveTo(location2, moveOption, 0);
            moveAction.waitUntilDone();

            moveAction = robotPlatform.moveTo(location3, moveOption, 0);
            moveAction.waitUntilDone();

            Log.d(TAG, "========== Virtual Track ==========");
            /* draw a virtual track from (0, 0) to (2, 0), then move to (0, 0) via virtual track */
            robotPlatform.addLine(ArtifactUsageVirtualTrack, new Line(new PointF(0, 0), new PointF(2, 1)));

            moveOption.setKeyPoints(true);
            moveOption.setPrecise(true);
            moveAction = robotPlatform.moveTo(new Location(1.2f, 0, 0), moveOption, 0);
            moveAction.waitUntilDone();
            if (moveAction.getStatus() == ActionStatus.ERROR) {
                Log.d(TAG, "Action Failed: " + moveAction.getReason());
            }
        } catch (ConnectionTimeOutException e) {
            e.printStackTrace();
        } catch (UnsupportedCommandException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (ParseInvalidException e) {
            e.printStackTrace();
        } catch (ConnectionFailException e) {
            e.printStackTrace();
        } catch (RequestFailException e) {
            e.printStackTrace();
        } catch (UnauthorizedRequestException e) {
            e.printStackTrace();
        } catch (OperationFailException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (robotPlatform != null) {
            robotPlatform.disconnect();
        }
        super.onDestroy();
    }
}
