package com.sowiks.actors;

import com.sowiks.Main;
import com.sowiks.feast_objects.CucumberPlate;
import com.sowiks.monitors.ConsoleMonitor;
import com.sowiks.monitors.FeastResourcesMonitor;


public class CucumberServant extends NamedLoggable implements Runnable {
    private FeastResourcesMonitor feastMonitor;
    public CucumberServant(FeastResourcesMonitor feastMonitor, ConsoleMonitor logger) {
        super("Cucumber servant", logger);
        this.feastMonitor = feastMonitor;
        log( "Started");
    }

    @Override
    public void run() {
        for (int j = 0; j < 100 ; j++) {
            try {
                Thread.currentThread().sleep(Main.CUCUMBER_SERVANT_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                log("Waiting to refill plates");
                feastMonitor.OccupyResourcesToFillPlates();
                log("Occupied all plates");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (CucumberPlate cp : feastMonitor.takeAllPlates()) {
                cp.refill();
            }
            log("refilled plates");

            feastMonitor.ReleaseResourcesAfterRefill();
            log("Released all plates");
        }
    }
}
