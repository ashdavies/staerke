package com.chaos.starke.core;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chaos.starke.R;
import com.chaos.starke.adapters.ActionAdapter;
import com.chaos.starke.adapters.ActionItem;
import com.chaos.starke.models.Action;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActionActivity extends ListActivity {

    final ActionActivity context = this;

    private ActionAdapter adapter;
    private List<ActionItem> items;

    public class ActionHeader implements ActionItem {

        private String name;

        public ActionHeader(String name) {
            this.name = name;
        }

        @Override
        public int getViewType() {
            return Row.Header.ordinal();
        }

        @Override
        public View getView(LayoutInflater inflater, View convertView) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.card_action_header, null);
            }

            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(this.name);

            return convertView;

        }

    }

    public class ActionEntry implements ActionItem {

        private final Action routineAction;

        public ActionEntry(Action routineAction) {
            this.routineAction = routineAction;
        }

        @Override
        public int getViewType() {
            return Row.Entry.ordinal();
        }

        @Override
        public View getView(LayoutInflater inflater, View convertView) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.card_action, null);
            }

            TextView date = (TextView) convertView.findViewById(R.id.date);
            SimpleDateFormat format = new SimpleDateFormat("kk:mm", Locale.UK);
            date.setText(format.format(routineAction.date));

            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(routineAction.name);

            TextView description = (TextView) convertView.findViewById(R.id.description);
            description.setText(routineAction.weight + " Kg x " + routineAction.repetitions + " x " + routineAction.sets);

            return convertView;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        SimpleDateFormat format = new SimpleDateFormat("cccc dd LLLL", Locale.UK);

        items = new ArrayList<ActionItem>();
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
