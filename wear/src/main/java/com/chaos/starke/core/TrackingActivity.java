package com.chaos.starke.core;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.chaos.starke.R;

public class TrackingActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private TrackingClient trackingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setupTrackingService();
        setupSensors(sensorManager);
        registerSensorListeners(sensorManager, this);
    }

    private void setupTrackingService() {
        trackingClient = new TrackingClient(this);
    }

    private void setupSensors(SensorManager sensorManager) {
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void registerSensorListeners(SensorManager sensorManager, SensorEventListener sensorEventListener) {
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        trackingClient.send(sensorEvent.sensor.getType(), sensorEvent.accuracy, sensorEvent.timestamp, sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
