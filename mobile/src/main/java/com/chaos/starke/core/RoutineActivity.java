package com.chaos.starke.core;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

public class RoutineActivity extends FragmentActivity implements OnClickListener {

    private ActivityAdapter activityAdapter;
    private ListView activityListView;

    private Routine routine;

    private FloatingActionButton createActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        createActivity = (FloatingActionButton) findViewById(R.id.create);
        createActivity.setOnClickListener(this);

        long routineId = getIntent().getLongExtra("routine", 0);
        routine = Routine.findById(Routine.class, routineId);

        List<Activity> activities = Activity.find(Activity.class, "routine = ?", new String[]{String.valueOf(routineId)});
        activityAdapter = new ActivityAdapter(this, activities);

        activityListView = (ListView) findViewById(R.id.activities);
        activityListView.setAdapter(activityAdapter);
        activityListView.setOnItemClickListener(activityAdapter);
        activityListView.setOnTouchListener(new ShowHideOnScroll(createActivity));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.create:

                CreateActivityDialog dialog = new CreateActivityDialog(routine, null);
                dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
                return;

        }
    }

}
