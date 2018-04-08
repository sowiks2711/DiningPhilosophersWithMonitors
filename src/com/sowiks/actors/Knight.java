package com.sowiks.actors;


import com.sowiks.Main;
import com.sowiks.feast_objects.Chalice;
import com.sowiks.feast_objects.CucumberPlate;
import com.sowiks.monitors.ConsoleMonitor;
import com.sowiks.monitors.FeastResourcesMonitor;

import java.util.concurrent.ThreadLocalRandom;

public class Knight extends NamedLoggable implements Runnable {
    private int i;
    private FeastResourcesMonitor feastMonitor;
    public Knight(int i, FeastResourcesMonitor feastMonitor, ConsoleMonitor logger) {
        super("Knight " + i, logger);
        this.i = i;
        this.feastMonitor = feastMonitor;
        log( "Started");
    }

    @Override
    public void run() {
        for (int j = 0; j < 100 ; j++) {
            log("Going to sleep");
            try {
                Thread.currentThread().sleep(
                        ThreadLocalRandom.current().
                                nextInt(Main.MINIMAL_KNIGHT_SLEEP_TIME, Main.MAXIMAL_KNIGHT_SLEEP_TIME)
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
}
