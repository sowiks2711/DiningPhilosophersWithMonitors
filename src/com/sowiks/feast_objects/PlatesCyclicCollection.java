package com.sowiks.feast_objects;

import com.sowiks.Remainder;

public class PlatesCyclicCollection {
    private CucumberPlate[] plates;
    private int n;

    public PlatesCyclicCollection(int n) {
        this.n = n;
        plates = new CucumberPlate[n];
        for (int i = 0; i < n; i++) {
            plates[i] = new CucumberPlate();
        }
    }

    public CucumberPlate getPlateForIthK(int i) {
        return plates[Remainder.rem(((i+1)/2),n)];
    }

    public void occupyAll() {
        for (int i = 0; i < plates.length ; i++) {
            plates[i].take();
        }
    }

    public CucumberPlate[] TakeAll() {
        return plates;
    }
}
