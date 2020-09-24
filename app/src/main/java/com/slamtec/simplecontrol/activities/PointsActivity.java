package com.slamtec.simplecontrol.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.slamtec.simplecontrol.PointsAdapter;
import com.slamtec.simplecontrol.R;
import com.slamtec.simplecontrol.config.AppData;
import com.slamtec.simplecontrol.config.DataProcessor;
import com.slamtec.simplecontrol.config.MapPoint;
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
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.MoveOption;

import java.util.ArrayList;

import static com.slamtec.simplecontrol.config.Constants.PORT;

public class PointsActivity extends AppCompatActivity {

    private Context context;
    private DataProcessor processor;
    private PointsAdapter pointsAdapter;
    private RecyclerView rvList;
    private IMoveAction moveAction;
    private AbstractSlamwarePlatform robotPlatform;

    private final static String TAG = "Points";

    private PointsAdapter.OnPointClickListener clickListener = new PointsAdapter.OnPointClickListener() {
        @Override
        public void onNavigate(final MapPoint point) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    goToPoint(point);
                }
            });
        }

        @Override
        public void onDelete(MapPoint point, int position) {
            deletePoint(point, position);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        context = this;

        AppData data = new AppData(context);

        String ip = data.getIpAddress();

        /* 连接底盘 */
        try {
            robotPlatform = DeviceManager.connect(ip, PORT);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Cannot Connect to robot. please check ip address and connect robot in same network", Toast.LENGTH_LONG).show();
        }

        processor = new DataProcessor(context);

        rvList = findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(context));
        rvList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        //processor.savePoint(new MapPoint("Test", 1,1,1,1));

        Button btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moveAction != null) {
                    try {
                        moveAction.cancel();
                    } catch (RequestFailException e) {
                        e.printStackTrace();
                    } catch (ConnectionFailException e) {
                        e.printStackTrace();
                    } catch (ConnectionTimeOutException e) {
                        e.printStackTrace();
                    } catch (UnauthorizedRequestException e) {
                        e.printStackTrace();
                    } catch (UnsupportedCommandException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        showPointList();

    }

    private void showPointList() {
        ArrayList<MapPoint> list = processor.getSavedPoints();
        pointsAdapter = new PointsAdapter(list, clickListener);
        rvList.setAdapter(pointsAdapter);
    }

    private void deletePoint(final MapPoint point, final int position) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Point Location")
                .setMessage("Are you sure you want to delete this location?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        processor.deletePoint(point.getId());
                        pointsAdapter.removePoint(position);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void goToPoint(MapPoint point) {
        try {
            MoveOption moveOption = new MoveOption();

            moveOption.setPrecise(true);
            moveOption.setMilestone(true);

            Location location1 = new Location(point.getX(), point.getY(), point.getZ());

            if (robotPlatform != null) {
                moveAction = robotPlatform.moveTo(location1, moveOption, 0);
                moveAction.waitUntilDone();
            }else {
                Toast.makeText(context, "Error Occurred!", Toast.LENGTH_SHORT).show();
                return;
            }


            Log.d(TAG, "========== Virtual Track ==========");
            /* draw a virtual track from (0, 0) to (2, 0), then move to (0, 0) via virtual track */
            //robotPlatform.addLine(ArtifactUsageVirtualTrack, new Line(new PointF(0, 0), new PointF(2, 1)));
            //moveOption.setKeyPoints(true);
            //moveOption.setPrecise(true);
            //moveAction = robotPlatform.moveTo(new Location(1.2f, 0, 0), moveOption, 0);

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
}