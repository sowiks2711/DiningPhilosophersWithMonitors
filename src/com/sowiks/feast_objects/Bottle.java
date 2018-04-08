package com.sowiks.feast_objects;

import com.sowiks.Main;

public class Bottle {
    private int capacity;
    private int leftWineUnits;
    private boolean isFree;
    public Bottle() {
        capacity = Main.WINE_BOTTLE_CAPACITY;
        leftWineUnits = Main.WINE_BOTTLE_CAPACITY;
        isFree = true;
    }
    public boolean isEmpty() {
        return capacity == 0;
    }

    public boolean isFree() {
       return isFree;
    }

    public void take() {
        isFree = false;
    }

    public void putDown() {
        isFree = true;
    }

    public void refill() {
        leftWineUnits = capacity;
    }

    public void pour() {
        leftWineUnits--;
    }
}
