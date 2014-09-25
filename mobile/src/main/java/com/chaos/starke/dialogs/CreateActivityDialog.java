package com.chaos.starke.dialogs;

import com.chaos.starke.R;
import com.chaos.starke.db.Routine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.backup.BackupManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

public class CreateActivityDialog extends DialogFragment {

    // Routine and optional action
    private Routine routine;
    private com.chaos.starke.db.RoutineActivity routineActivity;

    /**
     * Input field storage
     */
    private Spinner name;
    private NumberPicker weight;
    private NumberPicker repetitions;
    private NumberPicker sets;

    /**
     * Input value storage
     */
    private String[] weightValues;
    private String[] repetitionValues;
    private String[] setValues;

    public CreateActivityDialog(Routine routine, com.chaos.starke.db.RoutineActivity routineActivity) {
        super();
        this.routine = routine;
        this.routineActivity = routineActivity;
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_activity, null);

        // Create adapter for auto complete text view
        name = (Spinner) view.findViewById(R.id.name);
        String[] activities = getResources().getStringArray(R.array.activities);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, activities);
        name.setAdapter(adapter);

        // Populate weight stepper
        weight = (NumberPicker) view.findViewById(R.id.weight);
        weightValues = new String[45];
        for (int i = 0; i < weightValues.length; i++) weightValues[i] = Integer.toString(i * 5);
        weight.setMinValue(0);
        weight.setMaxValue(weightValues.length - 1);
        weight.setWrapSelectorWheel(false);
        weight.setDisplayedValues(weightValues);

        // Populate weight stepper
        repetitions = (NumberPicker) view.findViewById(R.id.repetitions);
        repetitionValues = new String[30];
        for (int i = 0; i < repetitionValues.length; i++) repetitionValues[i] = Integer.toString(i);
        repetitions.setMinValue(0);
        repetitions.setMaxValue(repetitionValues.length - 1);
        repetitions.setWrapSelectorWheel(false);
        repetitions.setDisplayedValues(repetitionValues);

        // Populate weight stepper
        sets = (NumberPicker) view.findViewById(R.id.sets);
        setValues = new String[15];
        for (int i = 0; i < setValues.length; i++) setValues[i] = Integer.toString(i);
        sets.setMinValue(0);
        sets.setMaxValue(setValues.length - 1);
        sets.setWrapSelectorWheel(false);
        sets.setDisplayedValues(setValues);

        // Select existing values
        if (routineActivity != null) {

            // Set activity name field
            for (int i = 0; i < activities.length; i++)
                if (activities[i].equals(routineActivity.name)) {
                    name.setSelection(i, true);
                    break;
                }

            // Set weight values
            for (int i = 0; i < weightValues.length; i++)
                if (weightValues[i].equals(String.valueOf(routineActivity.weight))) {
                    weight.setValue(i);
                    break;
                }

            // Set repetition value
            for (int i = 0; i < repetitionValues.length; i++)
                if (repetitionValues[i].equals(String.valueOf(routineActivity.repetitions))) {
                    repetitions.setValue(i);
                    break;
                }

            // Set value value
            for (int i = 0; i < setValues.length; i++)
                if (setValues[i].equals(String.valueOf(routineActivity.sets))) {
                    sets.setValue(i);
                    break;
                }

        }

        // Inflate the view with null parent view
        builder.setView(view)
                .setTitle(routineActivity == null ? R.string.dialog_create_activity_title : R.string.dialog_edit_activity_title)
                .setPositiveButton(routineActivity == null ? R.string.action_create : R.string.action_edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                        // Create new activity
                        if (routineActivity == null) {
                            routineActivity = new com.chaos.starke.db.RoutineActivity();
                        }
                        routineActivity.routine = routine;
                        routineActivity.name = name.getSelectedItem().toString();
                        routineActivity.weight = Integer.valueOf(weightValues[weight.getValue()]);
                        routineActivity.repetitions = Integer.valueOf(repetitionValues[repetitions.getValue()]);
                        routineActivity.sets = Integer.valueOf(setValues[sets.getValue()]);
                        routineActivity.save();

                        // Notify backup agent
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
