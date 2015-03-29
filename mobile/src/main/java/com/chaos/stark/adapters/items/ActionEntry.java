package com.chaos.stark.adapters.items;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chaos.stark.R;
import com.chaos.stark.models.Action;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ActionEntry implements ActionInterface {

    private final Action routineAction;

    public ActionEntry(final Action routineAction) {
        this.routineAction = routineAction;
    }

    @Override
    public int getViewType() {
        return Row.Entry.ordinal();
    }

    @Override
    public View getView(final LayoutInflater inflater, final View convertView) {
        final View resultView;

        if (convertView == null) {
            resultView = inflater.inflate(R.layout.card_action, null);
        } else {
            resultView = convertView;
        }

        TextView date = (TextView) resultView.findViewById(R.id.date);
        SimpleDateFormat format = new SimpleDateFormat("kk:mm", Locale.UK);
        date.setText(format.format(this.routineAction.date));

        TextView name = (TextView) resultView.findViewById(R.id.name);
        name.setText(this.routineAction.name);

        TextView description = (TextView) resultView.findViewById(R.id.description);
        description.setText(this.routineAction.weight + " Kg x " + this.routineAction.repetitions + " x " + this.routineAction.sets);

        return resultView;

    }
}
