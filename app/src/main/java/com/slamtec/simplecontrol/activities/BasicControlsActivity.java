package com.slamtec.simplecontrol.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.slamtec.simplecontrol.config.AppData;
import com.slamtec.simplecontrol.R;
import com.slamtec.simplecontrol.config.DataProcessor;
import com.slamtec.simplecontrol.config.MapPoint;
import com.slamtec.slamware.AbstractSlamwarePlatform;
import com.slamtec.slamware.action.IMoveAction;
import com.slamtec.slamware.action.MoveDirection;
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

import static com.slamtec.simplecontrol.config.Constants.PORT;


public class BasicControlsActivity extends AppCompatActivity implements View.OnClickListener {

    private AbstractSlamwarePlatform robotPlatform;
    private IMoveAction moveAction;
    private TextView tvLocation;
    private EditText etName;
    private Context context;
    private Location location;
    private DataProcessor processor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_controls);

        context = this;
        AppData data = new AppData(context);
        processor = new DataProcessor(context);

        String ip = data.getIpAddress();

        /* 连接底盘 */
        try {
            robotPlatform = DeviceManager.connect(ip, PORT);
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Cannot Connect to robot. please check ip address and connect robot in same network", Toast.LENGTH_LONG).show();
            return;
        }

        tvLocation = findViewById(R.id.tvLocation);
        etName = findViewById(R.id.etName);

        Button button_get_location = findViewById(R.id.button_get_location);
        Button button_save_location = findViewById(R.id.button_save);


        Button button_stop = findViewById(R.id.button_stop);
        Button button_forward = findViewById(R.id.button_forward);
        Button button_backward = findViewById(R.id.button_backward);
        Button button_turn_left = findViewById(R.id.button_turn_left);
        Button button_turn_right = findViewById(R.id.button_turn_right);

        button_stop.setOnClickListener(this);
        button_forward.setOnClickListener(this);
        button_backward.setOnClickListener(this);
        button_turn_left.setOnClickListener(this);
        button_turn_right.setOnClickListener(this);
        button_backward.setOnClickListener(this);
        button_save_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLocation();
            }
        });
        button_get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocation();
            }
        });

    }

    private void saveLocation() {
        if (location == null) {
            Toast.makeText(context, "Please get current location first", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(context, "enter location name", Toast.LENGTH_SHORT).show();
            etName.setError("Enter Name");
            return;
        }

        MapPoint point = new MapPoint(name, location.getX(), location.getX(), location.getZ(), 0);
        processor.savePoint(point);
    }

    @Override
    public void onClick(View view) {

        try{
            switch (view.getId()) {

                case R.id.button_stop:
                    if(moveAction != null) {
                        moveAction.cancel();
                    }
                    break;
                case R.id.button_forward:
                    moveAction = robotPlatform.moveBy(MoveDirection.FORWARD);
                    break;
                case R.id.button_backward:
                    moveAction = robotPlatform.moveBy(MoveDirection.BACKWARD);
                    break;
                case R.id.button_turn_left:
                    moveAction = robotPlatform.moveBy(MoveDirection.TURN_LEFT);
                    break;
                case R.id.button_turn_right:
                    moveAction = robotPlatform.moveBy(MoveDirection.TURN_RIGHT);
                    break;

                default:
                    break;
            }
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
        } catch (ParseInvalidException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (OperationFailException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showLocation() {
        try {
            location = robotPlatform.getLocation();
            String point = "Current Location\nX: "+location.getX()+"\nY: "+location.getY()+"\nZ: "+location.getZ();
            tvLocation.setText(point);
            return;
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
        } catch (ParseInvalidException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }

        location = null;
        String point = "Current Location\nX: -\nY: -\nZ:";
        tvLocation.setText(point);
        Toast.makeText(context, "Cannot get location", Toast.LENGTH_SHORT).show();
    }
}