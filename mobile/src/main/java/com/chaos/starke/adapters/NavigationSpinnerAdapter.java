package com.chaos.starke.adapters;

import java.util.List;

import com.chaos.starke.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NavigationSpinnerAdapter extends ArrayAdapter<String> {

    // Context reference
    Context context;

    // Layout inflater
    LayoutInflater inflater;

    public NavigationSpinnerAdapter(Context context, List<String> objects) {
        super(context, R.layout.navigation_spinner_item, R.id.title, objects);
        inflater = LayoutInflater.from(context);
    }

    /**
     * Return the title view
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.navigation_spinner, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(getItem(position));
        return view;
    }

    /**
     * Return the child view
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.navigation_spinner_item, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(getItem(position));
        return view;
    }

}
