package com.sowiks.actors;

import com.sowiks.Main;
import com.sowiks.monitors.ConsoleMonitor;
import com.sowiks.monitors.WineBottleMonitor;

public class BottleRefillerServant extends NamedLoggable implements Runnable {
    private WineBottleMonitor bottleMonitor;
    public BottleRefillerServant(ConsoleMonitor logger, WineBottleMonitor bottleMonitor) {
        super("BottleServant", logger);
        this.bottleMonitor = bottleMonitor;
    }

    @Override
    public void run() {

        for (int j = 0; j < 100 ; j++) {
            try {
                Thread.currentThread().sleep(Main.BOTTLE_SERVANT_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                log("Waiting to refill bottle");
                bottleMonitor.occupyToRefill();
                log("Took wine bottle");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            bottleMonitor.takeWineBottle().refill();
            log("Bottle refilled");
            bottleMonitor.releaseAfterRefill();
            log("Released all plates");
        }
    }
}
