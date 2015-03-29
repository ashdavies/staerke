package com.chaos.stark.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.stark.R;
import com.chaos.stark.core.RoutineActivity;
import com.chaos.stark.dialogs.CreateActivityDialog;
import com.chaos.stark.models.Action;
import com.chaos.stark.models.Activity;

import java.util.List;

public class ActivityAdapter extends ArrayAdapter<Activity> implements OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    private Context context;

    public ActivityAdapter(final Context context, final List<Activity> activities) {
        super(context, R.layout.card_activity, activities);
        this.context = context;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View resultView;

        if (convertView == null) {
            resultView = LayoutInflater.from(this.context).inflate(R.layout.card_activity, parent, false);
        } else {
            resultView = convertView;
        }

        final Activity activity = getItem(position);

        final TextView name = (TextView) resultView.findViewById(R.id.name);
        name.setText(activity.name);

        final TextView description = (TextView) resultView.findViewById(R.id.description);
        description.setText(activity.weight + " Kg x " + activity.repetitions + " x " + activity.sets);

        return resultView;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        new Action(this.getItem(position)).save();
        final String message = this.context.getResources().getString(R.string.action_added);
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
        final Activity activity = this.getItem(position);
        final RoutineActivity routineActivity = (RoutineActivity) this.context;
        final CreateActivityDialog dialog = new CreateActivityDialog(activity.routine, activity);

        dialog.show(routineActivity.getSupportFragmentManager(), dialog.getClass().getName());

        return false;
    }
}
