package com.sowiks.actors;


public class Knight implements Runnable {
    private String name;
    public Knight(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + ":Hello!");

    }
}
