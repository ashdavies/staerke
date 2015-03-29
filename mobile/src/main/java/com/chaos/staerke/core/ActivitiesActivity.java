package com.chaos.staerke.core;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import com.chaos.staerke.R;
import com.chaos.staerke.adapters.ActionAdapter;
import com.chaos.staerke.adapters.items.ActionEntry;
import com.chaos.staerke.adapters.items.ActionHeader;
import com.chaos.staerke.adapters.items.ActionInterface;
import com.chaos.staerke.models.Action;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.InjectView;

public class ActivitiesActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks {
    private final ActivitiesActivity context = this;
    private GoogleApiClient googleApiClient;

    private ActionAdapter adapter;
    private List<ActionInterface> items;

    @InjectView(R.id.list)
    protected ListView list;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setupActivities();

        this.setupWearListener(this);
    }

    @Override
    public int onCreateViewId() {
        return R.layout.activity_activities;
    }

    private void setupActivities() {
        final SimpleDateFormat format = new SimpleDateFormat("cccc dd LLLL", Locale.GERMANY);

        this.items = new ArrayList<>();
        final List<Action> actions = Select.from(Action.class).orderBy("date DESC").list();
        long last = 0;

        for (final Action action : actions) {
            if (last == 0 || last > action.date / 86400000) {
                this.items.add(new ActionHeader(format.format(new Date(action.date))));
            }
            last = action.date / 86400000;
            this.items.add(new ActionEntry(action));
        }

        this.adapter = new ActionAdapter(this.context, this.items);
        this.list.setAdapter(this.adapter);
    }

    private void setupWearListener(final ActivitiesActivity context) {
        this.googleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
        this.googleApiClient.registerConnectionCallbacks(context);
        this.googleApiClient.connect();
    }

    @Override
    public void onConnected(final Bundle bundle) {
        Wearable.MessageApi.addListener(this.googleApiClient, new MessageApi.MessageListener() {
            @Override
            public void onMessageReceived(final MessageEvent messageEvent) {
                Log.i(getPackageName(), "Received value: " + messageEvent.getPath());
                final Double value = Double.valueOf(messageEvent.getPath());
                if (value > 0) {
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(final int i) {
    }
}
