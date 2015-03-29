package com.chaos.staerke.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.chaos.staerke.R;
import com.chaos.staerke.models.Routine;

import java.util.ArrayList;

public class CreateRoutineDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_routine, null);

        ArrayList<Routine.Category> categories = new ArrayList<Routine.Category>();
        for (Routine.Category option : Routine.Category.values()) categories.add(option);
        Spinner category = (Spinner) view.findViewById(R.id.category);
        ArrayAdapter<Routine.Category> adapter = new ArrayAdapter<Routine.Category>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categories);
        category.setAdapter(adapter);

        builder.setView(view)
                .setTitle(R.string.dialog_title_create_routine)
                .setPositiveButton(R.string.action_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                        Dialog dialog = (Dialog) dialogInterface;
                        String name = ((TextView) dialog.findViewById(R.id.name)).getText().toString();
                        Routine.Category category = (Routine.Category) ((Spinner) dialog.findViewById(R.id.category)).getSelectedItem();
                        Routine routine = new Routine(name, category);
                        routine.save();

                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        CreateRoutineDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
