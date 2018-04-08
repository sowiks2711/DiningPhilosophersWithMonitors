package com.sowiks.actors;


import com.sowiks.FeastObjects.Chalice;
import com.sowiks.FeastObjects.CucumberPlate;
import com.sowiks.monitors.ConsoleMonitor;
import com.sowiks.monitors.FeastResourcesMonitor;

public class Knight implements Runnable {
    private int i;
    private String name;
    private FeastResourcesMonitor feastMonitor;
    private ConsoleMonitor logger;
    public Knight(int i, FeastResourcesMonitor feastMonitor, ConsoleMonitor logger) {
        this.i = i;
        this.feastMonitor = feastMonitor;
        name = "Knight " + i;
        this.logger = logger;
        log( "Started");
    }

    @Override
    public void run() {
        for (int j = 0; j < 100 ; j++) {
            try {
                log("Waiting to occupy resources");
                feastMonitor.occupyResources(i);
                log("Occupied resources");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Chalice c =feastMonitor.takeChalice(i);
            log("Took chalice");
            CucumberPlate p = feastMonitor.takePlate(i);
            log("Took plate");
            c.drink();
            p.takeCucumber();

            feastMonitor.ReleaseResources(i);
            log("Released resources");


        }

    }
    private void log(String action) {
        logger.log(name, action);
    }
}
