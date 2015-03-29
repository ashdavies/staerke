package com.chaos.starke.dialogs;

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
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.chaos.starke.R;
import com.chaos.starke.models.Activity;
import com.chaos.starke.models.Routine;

public class CreateActivityDialog extends DialogFragment {

    private Routine routine;
    private Activity activity;

    private Spinner name;
    private NumberPicker weight;
    private NumberPicker repetitions;
    private NumberPicker sets;

    private String[] weightValues;
    private String[] repetitionValues;
    private String[] setValues;

    public CreateActivityDialog() {
    }

    public CreateActivityDialog(final Routine routine, final Activity activity) {
        this.routine = routine;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_create_activity, null);

        this.name = (Spinner) view.findViewById(R.id.name);
        final String[] activities = getResources().getStringArray(R.array.activities);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, activities);
        this.name.setAdapter(adapter);

        this.weight = (NumberPicker) view.findViewById(R.id.weight);
        this.weightValues = new String[45];
        for (int i = 0; i < this.weightValues.length; i++) this.weightValues[i] = Integer.toString(i * 5);
        this.weight.setMinValue(0);
        this.weight.setMaxValue(this.weightValues.length - 1);
        this.weight.setWrapSelectorWheel(false);
        this.weight.setDisplayedValues(this.weightValues);

        this.repetitions = (NumberPicker) view.findViewById(R.id.repetitions);
        this.repetitionValues = new String[30];
        for (int i = 0; i < this.repetitionValues.length; i++) this.repetitionValues[i] = Integer.toString(i);
        this.repetitions.setMinValue(0);
        this.repetitions.setMaxValue(this.repetitionValues.length - 1);
        this.repetitions.setWrapSelectorWheel(false);
        this.repetitions.setDisplayedValues(this.repetitionValues);

        this.sets = (NumberPicker) view.findViewById(R.id.sets);
        this.setValues = new String[15];
        for (int i = 0; i < this.setValues.length; i++) this.setValues[i] = Integer.toString(i);
        this.sets.setMinValue(0);
        this.sets.setMaxValue(this.setValues.length - 1);
        this.sets.setWrapSelectorWheel(false);
        this.sets.setDisplayedValues(this.setValues);

        if (this.activity != null) {
            for (int i = 0; i < activities.length; i++)
                if (activities[i].equals(this.activity.name)) {
                    this.name.setSelection(i, true);
                    break;
                }

            for (int i = 0; i < this.weightValues.length; i++)
                if (this.weightValues[i].equals(String.valueOf(this.activity.weight))) {
                    this.weight.setValue(i);
                    break;
                }

            for (int i = 0; i < this.repetitionValues.length; i++)
                if (this.repetitionValues[i].equals(String.valueOf(this.activity.repetitions))) {
                    this.repetitions.setValue(i);
                    break;
                }

            for (int i = 0; i < this.setValues.length; i++)
                if (this.setValues[i].equals(String.valueOf(this.activity.sets))) {
                    this.sets.setValue(i);
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
                        activity.weight = Integer.valueOf(weightValues[weight.getValue()]);
                        activity.repetitions = Integer.valueOf(repetitionValues[repetitions.getValue()]);
                        activity.sets = Integer.valueOf(setValues[sets.getValue()]);
                        activity.save();

                        BackupManager backupManager = new BackupManager(getActivity());
                        backupManager.dataChanged();

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
}
