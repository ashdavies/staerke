package com.chaos.starke.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.chaos.starke.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackingService extends WearableListenerService {

    private static final int ONGOING_NOTIFICATION_ID = 16;

    public static final String PATH_NOTIFICATION = "/ongoingnotification";
    public static final String PATH_DISMISS = "/dismissnotification";

    private GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        googleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        googleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();

        if (!googleApiClient.isConnected()) {
            ConnectionResult connectionResult = googleApiClient.blockingConnect(30, TimeUnit.SECONDS);
            if (!connectionResult.isSuccess()) {
                Log.e(getPackageName(), "Service failed to connect to GoogleApiClient.");
                return;
            }
        }

        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (PATH_NOTIFICATION.equals(path)) {

                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    final String title = dataMapItem.getDataMap().getString(TrackingActivity.KEY_EXTRA_TITLE);
                    Asset asset = dataMapItem.getDataMap().getAsset(TrackingActivity.KEY_EXTRA_IMAGE);

                    Intent notificationIntent = new Intent(this, TrackingActivity.class);
                    notificationIntent.putExtra(TrackingActivity.KEY_EXTRA_TITLE, title);
                    notificationIntent.putExtra(TrackingActivity.KEY_EXTRA_IMAGE, asset);
                    PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                            this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification.Builder notificationBuilder = new Notification.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher_circle)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_circle))
                            .setOngoing(true)
                            .extend(new Notification.WearableExtender()
                                    .setDisplayIntent(notificationPendingIntent));

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(ONGOING_NOTIFICATION_ID, notificationBuilder.build());
                } else {
                    Log.d(getPackageName(), "Unrecognized path: " + path);
                }
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(PATH_DISMISS)) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(ONGOING_NOTIFICATION_ID);
        }
    }
}