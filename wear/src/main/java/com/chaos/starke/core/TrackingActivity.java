package com.chaos.starke.core;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.InsetActivity;
import android.util.Log;

import com.chaos.starke.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class TrackingActivity extends InsetActivity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks {

    public static final String KEY_EXTRA_TITLE = "EXTRA_TITLE";
    public static final String KEY_EXTRA_IMAGE = "EXTRA_IMAGE";

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private GoogleApiClient googleApiClient;

    private Handler sensoreUpdateHandler = new Handler();
    private Runnable sensorUpdateRunnable = new Runnable() {
        @Override public void run() {
            Double acceleration = (double)accelerometerSensor.getPower();
            sendMessage(acceleration.toString(), null);
            sensoreUpdateHandler.postDelayed(sensorUpdateRunnable, 500);
        }
    };

    @Override
    public void onReadyForContent() {
        setContentView(R.layout.activity_tracking);

        setupGoogleApiClient(this);
        setupSensors(this);

    }

    private void setupGoogleApiClient(TrackingActivity context) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(context)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.i(TrackingActivity.class.getSimpleName(), "Connection failed");
                    }
                })
                .addApi(Wearable.API)
                .build();

        googleApiClient.connect();
    }

    private void setupSensors(TrackingActivity trackingActivity) {
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(googleApiClient, this);
        sensoreUpdateHandler.post(sensorUpdateRunnable);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TrackingActivity.class.getSimpleName(), "Connection failed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Wearable.MessageApi.removeListener(googleApiClient, this);
        googleApiClient.disconnect();
    }

    private void sendMessage(final String message, final byte[] payload) {
        Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                List<Node> nodes = getConnectedNodesResult.getNodes();
                for (Node node : nodes) {
                    Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), message, payload).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            Log.i(TrackingActivity.class.getSimpleName(), "WEAR Result " + sendMessageResult.getStatus());
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }
}