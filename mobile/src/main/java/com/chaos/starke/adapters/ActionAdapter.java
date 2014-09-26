package com.chaos.starke.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class ActionAdapter extends ArrayAdapter<ActionItem> {

    private LayoutInflater inflater;

    public ActionAdapter(Context context, List<ActionItem> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return ActionItem.Row.values().length;
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