package com.chaos.starke.core;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.wearable.activity.InsetActivity;
import android.widget.TextView;

import com.chaos.starke.R;

public class TrackingActivity extends InsetActivity implements SensorEventListener {
    private static final int DEFAULT_SPEED_THRESHOLD = 20;
    private static final double DEFAULT_THRESHOLD_MARGIN = 0.8;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private Pt lastPoint;

    private double speedThreshold = DEFAULT_SPEED_THRESHOLD;
    private double speedTotal = 0;

    private int peaks = 0;

    private TextView reading;

    @Override
    public void onReadyForContent() {
        setContentView(R.layout.activity_tracking);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setupSensors(sensorManager);
        registerSensorListeners(sensorManager, this);

        reading = (TextView) findViewById(R.id.reading);
    }

    private void setupSensors(SensorManager sensorManager) {
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void registerSensorListeners(SensorManager sensorManager, SensorEventListener sensorEventListener) {
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Pt point = new Pt(sensorEvent.values);
        if (lastPoint == null) {
            lastPoint = point;
        }

        float speed = Math.abs(point.sum() - lastPoint.sum());
        speedTotal += speed;

        if (speed > speedThreshold) {
            peaks++;
        }

        double averagePeak = speedTotal / peaks;
        speedThreshold = averagePeak * DEFAULT_THRESHOLD_MARGIN;

        reading.setText("P:" + peaks);
        lastPoint = point;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class Pt {
        private float x = 0;
        private float y = 0;
        private float z = 0;

        public float getX() {
            return this.x;
        }

        public float getY() {
            return this.y;
        }

        public float getZ() {
            return this.z;
        }

        public Pt(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Pt(float[] values) {
            if (values.length != 3) {
                throw new RuntimeException("Invalid number of values");
            }

            this.x = values[0];
            this.y = values[1];
            this.z = values[2];
        }

        public float sum() {
            return this.x + this.y + this.z;
        }
    }

}