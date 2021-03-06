package com.chaos.staerke.models;

import com.chaos.staerke.R;
import com.orm.SugarRecord;

public class Routine extends SugarRecord<Routine> {
    public String name;
    public String description;

    public Category category;
    public boolean favourite;

    public Routine() {
    }

    public Routine(String name, Category category) {
        this.name = name;
        this.category = category;
        this.favourite = false;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public enum Category implements CategoryWithIcon {
        Strength {
            public int getIcon() {
                return R.drawable.ic_strength;
            }
        },
        Health {
            public int getIcon() {
                return R.drawable.ic_health;
            }
        },
        Endurance {
            public int getIcon() {
                return R.drawable.ic_endurance;
            }
        },
        Performance {
            public int getIcon() {
                return R.drawable.ic_performance;
            }
        },
        Women {
            public int getIcon() {
                return R.drawable.ic_women;
            }
        }
    }

    private interface CategoryWithIcon {
        public int getIcon();
    }

}
