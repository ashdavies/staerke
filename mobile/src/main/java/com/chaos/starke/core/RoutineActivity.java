package com.chaos.starke.core;

import android.app.ActionBar;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.chaos.starke.R;
import com.chaos.starke.adapters.ActivityAdapter;
import com.chaos.starke.db.Routine;
import com.chaos.starke.db.Workout;
import com.chaos.starke.dialogs.CreateActivityDialog;
import com.chaos.starke.services.WorkoutService;
import com.chaos.starke.services.WorkoutService.ServiceBinding;

public class RoutineActivity extends FragmentActivity implements OnClickListener {

    // Workout service binding
    private WorkoutService workoutService;
    private ServiceConnection workoutServiceConnection;

    // Store final reference to activity
    private RoutineActivity context = this;

    // Card array adapter
    private ActivityAdapter adapter;
    private ListView list;
    private View header;

    // Intent
    Intent intent;

    // Routine identifier
    private Routine routine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create activity and set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_detail);

        // Update action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setHomeButtonEnabled(true);

        // Store intent and routine
        intent = getIntent();
        if (intent.hasExtra("routine")) {
            routine = Routine.findById(Routine.class, intent.getLongExtra("routine", 0));
        }

        // Bind workout service
        workoutServiceConnection = WorkoutService.bind(context, new ServiceBinding() {
            @Override
            public void onConnected(WorkoutService service) {

                workoutService = service;

                // Update workout state
                if (WorkoutService.Status() == true) {
                    visible(header, new int[]{R.id.pause, R.id.stop}, true);
                    visible(header, new int[]{R.id.start}, false);
                    visible(null, new int[]{R.id.create}, false);
                }

                // Check for routine id
                Workout workout = workoutService.getWorkout();
                if (workout != null && workout.routine != null) {
                    routine = Routine.findById(Routine.class, workout.routine);
                    update();
                }

                // Check for intent action
                if (intent.hasExtra("action")) {
                    WorkoutService.Action action = (WorkoutService.Action) intent.getSerializableExtra("action");
                    if (action == WorkoutService.Action.Start) {
                        start(null);
                    } else if (action == WorkoutService.Action.Pause) {
                        pause();
                    } else if (action == WorkoutService.Action.Stop) {
                        stop();
                    }
                }

            }

            @Override
            public void onDisconnected() {
                workoutService = null;
            }
        });

        // Get list view and create array adapter
        list = (ListView) findViewById(R.id.cards);

        // Create header and get button elements and assign click listeners
        header = getLayoutInflater().inflate(R.layout.card_activity_header, list, false);
        header.findViewById(R.id.start).setOnClickListener(this);
        header.findViewById(R.id.pause).setOnClickListener(this);
        header.findViewById(R.id.resume).setOnClickListener(this);
        header.findViewById(R.id.stop).setOnClickListener(this);
        list.addHeaderView(header);
        update();

        // Listen for create button
        findViewById(R.id.create).setOnClickListener(this);

    }

    /**
     * Update card list view
     */
    public void update() {

        // Check routine and list
        if (routine == null) {
            return;
        } else if (list == null) {
            return;
        }

        // Create new activity adapter using database fetch
        adapter = new ActivityAdapter(context, com.chaos.starke.db.RoutineActivity.find(com.chaos.starke.db.RoutineActivity.class, "routine = ?", new String[]{routine.getId().toString()}));

        // Update list adapter
        list.setAdapter(adapter);
        list.setOnItemClickListener(adapter);

    }

    /**
     * Release service connection
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (workoutServiceConnection != null) {
            unbindService(workoutServiceConnection);
            workoutServiceConnection = null;
            workoutService = null;
        }

    }

    /**
     * Handle on click events
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // Create new dialog to create routine
            case R.id.create:

                // Create routine
                CreateActivityDialog dialog = new CreateActivityDialog(routine, null);
                dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
                return;

            // Start a new workout
            case R.id.start:
                if (routine != null) {
                    start(routine.getId());
                }
                return;

            case R.id.pause:
                pause();
                return;

            case R.id.resume:
                resume();
                return;

            // Stop a workout
            case R.id.stop:
                stop();
                return;

        }
    }

    /**
     * Start workout
     */
    public boolean start(Long routine) {

        // Start workout
        if (workoutService == null || !workoutService.start(routine)) {
            return false;
        }

        // Toggle element visibility
        visible(header, new int[]{R.id.pause, R.id.stop}, true);
        visible(header, new int[]{R.id.start, R.id.resume}, false);
        visible(null, new int[]{R.id.create}, false);
        return true;

    }

    /**
     * Pause workout
     */
    public boolean pause() {

        // Pause workout
        if (workoutService == null || !workoutService.pause()) {
            return false;
        }

        // Toggle element visibility
        visible(header, new int[]{R.id.resume}, true);
        visible(header, new int[]{R.id.pause}, false);
        return true;

    }

    /**
     * Resume workout
     */
    public boolean resume() {

        // Resume workout
        if (workoutService == null || !workoutService.start(routine.getId())) {
            return false;
        }

        // Toggle element visibility
        visible(header, new int[]{R.id.pause}, true);
        visible(header, new int[]{R.id.resume}, false);

        return true;

    }

    /**
     * Stop workout
     */
    public boolean stop() {

        // Stop workout
        if (workoutService == null || !workoutService.stop()) {
            return false;
        }

        // Toggle element visibility
        visible(header, new int[]{R.id.start}, true);
        visible(header, new int[]{R.id.pause, R.id.resume, R.id.stop}, false);
        visible(null, new int[]{R.id.create}, true);

        return true;

    }

    /**
     * Inflate the action bar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Handle menu item selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.log:
                context.startActivity(new Intent(context, ActionActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Toggle the visibility on a set of resources
     *
     * @param ids
     * @param visible
     */
    public void visible(View parent, int[] ids, boolean visible) {
        for (int id : ids) {
            View view = parent == null ? findViewById(id) : parent.findViewById(id);
            if (view != null) {
                view.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }
    }

}
