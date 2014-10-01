package com.chaos.starke.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.chaos.starke.adapters.items.ActionInterface;

import java.util.List;

public class ActionAdapter extends ArrayAdapter<ActionInterface> {

    private LayoutInflater inflater;

    public ActionAdapter(Context context, List<ActionInterface> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return ActionInterface.Row.values().length;
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