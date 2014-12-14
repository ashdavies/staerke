package com.chaos.starke.core;

import android.content.Context;
import android.util.Log;

import com.chaos.starke.shared.SensorEntry;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TrackingClient {
    private static final int CONNECTION_TIMEOUT = 15000;

    private Context context;
    private GoogleApiClient googleApiClient;
    private ExecutorService executorService;

    public TrackingClient(Context context) {
        this.context = context;
        googleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
        executorService = Executors.newCachedThreadPool();
    }

    private boolean validateConnection() {
        if (googleApiClient.isConnected()) {
            return true;
        }

        ConnectionResult result = googleApiClient.blockingConnect(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        return result.isSuccess();
    }

    public void send(int sensorType, int accuracy, long timestamp, float[] values) {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/sensors/" + sensorType);

        dataMapRequest.getDataMap().putInt(SensorEntry.ACCURACY, accuracy);
        dataMapRequest.getDataMap().putLong(SensorEntry.TIMESTAMP, timestamp);
        dataMapRequest.getDataMap().putFloatArray(SensorEntry.VALUES, values);

        PutDataRequest putDataRequest = dataMapRequest.asPutDataRequest();
        send(putDataRequest);
    }

    private void send(final PutDataRequest putDataRequest) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                if (validateConnection()) {
                    Wearable.DataApi.putDataItem(googleApiClient, putDataRequest);
                }
            }
        });
    }
}
