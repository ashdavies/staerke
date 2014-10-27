package com.chaos.starke.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.starke.R;
import com.chaos.starke.core.RoutineActivity;
import com.chaos.starke.dialogs.CreateActivityDialog;
import com.chaos.starke.models.Action;
import com.chaos.starke.models.Activity;

import java.util.List;

public class ActivityAdapter extends ArrayAdapter<Activity> implements OnItemClickListener, AdapterView.OnItemLongClickListener {

    private Context context;

    public ActivityAdapter(Context context, List<Activity> activities) {
        super(context, R.layout.card_activity, activities);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_activity, parent, false);
        }

        Activity activity = getItem(position);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(activity.name);

        TextView description = (TextView) convertView.findViewById(R.id.description);
        description.setText(activity.weight + " Kg x " + activity.repetitions + " x " + activity.sets);

        return convertView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        new Action(getItem(position)).save();
        String message = context.getResources().getString(R.string.action_added);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        final Activity activity = getItem(position);
        RoutineActivity routineActivity = (RoutineActivity) context;
        CreateActivityDialog dialog = new CreateActivityDialog(activity.routine, activity);
        dialog.show(routineActivity.getSupportFragmentManager(), dialog.getClass().getName());
        return false;
    }
}
