package com.chaos.starke.core;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.chaos.starke.R;
import com.chaos.starke.adapters.ActionAdapter;
import com.chaos.starke.adapters.items.ActionEntry;
import com.chaos.starke.adapters.items.ActionHeader;
import com.chaos.starke.adapters.items.ActionInterface;
import com.chaos.starke.models.Action;
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
import java.util.Random;

public class ActivitiesActivity extends ListActivity {

    private final ActivitiesActivity context = this;

    private final int MAXIMUM_HORIZONTAL_AMOUNT = 100;

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
        setupAccelerationGraph(this, (LinearLayout)findViewById(R.id.acceleration));

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

        testHandler.post(testRunnable);

    }

    private void setupActionBar(ActionBar actionBar) {
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupAccelerationGraph(Context context, LinearLayout container) {
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

    public void addAccelerationData(double value) {
        GraphView.GraphViewData graphViewData = new GraphView.GraphViewData(currentIndex + 1, value);
        accelerationDataSeries.appendData(graphViewData, true, MAXIMUM_HORIZONTAL_AMOUNT);
        currentIndex++;
    }

    Handler testHandler = new Handler();
    Runnable testRunnable = new Runnable() {

        @Override
        public void run() {
            double data = new Random().nextInt(accelerationGraph.getHeight());
            addAccelerationData(data);
            testHandler.postDelayed(this, 100);
        }
    };

}
