package com.chaos.starke.adapters;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.starke.R;
import com.chaos.starke.RoutineDetailActivity;
import com.chaos.starke.dialogs.CreateActivityDialog;
import com.chaos.starke.services.WorkoutService;

public class ActivityAdapter extends ArrayAdapter<com.chaos.starke.db.RoutineActivity> implements OnItemClickListener {

    // Context storage
    private Context context;

    // Call parent and store context
    public ActivityAdapter(Context context, List<com.chaos.starke.db.RoutineActivity> activities) {
        super(context, R.layout.card_activity, activities);
        this.context = context;
    }

    /**
     * Get view
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Inflate view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_activity, parent, false);
        }

        // Get activity
        com.chaos.starke.db.RoutineActivity routineActivity = getItem(position);

        // Set routine name
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(routineActivity.name);

        // Set routine description
        TextView description = (TextView) convertView.findViewById(R.id.description);
        description.setText(routineActivity.weight + " Kg x " + routineActivity.repetitions + " x " + routineActivity.sets);

        // Return view
        return convertView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Get activity
        final com.chaos.starke.db.RoutineActivity routineActivity = getItem(position);

        // If the workout is active
        if (WorkoutService.Status() == true) {

            // Instantiate an AlertDialog and assign titles
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder
                    .setTitle(R.string.dialog_log_activity)
                    .setMessage(R.string.dialog_log_activity_message)
                    .setPositiveButton(R.string.action_add, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Create action
                            com.chaos.starke.db.RoutineAction routineAction = new com.chaos.starke.db.RoutineAction(null, routineActivity);
                            routineAction.save();

                            // Create toast notification
                            Toast.makeText(context, context.getResources().getString(R.string.action_added), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setNegativeButton(R.string.action_cancel, null);

            // Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {

            // Alter existing routine
            RoutineDetailActivity parentActivity = (RoutineDetailActivity) context;
            CreateActivityDialog dialog = new CreateActivityDialog(routineActivity.routine, routineActivity);
            dialog.show(parentActivity.getSupportFragmentManager(), dialog.getClass().getName());

        }

    }

}
