package com.chaos.starke.models;

import com.orm.SugarRecord;

public class Routine extends SugarRecord<Routine> {

    public String name;
    public String description;

    public Category category;

    public enum Category {Strength, Health, Endurance, Performance, Women}

    public Boolean favourite;

    public Routine() {
    }

    public Routine(String name, Category category) {

        this.name = name;
        this.category = category;
        this.favourite = false;

    }

}
