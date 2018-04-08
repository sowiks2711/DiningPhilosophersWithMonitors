package com.sowiks.actors;


import com.sowiks.Main;
import com.sowiks.feast_objects.Chalice;
import com.sowiks.feast_objects.CucumberPlate;
import com.sowiks.monitors.ConsoleMonitor;
import com.sowiks.monitors.FeastResourcesMonitor;
import com.sowiks.monitors.WineBottleMonitor;

import java.util.concurrent.ThreadLocalRandom;

public class Knight extends NamedLoggable implements Runnable {
    private int i;
    private WineBottleMonitor bottleMonitor;
    private FeastResourcesMonitor feastMonitor;
    public Knight(int i, ConsoleMonitor logger, FeastResourcesMonitor feastMonitor, WineBottleMonitor bottleMonitor) {
        super("Knight " + i, logger);
        this.i = i;
        this.feastMonitor = feastMonitor;
        this.bottleMonitor = bottleMonitor;
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
            log("Waiting for bottle");
            try {
                bottleMonitor.takeBottle(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log("Pouring wine");
            bottleMonitor.takeWineBottle().pour();
            bottleMonitor.releaseBottle();
            log("Drinking wine");
            c.drink();
            log("Biting cucumber");
            p.takeCucumber();

            feastMonitor.ReleaseResources(i);
            log("Released resources");
        }

    }
}
