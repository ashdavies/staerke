package com.chaos.starke.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.chaos.starke.R;
import com.chaos.starke.adapters.ActivityAdapter;
import com.chaos.starke.dialogs.CreateActivityDialog;
import com.chaos.starke.models.Activity;
import com.chaos.starke.models.Routine;
import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;

import java.util.List;

public class RoutineActivity extends ActionBarActivity implements OnClickListener {

    private ActivityAdapter activityAdapter;
    private ListView activityListView;

    private Routine routine;

    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        routine = getRoutineFromIntent(getIntent());
        activityAdapter = new ActivityAdapter(this, getActivitiesForRoutine(routine));

        setupActionButton();
        setupActivitiesList();
    }

    private void setupActionButton() {
        actionButton = (FloatingActionButton) findViewById(R.id.create);
        actionButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                CreateActivityDialog dialog = new CreateActivityDialog(routine, null);
                dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
            }
        });
    }

    private void setupActivitiesList() {
        activityListView = (ListView) findViewById(R.id.activities);
        activityListView.setAdapter(activityAdapter);
        activityListView.setOnItemClickListener(activityAdapter);
        activityListView.setOnItemLongClickListener(activityAdapter);
        activityListView.setOnTouchListener(new ShowHideOnScroll(actionButton));
    }

    private Routine getRoutineFromIntent(Intent intent) {
        long routineId = intent.getLongExtra("routine", 0);
        return Routine.findById(Routine.class, routineId);
    }

    private List<Activity> getActivitiesForRoutine(Routine routine) {
        String routineId = String.valueOf(routine.id);
        return Activity.find(Activity.class, "routine = ?", new String[]{routineId});
    }

}
