package com.chaos.starke.db;

import android.app.Activity;

import com.orm.SugarRecord;

import java.util.Date;

public class WorkoutAction extends SugarRecord<com.chaos.starke.db.RoutineAction> {

    public String name;

    public int weight;
    public int repetitions;
    public int sets;

    public com.chaos.starke.db.Workout workout;

    public long date;

    public WorkoutAction() {
    }

    public WorkoutAction(com.chaos.starke.db.Workout workout, Activity activity) {

        // Store values
        this.workout = workout;
        this.name = activity.name;
        this.weight = activity.weight;
        this.repetitions = activity.repetitions;
        this.sets = activity.sets;

        // Create date
        date = new Date().getTime();

    }
}
