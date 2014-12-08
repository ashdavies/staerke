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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar(getSupportActionBar());
        setupActionButton();
        setupNavigation();
        setupRoutines();
    }

    private void setupActionBar(ActionBar actionBar) {
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupActionButton() {
        actionButton = (FloatingActionButton) findViewById(R.id.create);
        actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateRoutineDialog dialog = new CreateRoutineDialog();
                dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
            }
        });
    }

    private void setupNavigation() {
        navigationAdapter = new NavigationAdapter(this);
        navigationAdapter.addCategories(Routine.Category.values());

        navigationList = (ListView) findViewById(R.id.navigation_drawer);
        navigationList.setAdapter(navigationAdapter);
        navigationList.setOnItemClickListener(navigationAdapter);

        navigationLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationToggle = new ActionBarDrawerToggle(this, navigationLayout, R.string.application, R.string.application);
        navigationToggle.setDrawerIndicatorEnabled(true);
        navigationLayout.setDrawerListener(navigationToggle);
    }

    private void setupRoutines() {
        routineAdapter = new RoutineAdapter(this);
        routineListView = (ListView) findViewById(R.id.routines);
        routineListView.setOnItemClickListener(routineAdapter);
        routineListView.setAdapter(routineAdapter);
        routineListView.setOnTouchListener(new ShowHideOnScroll(actionButton));

        if (hasNoRoutines()) {
            routineAdapter.importFromResource(new Gson(), R.raw.routines);
        }

        for (Routine.Category category : Routine.Category.values())
            routineAdapter.addRoutinesFromCategory(category);
    }

    private boolean hasNoRoutines() {
        return Routine.count(Routine.class, null, null) == NO_ROUTINES;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navigationToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        navigationToggle.onConfigurationChanged(configuration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (navigationToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {

            case R.id.activities:
                startActivity(new Intent(this, ActivitiesActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}