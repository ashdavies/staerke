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

    public ActionAdapter(final Context context, final List<ActionInterface> items) {
        super(context, 0, items);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return ActionInterface.Row.values().length;
    }

    @Override
    public int getItemViewType(final int position) {
        return this.getItem(position).getViewType();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        return this.getItem(position).getView(this.inflater, convertView);
    }
}
