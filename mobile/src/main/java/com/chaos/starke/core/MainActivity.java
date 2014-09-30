package com.chaos.starke.core;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chaos.starke.R;
import com.chaos.starke.adapters.NavigationAdapter;
import com.chaos.starke.adapters.RoutineAdapter;
import com.chaos.starke.models.Routine;
import com.google.gson.Gson;
import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;

import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnClickListener, AdapterView.OnItemClickListener {

    private static int NO_ROUTINES = 0;

    private RoutineAdapter routineAdapter;
    private ListView routineListView;

    private NavigationAdapter navigationAdapter;
    private ActionBarDrawerToggle navigationToggle;
    private DrawerLayout navigationLayout;
    private ListView navigationList;

    private FloatingActionButton createRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        navigationAdapter = new NavigationAdapter(this);
        navigationList = (ListView) findViewById(R.id.navigation_drawer);
        navigationList.setAdapter(navigationAdapter);
        navigationList.setOnItemClickListener(this);

        navigationLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationToggle = new ActionBarDrawerToggle(this, navigationLayout, R.drawable.ic_navigation_drawer,
                R.string.application, R.string.application);
        navigationLayout.setDrawerListener(navigationToggle);

        createRoutine = (FloatingActionButton) findViewById(R.id.create);
        createRoutine.setOnClickListener(this);

        routineAdapter = new RoutineAdapter(this);
        routineListView = (ListView) findViewById(R.id.routines);
        routineListView.setOnItemClickListener(routineAdapter);
        routineListView.setAdapter(routineAdapter);
        routineListView.setOnTouchListener(new ShowHideOnScroll(createRoutine));

        populateNavigationFromCategories();
        populateRoutinesFromFavourites();

        if (hasExistingRoutines() == false) {
            importRoutinesFromRawResource();
            populateRoutinesFromFavourites();
        }

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

    // TODO Should be a method of navigation adapter
    private void populateNavigationFromCategories() {
        Routine.Category[] categories = Routine.Category.values();
        for (Routine.Category category : categories) navigationAdapter.add(category.name());
    }

    // TODO Should be a method of routine adapter
    public void populateRoutinesFromFavourites() {
        List<Routine> routines = Routine.find(Routine.class, "favourite = ?", "1");
        for (Routine routine : routines) routineAdapter.add(routine);
    }

    // TODO Should be a method of routine adapter
    public void populateRoutinesFromCategory(Routine.Category category) {
        List<Routine> routines = Routine.find(Routine.class, "category = ?", category.name());
        for (Routine routine : routines) routineAdapter.add(routine);
    }

    public boolean hasExistingRoutines() {
        return routineAdapter.getCount() != NO_ROUTINES;
    }

    private void importRoutinesFromRawResource() {
        Gson gson = new Gson();
        InputStreamReader routines = new InputStreamReader(getResources().openRawResource(R.raw.routine));
        for (Routine routine : gson.fromJson(routines, Routine[].class)) routine.save();
    }

    // TODO On click listener should not contain switch
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /*case R.id.create:
                CreateRoutineDialog dialog = new CreateRoutineDialog();
                dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
                return;*/

        }
    }

    // TODO Should be method of adapter
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    // TODO Should be implemented as a navigation interface
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (navigationToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {

            case R.id.log:
                startActivity(new Intent(this, ActionActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}