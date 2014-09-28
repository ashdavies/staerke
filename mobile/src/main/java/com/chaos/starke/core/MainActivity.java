package com.chaos.starke.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.chaos.starke.R;
import com.chaos.starke.adapters.RoutineAdapter;
import com.chaos.starke.dialogs.CreateRoutineDialog;
import com.chaos.starke.models.Routine;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnClickListener, AdapterView.OnItemClickListener {

    private RoutineAdapter routineAdapter;
    private ListView routineList;

    private DrawerLayout drawerLayout;
    private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.navigation_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getCategories()));
        drawerList.setOnItemClickListener(this);

        if (Routine.count(Routine.class, null, null) == 0) {
            importRoutines();
        }

        ImageButton create = (ImageButton) findViewById(R.id.create);
        create.setOnClickListener(this);

        routineAdapter = new RoutineAdapter(this, Routine.listAll(Routine.class));
        routineList = (ListView) findViewById(R.id.routines);
        routineList.setOnItemClickListener(routineAdapter);
        routineList.setAdapter(routineAdapter);
    }

    private List<String> getCategories() {
        List<String> items = new LinkedList<String>(Arrays.asList(new String[]{"My Routines"}));
        for (Routine.Category category : Routine.Category.values()) items.add(category.name());
        return items;
    }

    private void importRoutines() {
        Gson gson = new Gson();
        InputStreamReader routines = new InputStreamReader(getResources().openRawResource(R.raw.routine));
        for (Routine routine : gson.fromJson(routines, Routine[].class)) routine.save();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.create:
                CreateRoutineDialog dialog = new CreateRoutineDialog();
                dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
                return;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.log:
                startActivity(new Intent(this, ActionActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}