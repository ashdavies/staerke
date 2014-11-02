package com.chaos.starke.core;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.wearable.activity.InsetActivity;

import com.chaos.starke.R;

public class TrackingActivity extends InsetActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor heartrateSensor;
    private Sensor significantMotionSensor;

    private TrackingClient trackingClient;

    @Override
    public void onReadyForContent() {
        setContentView(R.layout.activity_tracking);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setupWearClient(this);
        setupSensors(sensorManager);
        registerSensorListeners(sensorManager, this);
    }

    private void setupWearClient(Context context) {
        trackingClient = new TrackingClient(context);
    }

    private void setupSensors(SensorManager sensorManager) {
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        heartrateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        significantMotionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
    }

    private void registerSensorListeners(SensorManager sensorManager, SensorEventListener sensorEventListener) {
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, heartrateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, significantMotionSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        trackingClient.send(sensorEvent.sensor.getType(), sensorEvent.accuracy, sensorEvent.timestamp, sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        trackingClient.disconnect();
    }

}