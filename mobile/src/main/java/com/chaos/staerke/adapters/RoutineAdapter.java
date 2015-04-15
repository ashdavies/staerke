package com.chaos.staerke.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chaos.staerke.R;
import com.chaos.staerke.models.Routine;
import com.chaos.staerke.ui.RoutineActivity;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RoutineAdapter extends ArrayAdapter<Routine> implements OnItemClickListener {
    private Context context;

    public RoutineAdapter(final Context context) {
        super(context, R.layout.card_routine, R.id.name);
        this.context = context;
    }

    @Override
    public View getView(final int position, final View view, final ViewGroup parent) {
        final View convertView = this.convertView(position, view, parent);

        final Routine routine = this.getItem(position);
        final CircleImageView thumbnail = (CircleImageView) convertView.findViewById(R.id.thumbnail);
        final Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(), routine.category.getIcon());
        thumbnail.setImageBitmap(bitmap);

        final TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(routine.name);

        final TextView category = (TextView) convertView.findViewById(R.id.category);
        if (routine.category != null) {
            category.setText(routine.category.name());
        }

        final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.favourite);
        checkbox.setChecked(routine.isFavourite());
        checkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                CheckBox checkbox = (CheckBox) view;
                routine.favourite = checkbox.isChecked();
                routine.save();
            }
        });

        return convertView;
    }

    private View convertView(final int position, final View view, final ViewGroup parent) {
        if (view == null) {
            return super.getView(position, null, parent);
        }

        return view;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        final Routine routine = this.getItem(position);

        final Intent intent = new Intent(this.context, RoutineActivity.class);
        intent.putExtra("routine", routine.getId());
        this.context.startActivity(intent);
    }

    public void addRoutinesFromCategory(final Routine.Category category) {
        final List<Routine> routines = Routine.find(Routine.class, "category = ?", category.name());
        for (final Routine routine : routines) {
            add(routine);
        }
    }

    public void addRoutinesFromFavourite() {
        final List<Routine> routines = Routine.find(Routine.class, "favourite = ?", "1");
        for (final Routine routine : routines) {
            add(routine);
        }
    }

    public void importFromResource(final Gson gson, final int resource) {
        final InputStreamReader routines = new InputStreamReader(this.context.getResources().openRawResource(resource));
        for (final Routine routine : gson.fromJson(routines, Routine[].class)) {
            routine.save();
        }
    }

}
