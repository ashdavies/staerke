package com.chaos.starke.actions;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chaos.starke.R;

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
