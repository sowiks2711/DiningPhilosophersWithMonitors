package com.sowiks.feast_objects;

import com.sowiks.Main;

public class CucumberPlate {
    private int plateCapacity;
    private int cucumbersLeft;
    private boolean isFree;
    public CucumberPlate() {
        plateCapacity = Main.PLATE_CAPACITY;
        cucumbersLeft = Main.PLATE_CAPACITY;
        isFree = true;
    }
    public boolean isFree() {
        return isFree;
    }
    public boolean isEmpty() {
        return cucumbersLeft == 0;
    }
    public void take() {
        isFree = false;
    }
    public void putDown() {
        isFree = true;
    }
    public void takeCucumber() {
        cucumbersLeft--;
    }
    public void refill() {
        cucumbersLeft = plateCapacity;
    }

}
