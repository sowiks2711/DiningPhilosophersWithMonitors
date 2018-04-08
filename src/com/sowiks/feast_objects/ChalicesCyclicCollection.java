package com.sowiks.feast_objects;

import com.sowiks.Remainder;

public class ChalicesCyclicCollection {
    private Chalice[] chalices;
    private int n;

    public ChalicesCyclicCollection(int n) {
        this.n = n;
        chalices = new Chalice[n];
        for (int i = 0; i < n; i++) {
            chalices[i] = new Chalice();
        }
    }

    public Chalice getChaliceForIthK(int i) {
        return chalices[Remainder.rem((i/2),n)];
    }
}
