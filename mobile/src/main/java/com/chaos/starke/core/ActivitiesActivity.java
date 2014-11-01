package com.chaos.starke.core;

import android.app.ActionBar;
import android.app.ListActivity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.chaos.starke.R;
import com.chaos.starke.adapters.ActionAdapter;
import com.chaos.starke.adapters.items.ActionEntry;
import com.chaos.starke.adapters.items.ActionHeader;
import com.chaos.starke.adapters.items.ActionInterface;
import com.chaos.starke.models.Action;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ActivitiesActivity extends ListActivity {

    private final ActivitiesActivity context = this;

    private static final int MAXIMUM_HORIZONTAL_SIZE = 100;

    private SurfaceView accelerationGraph;
    private SurfaceHolder accelerationGraphHolder;
    private final Paint accelerationPainter = new Paint();
    private final List<Double> accelerationData = new ArrayList<Double>();

    private ActionAdapter adapter;
    private List<ActionInterface> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        //setupActionBar(getActionBar());
        setupAccelerationGraph((SurfaceView) findViewById(R.id.acceleration));

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

    private void setupAccelerationGraph(SurfaceView accelerationGraph) {
        this.accelerationGraph = accelerationGraph;
        accelerationGraphHolder = accelerationGraph.getHolder();
        int color = context.getResources().getColor(android.R.color.white);
        accelerationPainter.setColor(color);
    }

    public void addAccelerationData(double value) {
        accelerationData.add(value);
        if (accelerationData.size() >= MAXIMUM_HORIZONTAL_SIZE) {
            accelerationData.remove(0);
        }
        Log.i(getPackageName(), "data:" + accelerationData.size() + "x" + value);
    }

    private void updateAccelerationGraph() {

        Canvas canvas = accelerationGraphHolder.lockCanvas();
        int height = accelerationGraph.getHeight();
        int tick = accelerationGraph.getWidth() / MAXIMUM_HORIZONTAL_SIZE;

        canvas.drawColor(Color.BLACK);
        for (int i = 0; i < accelerationData.size(); i++) {
            int index = i == accelerationData.size() - 1 ? i : i + 1;
            Point from = new Point((i + 1) * tick, (int) (height - accelerationData.get(i)));
            Point to = new Point((i + 2) * tick, (int) (height - accelerationData.get(index)));
            canvas.drawLine(from.x, from.y, to.x, to.y, accelerationPainter);
        }

        accelerationGraphHolder.unlockCanvasAndPost(canvas);

    }

    Handler testHandler = new Handler();
    Runnable testRunnable = new Runnable() {

        @Override
        public void run() {
            double data = new Random().nextInt(accelerationGraph.getHeight());
            addAccelerationData(data);
            updateAccelerationGraph();
            testHandler.postDelayed(this, 100);
        }
    };

}
