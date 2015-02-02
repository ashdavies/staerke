package com.chaos.starke.core;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.wearable.activity.InsetActivity;
import android.widget.TextView;

import com.chaos.starke.R;

public class TrackingActivity extends InsetActivity implements MovementDetector.RepListener {
    private SensorManager manager;
    private Sensor sensor;

    private MovementDetector detector;

    private TextView reading;

    private int steps = 0;

    @Override
    public void onReadyForContent() {
        setContentView(R.layout.activity_tracking);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setupSensors(manager);
        setupDetector();
        setupViews();

        registerSensorListeners(manager, detector);
    }

    private void setupDetector() {
        detector = new MovementDetector();
        detector.setSensitivity(10.0f);
        detector.addRepListener(this);
    }

    private void setupViews() {
        reading = (TextView) findViewById(R.id.reading);
    }

    private void setupSensors(SensorManager sensorManager) {
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void registerSensorListeners(SensorManager sensorManager, SensorEventListener sensorEventListener) {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onRep() {
        steps++;
        reading.setText("S:" + steps);
    }
}