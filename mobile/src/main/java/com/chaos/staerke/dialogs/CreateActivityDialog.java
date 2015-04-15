package com.chaos.staerke.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.backup.BackupManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.chaos.staerke.R;
import com.chaos.staerke.models.Activity;
import com.chaos.staerke.models.Routine;

public class CreateActivityDialog extends DialogFragment {
    private final Routine routine;
    private Activity activity;

    private ActivityCreatedListener activityCreatedListener;

    private Spinner name;

    public CreateActivityDialog(final Routine routine, final Activity activity) {
        this.routine = routine;
        this.activity = activity;
    }

    public void setActivityCreatedListener(final ActivityCreatedListener activityCreatedListener) {
        this.activityCreatedListener = activityCreatedListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_create_activity, null);

        this.name = (Spinner) view.findViewById(R.id.name);
        final String[] activities = getResources().getStringArray(R.array.activities);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, activities);
        this.name.setAdapter(adapter);

        if (this.activity != null) {
            for (int i = 0; i < activities.length; i++)
                if (activities[i].equals(this.activity.name)) {
                    this.name.setSelection(i, true);
                    break;
                }
        }

        builder.setView(view)
                .setTitle(activity == null ? R.string.dialog_title_create_activity : R.string.dialog_title_edit_activity)
                .setPositiveButton(activity == null ? R.string.action_create : R.string.action_edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        if (activity == null) {
                            activity = new Activity();
                        }
                        activity.routine = routine;
                        activity.name = name.getSelectedItem().toString();
                        activity.save();

                        BackupManager backupManager = new BackupManager(getActivity());
                        backupManager.dataChanged();

                        if (activityCreatedListener != null) {
                            activityCreatedListener.onActivityCreatedListener(activity);
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        CreateActivityDialog.this.getDialog().cancel();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface ActivityCreatedListener {
        public void onActivityCreatedListener(Activity activity);
    }
}
