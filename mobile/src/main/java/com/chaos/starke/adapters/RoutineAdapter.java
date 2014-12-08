package com.chaos.starke.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chaos.starke.R;
import com.chaos.starke.core.RoutineActivity;
import com.chaos.starke.models.Routine;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RoutineAdapter extends ArrayAdapter<Routine> implements OnItemClickListener {

    private Context context;

    public RoutineAdapter(Context context) {
        super(context, R.layout.card_routine, new ArrayList<Routine>());
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_routine, parent, false);
        }

        final Routine routine = getItem(position);

        CircleImageView thumbnail = (CircleImageView) convertView.findViewById(R.id.thumbnail);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), routine.category.getIcon());
        thumbnail.setImageBitmap(bitmap);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(routine.name);

        TextView category = (TextView) convertView.findViewById(R.id.category);
        if (routine.category != null) {
            category.setText(routine.category.name());
        }

        CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.favourite);
        checkbox.setChecked(routine.favourite == null ? false : routine.favourite);
        checkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkbox = (CheckBox) view;
                routine.favourite = checkbox.isChecked();
                routine.save();
            }
        });

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Routine routine = getItem(position);

        Intent intent = new Intent(context, RoutineActivity.class);
        intent.putExtra("routine", routine.getId());
        context.startActivity(intent);
    }

    public void addRoutinesFromCategory(Routine.Category category) {
        List<Routine> routines = Routine.find(Routine.class, "category = ?", category.name());
        for (Routine routine : routines) add(routine);
    }

    public void addRoutinesFromFavourite() {
        List<Routine> routines = Routine.find(Routine.class, "favourite = ?", "1");
        for (Routine routine : routines) add(routine);
    }

    public void importFromResource(Gson gson, int resource) {
        InputStreamReader routines = new InputStreamReader(context.getResources().openRawResource(resource));
        for (Routine routine : gson.fromJson(routines, Routine[].class)) routine.save();
    }

}
