package com.chaos.starke.dialogs;

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

    public CreateActivityDialog() {}
    public CreateActivityDialog(Routine routine, Activity activity) {
        super();
        this.routine = routine;
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_activity, null);

        name = (Spinner) view.findViewById(R.id.name);
        String[] activities = getResources().getStringArray(R.array.activities);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, activities);
        name.setAdapter(adapter);

        weight = (NumberPicker) view.findViewById(R.id.weight);
        weightValues = new String[45];
        for (int i = 0; i < weightValues.length; i++) weightValues[i] = Integer.toString(i * 5);
        weight.setMinValue(0);
        weight.setMaxValue(weightValues.length - 1);
        weight.setWrapSelectorWheel(false);
        weight.setDisplayedValues(weightValues);

        repetitions = (NumberPicker) view.findViewById(R.id.repetitions);
        repetitionValues = new String[30];
        for (int i = 0; i < repetitionValues.length; i++) repetitionValues[i] = Integer.toString(i);
        repetitions.setMinValue(0);
        repetitions.setMaxValue(repetitionValues.length - 1);
        repetitions.setWrapSelectorWheel(false);
        repetitions.setDisplayedValues(repetitionValues);

        sets = (NumberPicker) view.findViewById(R.id.sets);
        setValues = new String[15];
        for (int i = 0; i < setValues.length; i++) setValues[i] = Integer.toString(i);
        sets.setMinValue(0);
        sets.setMaxValue(setValues.length - 1);
        sets.setWrapSelectorWheel(false);
        sets.setDisplayedValues(setValues);

        if (activity != null) {

            for (int i = 0; i < activities.length; i++)
                if (activities[i].equals(activity.name)) {
                    name.setSelection(i, true);
                    break;
                }

            for (int i = 0; i < weightValues.length; i++)
                if (weightValues[i].equals(String.valueOf(activity.weight))) {
                    weight.setValue(i);
                    break;
                }

            for (int i = 0; i < repetitionValues.length; i++)
                if (repetitionValues[i].equals(String.valueOf(activity.repetitions))) {
                    repetitions.setValue(i);
                    break;
                }

            for (int i = 0; i < setValues.length; i++)
                if (setValues[i].equals(String.valueOf(activity.sets))) {
                    sets.setValue(i);
                    break;
                }

        }

        builder.setView(view)
                .setTitle(activity == null ? R.string.dialog_create_activity_title : R.string.dialog_title_edit_activity)
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
