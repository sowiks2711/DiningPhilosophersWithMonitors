package com.sowiks.FeastObjects;

import com.sowiks.Main;

public class CucumberPlate {
    int plateCapacity = Main.PLATE_CAPACITY;
    int cucumbersLeft = plateCapacity;
    boolean isFree = true;
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
        //cucumbersLeft--;
    }
    public void refill() {
        cucumbersLeft = plateCapacity;
    }

}
