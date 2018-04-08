package com.sowiks.feast_objects;

import com.sowiks.Main;

public class Chalice {
    private boolean isFree = true;
    private boolean isEmpty = false;

    public boolean isFree() {
        return isFree;
    }
    public void take() {
        isFree = false;
    }
    public void putDown() {
        isFree = true;
    }
    public void pour() {
        try {
            Thread.currentThread().sleep(Main.POUR_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isEmpty = false;
    }
    public void drink() {
        try {
            Thread.currentThread().sleep(Main.DRINK_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //isEmpty = true;
    }

}
