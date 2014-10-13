package com.chaos.starke.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.chaos.starke.R;
import com.chaos.starke.models.Routine;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {

    public NavigationAdapter(Context context) {
        super(context, R.layout.navigation_item, R.id.title, new ArrayList<String>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = super.getView(position, convertView, parent);

        Routine.Category category = Routine.Category.valueOf(getItem(position));
        CircleImageView thumbnail = (CircleImageView) convertView.findViewById(R.id.thumbnail);
        thumbnail.setImageResource(category.getIcon());

        return convertView;

    }

    public void addCategories(Routine.Category[] categories) {
        for (Routine.Category category : categories) add(category.name());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }

}
