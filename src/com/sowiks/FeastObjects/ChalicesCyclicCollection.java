package com.sowiks.FeastObjects;

public class ChalicesCyclicCollection {
    private Chalice[] chalices;
    private int n;

    public ChalicesCyclicCollection(int n){
        this.n = n;
        chalices = new Chalice[n];

    }

    public Chalice getChaliceForKi(int i) {
        return chalices[(i/2)%n];
    }
}
