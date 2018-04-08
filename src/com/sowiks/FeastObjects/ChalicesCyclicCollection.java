package com.sowiks.FeastObjects;

import com.sowiks.Remainder;

public class ChalicesCyclicCollection {
    private Chalice[] chalices;
    private int n;

    public ChalicesCyclicCollection(int n){
        this.n = n;
        chalices = new Chalice[n];

    }

    public Chalice getChaliceForIthK(int i) {
        return chalices[Remainder.rem((i/2),n)];
    }
}
