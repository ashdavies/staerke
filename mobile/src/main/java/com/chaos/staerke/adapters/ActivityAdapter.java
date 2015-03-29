package com.chaos.staerke.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chaos.staerke.R;
import com.chaos.staerke.core.RoutineActivity;
import com.chaos.staerke.core.TrackingActivity;
import com.chaos.staerke.dialogs.CreateActivityDialog;
import com.chaos.staerke.models.Activity;

import java.util.List;

public class ActivityAdapter extends ArrayAdapter<Activity> implements OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    private Context context;

    public ActivityAdapter(final Context context, final List<Activity> activities) {
        super(context, R.layout.card_activity, R.id.name, activities);
        this.context = context;
    }

    @Override
    public View getView(final int position, final View view, final ViewGroup parent) {
        final View convertView = this.convertView(position, view, parent);

        final Activity activity = getItem(position);
        final TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(activity.name);

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
        final Activity activity = this.getItem(position);

        final Intent intent = new Intent(this.context, TrackingActivity.class);
        intent.putExtra("activity", activity.getId());

        this.context.startActivity(intent);
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
