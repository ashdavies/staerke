package com.chaos.starke.adapters.items;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chaos.starke.R;
import com.chaos.starke.models.Action;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ActionEntry implements ActionInterface {

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
