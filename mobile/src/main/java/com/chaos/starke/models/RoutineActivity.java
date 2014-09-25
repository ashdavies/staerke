package com.chaos.starke.db;

import com.orm.SugarRecord;

public class RoutineActivity extends SugarRecord<com.chaos.starke.db.RoutineActivity> {

    public String name;
    public String description;

    public Rating rating;

    public enum Rating {Novice, Beginner, Intermediate, Advanced, Hardcode}

    public int sets;
    public int repetitions;
    public int weight;

    public com.chaos.starke.db.Routine routine;

    public RoutineActivity() {
    }

    public RoutineActivity(String name, int sets, int repetitions, int weight) {

        this.name = name;
        this.sets = sets;
        this.repetitions = repetitions;
        this.weight = weight;

    }

}
