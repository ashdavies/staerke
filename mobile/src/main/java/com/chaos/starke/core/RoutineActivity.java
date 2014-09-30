package com.chaos.starke.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.chaos.starke.R;
import com.chaos.starke.adapters.ActivityAdapter;
import com.chaos.starke.models.Activity;
import com.chaos.starke.models.Routine;

public class RoutineActivity extends FragmentActivity implements OnClickListener {

    private RoutineActivity context = this;

    private ActivityAdapter adapter;
    private ListView list;
    private View header;

    private Routine routine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        Intent intent = getIntent();
        routine = Routine.findById(Routine.class, intent.getLongExtra("routine", 0));

        list = (ListView) findViewById(R.id.cards);
        adapter = new ActivityAdapter(context, Activity.find(Activity.class, "routine = ?", new String[]{routine.getId().toString()}));

        list.setAdapter(adapter);
        list.setOnItemClickListener(adapter);

        findViewById(R.id.create).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /*case R.id.create:

                CreateActivityDialog dialog = new CreateActivityDialog(routine, null);
                dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
                return;        */

        }
    }

}
