package com.chaos.stark.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.chaos.stark.R;
import com.chaos.stark.adapters.ActivityAdapter;
import com.chaos.stark.dialogs.CreateActivityDialog;
import com.chaos.stark.models.Activity;
import com.chaos.stark.models.Routine;
import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;

import java.util.List;

import butterknife.InjectView;

public class RoutineActivity extends BaseActivity implements OnClickListener {

    @InjectView(R.id.activities)
    protected ListView activityListView;

    @InjectView(R.id.action_button)
    protected FloatingActionButton actionButton;

    private Routine routine;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.routine = this.getRoutineFromIntent(this.getIntent());
        this.setTitle(this.routine.name);

        this.actionButton.setOnClickListener(this);

        final ActivityAdapter activityAdapter = new ActivityAdapter(this, this.getActivitiesForRoutine(this.routine));
        this.setupActivitiesList(activityAdapter);
    }

    @Override
    public int onCreateViewId() {
        return R.layout.activity_routine;
    }

    @Override
    public void onClick(final View view) {
        final CreateActivityDialog dialog = new CreateActivityDialog(RoutineActivity.this.routine, null);
        dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
    }

    private void setupActivitiesList(final ActivityAdapter activityAdapter) {
        this.activityListView.setAdapter(activityAdapter);
        this.activityListView.setOnItemClickListener(activityAdapter);
        this.activityListView.setOnItemLongClickListener(activityAdapter);
        this.activityListView.setOnTouchListener(new ShowHideOnScroll(this.actionButton));
    }

    private Routine getRoutineFromIntent(final Intent intent) {
        long routineId = intent.getLongExtra("routine", 0);
        return Routine.findById(Routine.class, routineId);
    }

    private List<Activity> getActivitiesForRoutine(final Routine routine) {
        final String routineId = String.valueOf(routine.getId());
        return Activity.find(Activity.class, "routine = ?", routineId);
    }
}
