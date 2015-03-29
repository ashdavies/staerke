package com.chaos.starke.core;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.chaos.starke.R;
import com.chaos.starke.adapters.NavigationAdapter;
import com.chaos.starke.adapters.RoutineAdapter;
import com.chaos.starke.dialogs.CreateRoutineDialog;
import com.chaos.starke.models.Routine;
import com.google.gson.Gson;
import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;

public class MainActivity extends ActionBarActivity {

    private static final long NO_ROUTINES = 0;

    private RoutineAdapter routineAdapter;
    private ListView routineListView;

    private NavigationAdapter navigationAdapter;
    private ActionBarDrawerToggle navigationToggle;
    private DrawerLayout navigationLayout;
    private ListView navigationList;

    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.setupActionBar(getSupportActionBar());
        this.setupActionButton();
        this.setupNavigation();
        this.setupRoutines();
    }

    private void setupActionBar(final ActionBar actionBar) {
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupActionButton() {
        this.actionButton = (FloatingActionButton) findViewById(R.id.create);
        this.actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                CreateRoutineDialog dialog = new CreateRoutineDialog();
                dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
            }
        });
    }

    private void setupNavigation() {
        this.navigationAdapter = new NavigationAdapter(this);
        this.navigationAdapter.addCategories(Routine.Category.values());

        this.navigationList = (ListView) findViewById(R.id.navigation_drawer);
        this.navigationList.setAdapter(this.navigationAdapter);
        this.navigationList.setOnItemClickListener(this.navigationAdapter);

        this.navigationLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.navigationToggle = new ActionBarDrawerToggle(this, this.navigationLayout, R.string.application, R.string.application);
        this.navigationToggle.setDrawerIndicatorEnabled(true);
        this.navigationLayout.setDrawerListener(this.navigationToggle);
    }

    private void setupRoutines() {
        this.routineAdapter = new RoutineAdapter(this);
        this.routineListView = (ListView) findViewById(R.id.routines);
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
        this.navigationToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.navigationToggle.onConfigurationChanged(configuration);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (this.navigationToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.activities:
                this.startActivity(new Intent(this, ActivitiesActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
