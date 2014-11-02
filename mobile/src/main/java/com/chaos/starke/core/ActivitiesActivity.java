package com.chaos.starke.core;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import com.chaos.starke.R;
import com.chaos.starke.adapters.ActionAdapter;
import com.chaos.starke.adapters.items.ActionEntry;
import com.chaos.starke.adapters.items.ActionHeader;
import com.chaos.starke.adapters.items.ActionInterface;
import com.chaos.starke.models.Action;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivitiesActivity extends ListActivity implements GoogleApiClient.ConnectionCallbacks {

    private final ActivitiesActivity context = this;

    private final int MAXIMUM_HORIZONTAL_AMOUNT = 100;

    private GoogleApiClient googleApiClient;

    private GraphViewSeries accelerationDataSeries;
    private GraphView accelerationGraph;
    private int currentIndex = 6;

    private ActionAdapter adapter;
    private List<ActionInterface> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        //setupActionBar(getActionBar());
        setupAccelerationGraph(this, (LinearLayout) findViewById(R.id.acceleration));
        setupActivities();

        setupWearListener(this);

    }

    private void setupActionBar(final ActionBar actionBar) {
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupAccelerationGraph(final Context context, final LinearLayout container) {
        accelerationDataSeries = new GraphViewSeries(new GraphView.GraphViewData[]{});
        accelerationGraph = new LineGraphView(context, context.getString(R.string.activity_activities));
        accelerationGraph.addSeries(accelerationDataSeries);
        accelerationGraph.setScrollable(true);
        accelerationGraph.setShowHorizontalLabels(false);
        accelerationGraph.getGraphViewStyle().setGridStyle(GraphViewStyle.GridStyle.HORIZONTAL);
        accelerationGraph.setViewPort(1, MAXIMUM_HORIZONTAL_AMOUNT);
        accelerationGraph.setScalable(true);
        container.addView(accelerationGraph);
    }

    private void setupActivities() {
        SimpleDateFormat format = new SimpleDateFormat("cccc dd LLLL", Locale.GERMANY);

        items = new ArrayList<ActionInterface>();
        List<Action> actions = Select.from(Action.class).orderBy("date DESC").list();
        long last = 0;

        for (Action action : actions) {
            if (last == 0 || last > action.date / 86400000) {
                items.add(new ActionHeader(format.format(new Date(action.date))));
            }
            last = action.date / 86400000;
            items.add(new ActionEntry(action));
        }

        adapter = new ActionAdapter(context, items);
        setListAdapter(adapter);
    }

    private void setupWearListener(final ActivitiesActivity context) {
        googleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
        googleApiClient.registerConnectionCallbacks(context);
        googleApiClient.connect();
    }

    public void addAccelerationData(double value) {
        GraphView.GraphViewData graphViewData = new GraphView.GraphViewData(currentIndex + 1, value);
        accelerationDataSeries.appendData(graphViewData, true, MAXIMUM_HORIZONTAL_AMOUNT);
        currentIndex++;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(googleApiClient, new MessageApi.MessageListener() {
            @Override
            public void onMessageReceived(MessageEvent messageEvent) {
                Log.i(getPackageName(), "Received value: " + messageEvent.getPath());
                final Double value = Double.valueOf(messageEvent.getPath());
                if (value > 0) {
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            addAccelerationData(value);
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
