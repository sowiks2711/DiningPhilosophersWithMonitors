package com.sowiks.FeastObjects;

public class PlatesCyclicCollection {
    private CucumberPlate[] plates;
    private int n;

    public PlatesCyclicCollection(int n){
        this.n = n;
        plates = new CucumberPlate[n];

    }

    public CucumberPlate getPlateForKi(int i) {
        return plates[((i+1)/2)%n];
    }
}
