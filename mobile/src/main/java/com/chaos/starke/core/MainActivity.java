package com.chaos.starke.core;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.chaos.starke.R;
import com.chaos.starke.adapters.RoutineAdapter;
import com.chaos.starke.dialogs.CreateRoutineDialog;
import com.chaos.starke.models.Routine;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnClickListener {

    final MainActivity context = this;

    private RoutineAdapter adapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        List<String> items = new LinkedList<String>(Arrays.asList(new String[]{"My Routines"}));
        for (Routine.Category category : Routine.Category.values()) items.add(category.name());

        ImageButton create = (ImageButton) findViewById(R.id.create);
        create.setOnClickListener(this);

        //List<Routine> routines = Select.from(Routine.class).where("favourite = 1").orderBy("name").list();
        List<Routine> routines = Routine.listAll(Routine.class);
        adapter = new RoutineAdapter(context, routines);
        list = (ListView) findViewById(R.id.cards);
        list.setOnItemClickListener(adapter);
        list.setAdapter(adapter);

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

}