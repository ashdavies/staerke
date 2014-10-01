package com.chaos.starke.core;

import android.app.ListActivity;
import android.os.Bundle;

import com.chaos.starke.R;
import com.chaos.starke.adapters.items.ActionEntry;
import com.chaos.starke.adapters.items.ActionHeader;
import com.chaos.starke.adapters.items.ActionInterface;
import com.chaos.starke.adapters.ActionAdapter;
import com.chaos.starke.models.Action;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends ListActivity {

    final HistoryActivity context = this;

    private ActionAdapter adapter;
    private List<ActionInterface> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SimpleDateFormat format = new SimpleDateFormat("cccc dd LLLL", Locale.GERMANY);

        items = new ArrayList<ActionInterface>();
        List<Action> actions = Select.from(Action.class).orderBy("date DESC").list();
        long last = 0;

        for (Action action : actions) {
            if (last == 0 || last > action.date / 86400000) {
                items.add(new ActionHeader(format.format(new Date(action.date))));
            }
            last = action.date / 86400000;
            items.add(new ActionEntry(action));
        }

        adapter = new ActionAdapter(context, items);
        setListAdapter(adapter);

    }

}
