package com.chaos.starke.core;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.chaos.starke.R;
import com.chaos.starke.adapters.NavigationSpinnerAdapter;
import com.chaos.starke.adapters.RoutineAdapter;
import com.chaos.starke.dialogs.CreateRoutineDialog;
import com.chaos.starke.models.Routine;
import com.google.gson.Gson;
import com.orm.query.Select;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnClickListener, OnNavigationListener {

    // Store final reference to activity
    final MainActivity context = this;

    // Card array adapter
    private RoutineAdapter adapter;
    private ListView list;

    // Navigation spinner
    NavigationSpinnerAdapter spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create activity and set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        // Create spinner list resource
        List<String> items = new LinkedList<String>(Arrays.asList(new String[]{"My Routines"}));

        for (Routine.Category category : Routine.Category.values()) items.add(category.name());

        // Set action bar spinner
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(Boolean.FALSE);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        spinner = new NavigationSpinnerAdapter(context, items);
        actionBar.setListNavigationCallbacks(spinner, this);

        // Parse routines
        if (Routine.count(Routine.class) == 0) {
            Gson gson = new Gson();
            InputStreamReader routines = new InputStreamReader(getResources().openRawResource(R.raw.routines));
            for (Routine routine : gson.fromJson(routines, Routine[].class)) routine.save();
            //InputStreamReader activities = new InputStreamReader( getResources( ).openRawResource( R.raw.activities ) );
            //for( Activity activity : gson.fromJson( activities, Activity[].class ) ) activity.save( );
        }

        // Register click event for new routine
        ImageButton create = (ImageButton) findViewById(R.id.create);
        create.setOnClickListener(this);

        List<Routine> routines = Select.from(Routine.class).where("favourite = 1").orderBy("name").list();
        adapter = new RoutineAdapter(context, routines);
        list = (ListView) findViewById(R.id.cards);
        list.setOnItemClickListener(adapter);
        list.setAdapter(adapter);

    }

    public void show(Routine.Category category) {

        // Fetch favourites
        List<Routine> routines;
        if (category == null) {
            routines = Select.from(Routine.class)
                    .where("favourite = 1").orderBy("name").list();
        }

        // Fetch routine by category
        else {
            routines = Select.from(Routine.class)
                    .where("category = ?", new String[]{category.name()}).orderBy("name").list();
        }

        // Update routines
        adapter.clear();
        adapter.addAll(routines);

    }

    /**
     * Handle on click events
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // Create new dialog to create routine
            case R.id.create:
                CreateRoutineDialog dialog = new CreateRoutineDialog();
                dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
                return;

        }
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
     * Handle title spinner selection
     */
    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        if (position > 0) {
            show(Routine.Category.valueOf(spinner.getItem(position)));
        } else {
            show(null);
        }
        return false;
    }

}