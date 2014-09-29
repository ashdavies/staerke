package com.chaos.starke.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class NavigationAdapter extends ArrayAdapter<String> {

    public NavigationAdapter(Context context, List<String> categories) {
        super(context, android.R.layout.simple_list_item_1,categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
