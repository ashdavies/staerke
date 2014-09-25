package com.chaos.starke.dialogs;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.chaos.starke.R;
import com.chaos.starke.db.Routine;

import java.util.ArrayList;

public class CreateRoutineDialog extends DialogFragment {

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_routine, null);

        // Populate spinner with routine categories
        ArrayList<Routine.Category> categories = new ArrayList<Routine.Category>();
        for (Routine.Category option : Routine.Category.values()) categories.add(option);
        Spinner category = (Spinner) view.findViewById(R.id.category);
        ArrayAdapter<Routine.Category> adapter = new ArrayAdapter<Routine.Category>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categories);
        category.setAdapter(adapter);

        // Inflate the view with null parent view
        builder.setView(view)
                .setTitle(R.string.dialog_create_routine_title)
                .setPositiveButton(R.string.action_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                        // Create new routine
                        Dialog dialog = (Dialog) dialogInterface;
                        String name = ((TextView) dialog.findViewById(R.id.name)).getText().toString();
                        Routine.Category category = (Routine.Category) ((Spinner) dialog.findViewById(R.id.category)).getSelectedItem();
                        Routine routine = new Routine(name, category);
                        routine.save();

                        // Notify backup agent
                        BackupManager backupManager = new BackupManager(getActivity());
                        backupManager.dataChanged();

                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        CreateRoutineDialog.this.getDialog().cancel();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();

    }

}
