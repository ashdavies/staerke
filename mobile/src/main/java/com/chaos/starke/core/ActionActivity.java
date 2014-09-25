package com.chaos.starke.core;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chaos.starke.R;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActionActivity extends ListActivity {

    // Store final reference to activity
    final ActionActivity context = this;

    // Card array adapter
    private ActionAdapter adapter;
    private List<ActionItem> items;

    // Card row type
    private enum Row {
        Header, Entry
    }

    /**
     * Log entry adapter
     */
    public class ActionAdapter extends ArrayAdapter<ActionItem> {

        private LayoutInflater inflater;

        public ActionAdapter(Context context, List<ActionItem> items) {
            super(context, 0, items);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getViewTypeCount() {
            return Row.values().length;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).getViewType();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(inflater, convertView);
        }

    }

    ;

    /**
     * Log entry interface
     */
    private interface ActionItem {
        public int getViewType();

        public View getView(LayoutInflater inflater, View view);
    }

    /**
     * Log header
     */
    @SuppressLint("InflateParams")
    public class ActionHeader implements ActionItem {

        // Header text
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

            // Inflate view if necessary
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.card_action_header, null);
            }

            // Set the name
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(this.name);

            return convertView;

        }

    }

    /**
     * Log entry
     */
    @SuppressLint("InflateParams")
    public class ActionEntry implements ActionItem {

        // Action entry
        private final com.chaos.starke.db.RoutineAction routineAction;

        public ActionEntry(com.chaos.starke.db.RoutineAction routineAction) {
            this.routineAction = routineAction;
        }

        @Override
        public int getViewType() {
            return Row.Entry.ordinal();
        }

        @Override
        public View getView(LayoutInflater inflater, View convertView) {

            // Inflate view if necessary
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.card_action, null);
            }

            // Set the date
            TextView date = (TextView) convertView.findViewById(R.id.date);
            SimpleDateFormat format = new SimpleDateFormat("kk:mm", Locale.UK);
            date.setText(format.format(routineAction.date));

            // Set the name
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(routineAction.name);

            // Set the description
            TextView description = (TextView) convertView.findViewById(R.id.description);
            description.setText(routineAction.weight + " Kg x " + routineAction.repetitions + " x " + routineAction.sets);

            return convertView;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Call parent and set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        // Simple date format for day of the week
        SimpleDateFormat format = new SimpleDateFormat("cccc dd LLLL", Locale.UK);

        // Get actions from database and add them to the adapter
        items = new ArrayList<ActionItem>();
        List<com.chaos.starke.db.RoutineAction> routineActions = Select.from(com.chaos.starke.db.RoutineAction.class).orderBy("date DESC").list();
        long last = 0;

        // Iterate over items with headers
        for (com.chaos.starke.db.RoutineAction routineAction : routineActions) {
            if (last == 0 || last > routineAction.date / 86400000) {
                items.add(new ActionHeader(format.format(new Date(routineAction.date))));
            }
            last = routineAction.date / 86400000;
            items.add(new ActionEntry(routineAction));
        }

        // Create a new log entry adapter
        adapter = new ActionAdapter(context, items);
        setListAdapter(adapter);

    }

}
