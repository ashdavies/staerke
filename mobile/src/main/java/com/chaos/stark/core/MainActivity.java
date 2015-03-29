package com.chaos.stark.core;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.ListView;

import com.chaos.stark.R;
import com.chaos.stark.adapters.NavigationAdapter;
import com.chaos.stark.adapters.RoutineAdapter;
import com.chaos.stark.dialogs.CreateRoutineDialog;
import com.chaos.stark.models.Routine;
import com.google.gson.Gson;
import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;

import butterknife.InjectView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final long NO_ROUTINES = 0;

    @InjectView(R.id.action_button)
    protected FloatingActionButton actionButton;

    @InjectView(R.id.navigation_list)
    protected ListView navigationList;

    @InjectView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;

    @InjectView(R.id.routines)
    protected ListView routineListView;

    private RoutineAdapter routineAdapter;
    private NavigationAdapter navigationAdapter;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.actionButton.setOnClickListener(this);

        this.setupNavigation();
        this.setupRoutines();
    }

    @Override
    public int onCreateViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(final View view) {
        final CreateRoutineDialog dialog = new CreateRoutineDialog();
        dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
    }

    private void setupNavigation() {
        this.navigationAdapter = new NavigationAdapter(this);
        this.navigationAdapter.addCategories(Routine.Category.values());

        this.navigationList.setAdapter(this.navigationAdapter);
        this.navigationList.setOnItemClickListener(this.navigationAdapter);

        this.actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.string.application, R.string.application);
        this.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        this.drawerLayout.setDrawerListener(this.actionBarDrawerToggle);
    }

    private void setupRoutines() {
        this.routineAdapter = new RoutineAdapter(this);
        this.routineListView.setOnItemClickListener(this.routineAdapter);
        this.routineListView.setAdapter(this.routineAdapter);
        this.routineListView.setOnTouchListener(new ShowHideOnScroll(this.actionButton));

        if (this.hasNoRoutines()) {
            this.routineAdapter.importFromResource(new Gson(), R.raw.routines);
        }

        for (final Routine.Category category : Routine.Category.values()) {
            this.routineAdapter.addRoutinesFromCategory(category);
        }
    }

    private boolean hasNoRoutines() {
        return Routine.count(Routine.class, null, null) == NO_ROUTINES;
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.actionBarDrawerToggle.onConfigurationChanged(configuration);
    }
}
