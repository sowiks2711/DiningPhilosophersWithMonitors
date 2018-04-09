package com.sowiks.actors;


import com.sowiks.Main;
import com.sowiks.feast_objects.Chalice;
import com.sowiks.feast_objects.CucumberPlate;
import com.sowiks.monitors.ConsoleMonitor;
import com.sowiks.monitors.FeastResourcesMonitor;
import com.sowiks.monitors.StoryTellingMonitor;
import com.sowiks.monitors.WineBottleMonitor;

import java.util.concurrent.ThreadLocalRandom;

public class Knight extends NamedLoggable implements Runnable {
    private int i;
    private WineBottleMonitor bottleMonitor;
    private FeastResourcesMonitor feastMonitor;
    private StoryTellingMonitor storyMonitor;
    public Knight(int i, ConsoleMonitor logger, FeastResourcesMonitor feastMonitor, WineBottleMonitor bottleMonitor, StoryTellingMonitor storyMonitor) {
        super(i != 0 ? "Knight " + i : "King", logger);
        this.i = i;
        this.feastMonitor = feastMonitor;
        this.bottleMonitor = bottleMonitor;
        this.storyMonitor = storyMonitor;
        log( "Started");
    }

    @Override
    public void run() {
        for (int j = 0; j < 10 ; j++) {
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
            log("Drinking " + (j+1) + " wine");
            c.drink();
            log("Biting cucumber");
            p.takeCucumber();

            feastMonitor.ReleaseResources(i);
            log("Released resources");
            log("Wait for talking turn");
            try {
                storyMonitor.occupyAttention(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("Tell a story");
            try {
                Thread.currentThread().sleep(
                        ThreadLocalRandom.current().
                                nextInt(Main.MINIMAL_KNIGHT_STORY_TELLING_TIME, Main.MAXIMAL_KNIGHT_STORY_TELLING_TIME)
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            storyMonitor.releaseAttention(i);


        }
        log("Falls down under table");

    }
}
