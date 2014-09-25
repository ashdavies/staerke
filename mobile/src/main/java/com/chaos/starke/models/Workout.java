package com.chaos.starke.db;

import java.util.Date;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class Workout extends SugarRecord<Workout> {

    @Ignore
    public Long routine;

    public Long elapsed;
    public Date started;

    @Ignore
    public boolean active = false;

    public Workout() {
    }

    public Workout(Long routine) {
        this.routine = routine;
        this.started = new Date();
    }

}
