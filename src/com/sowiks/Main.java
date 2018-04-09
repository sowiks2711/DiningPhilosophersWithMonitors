package com.sowiks;

import com.sowiks.actors.BottleRefillerServant;
import com.sowiks.actors.CucumberServant;
import com.sowiks.actors.Knight;
import com.sowiks.monitors.ConsoleMonitor;
import com.sowiks.monitors.FeastResourcesMonitor;
import com.sowiks.monitors.StoryTellingMonitor;
import com.sowiks.monitors.WineBottleMonitor;

import java.util.Random;

public class Main {
    public static final int N = 8;
    public static final int PLATE_CAPACITY = 4;
    public static final int STARVATION_TRESHOLD = 10000; //in nanoseconds
    public static final int CUCUMBER_SERVANT_SLEEP_TIME = 5000; //miliseconds
    public static final int MINIMAL_KNIGHT_SLEEP_TIME = 100;
    public static final int MAXIMAL_KNIGHT_SLEEP_TIME = 300;
    public static final int WINE_BOTTLE_CAPACITY = 10;
    public static final long BOTTLE_SERVANT_SLEEP_TIME = 5000;
    public static final int KING_INDEX = 0;
    public static final int MINIMAL_KNIGHT_STORY_TELLING_TIME = 700;
    public static final int MAXIMAL_KNIGHT_STORY_TELLING_TIME = 1200;
    public static long POUR_TIME = 200;
    public static long DRINK_TIME = 200;
    public static Random rnd = new Random();
    public static void main(String[] args) {
        if (N < 4 || (N%2) != 0)
            throw  new IllegalArgumentException(
                    "N=" + N + " should be even and greater than or equal to 4"
            );
        ConsoleMonitor logger = new ConsoleMonitor();
        FeastResourcesMonitor feastMonitor = new FeastResourcesMonitor();
        WineBottleMonitor bottleMonitor = new WineBottleMonitor();
        StoryTellingMonitor storyMonitor = new StoryTellingMonitor();
        for (int i = 0; i < N ; i++) {
            (new Thread(new Knight(i, logger, feastMonitor, bottleMonitor, storyMonitor))).start();
        }
        (new Thread(new CucumberServant(feastMonitor, logger))).start();
        (new Thread(new BottleRefillerServant(logger, bottleMonitor))).start();
    }
}
