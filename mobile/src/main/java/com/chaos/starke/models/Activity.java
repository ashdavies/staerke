package com.chaos.starke.models;

import com.orm.SugarRecord;

public class Activity extends SugarRecord<Activity> {
    public String name;
    public String description;

    public Routine routine;

    public Rating rating;
    public int sets;
    public int repetitions;
    public int weight;
    public Activity() {
    }

    public Activity(String name, int sets, int repetitions, int weight) {

        this.name = name;
        this.sets = sets;
        this.repetitions = repetitions;
        this.weight = weight;

    }

    public enum Rating {Novice, Beginner, Intermediate, Advanced, Hardcode}

}
