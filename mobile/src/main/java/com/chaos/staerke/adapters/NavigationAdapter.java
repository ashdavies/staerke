package com.chaos.staerke.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.chaos.staerke.R;
import com.chaos.staerke.models.Routine;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {
    public NavigationAdapter(final Context context) {
        super(context, R.layout.navigation_item, R.id.title, new ArrayList<String>());
    }

    @Override
    public View getView(final int position, final View view, final ViewGroup parent) {
        final View convertView = this.convertView(position, view, parent);

        final Routine.Category category = Routine.Category.valueOf(getItem(position));
        final CircleImageView thumbnail = (CircleImageView) convertView.findViewById(R.id.thumbnail);
        thumbnail.setImageResource(category.getIcon());

        return convertView;
    }

    private View convertView(final int position, final View view, final ViewGroup parent) {
        if (view == null) {
            return super.getView(position, null, parent);
        }

        return view;
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
