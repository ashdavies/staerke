package com.chaos.starke.core;

import android.content.Context;
import android.util.Log;
import android.util.SparseLongArray;

import com.chaos.starke.shared.Constants;
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

    private GoogleApiClient googleApiClient;
    private ExecutorService executorService;
    private SparseLongArray sensorData;

    private static final int CLIENT_CONNECTION_TIMEOUT = 15000;

    public TrackingClient(Context context) {
        googleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
        executorService = Executors.newCachedThreadPool();
        sensorData = new SparseLongArray();
    }

    public void send(final int sensorType, final int accuracy, final long timestamp, final float[] values) {

        long currentTime = System.currentTimeMillis();
        long lastTimestamp = sensorData.get(sensorType);
        long timeAgo = currentTime - lastTimestamp;

        if (lastTimestamp != 0 && timeAgo < 3000) {
            return;
        }

        sensorData.put(sensorType, currentTime);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                PutDataRequest putDataRequest = makeRequest(sensorType, accuracy, timestamp, values);
                if (connectionValid()) {
                    send(putDataRequest);
                }
            }
        });
    }

    private PutDataRequest makeRequest(int sensorType, int accuracy, long timestamp, float[] values) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/sensors/" + sensorType);

        dataMap.getDataMap().putInt(Constants.ACCURACY, accuracy);
        dataMap.getDataMap().putLong(Constants.TIMESTAMP, timestamp);
        dataMap.getDataMap().putFloatArray(Constants.VALUES, values);

        return dataMap.asPutDataRequest();
    }

    private boolean connectionValid() {
        if (googleApiClient.isConnected()) {
            return true;
        }

        return googleApiClient
                .blockingConnect(CLIENT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .isSuccess();
    }

    private void send(PutDataRequest putDataRequest) {
        Wearable.DataApi.putDataItem(googleApiClient, putDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                Log.v(TrackingClient.this.getClass().getCanonicalName(), "Sending data: " + dataItemResult.getStatus().isSuccess());
            }
        });
    }

    public void disconnect() {
        googleApiClient.disconnect();
        executorService.shutdown();
    }

}