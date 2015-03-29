package com.chaos.staerke.models;

import com.orm.SugarRecord;

public class Action extends SugarRecord<Action> {
    public String name;

    public int weight;
    public int repetitions;
    public int sets;

    public long date;

    public Action() {
    }

    public Action(Activity activity) {
        this.name = activity.name;
        this.weight = activity.weight;
        this.repetitions = activity.repetitions;
        this.sets = activity.sets;

        this.date = System.currentTimeMillis();
    }
}
