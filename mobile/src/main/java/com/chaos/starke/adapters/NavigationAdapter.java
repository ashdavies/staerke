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
    public NavigationAdapter(final Context context) {
        super(context, R.layout.navigation_item, R.id.title, new ArrayList<String>());
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View resultView;

        if (convertView == null) {
            resultView = super.getView(position, convertView, parent);
        } else {
            resultView = convertView;
        }

        final Routine.Category category = Routine.Category.valueOf(getItem(position));
        final CircleImageView thumbnail = (CircleImageView) resultView.findViewById(R.id.thumbnail);
        thumbnail.setImageResource(category.getIcon());

        return resultView;
    }

    public void addCategories(final Routine.Category[] categories) {
        for (Routine.Category category : categories) {
            add(category.name());
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
    }
}
