package com.chaos.staerke.adapters.items;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chaos.staerke.R;

public class ActionHeader implements ActionInterface {
    private final String name;

    public ActionHeader(final String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return Row.Header.ordinal();
    }

    @Override
    public View getView(final LayoutInflater inflater, final View convertView) {
        final View resultView;

        if (convertView == null) {
            resultView = inflater.inflate(R.layout.card_action_header, null);
        } else {
            resultView = convertView;
        }

        TextView name = (TextView) resultView.findViewById(R.id.name);
        name.setText(this.name);

        return resultView;

    }

}
