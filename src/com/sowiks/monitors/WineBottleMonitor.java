package com.sowiks.monitors;

import com.sowiks.Main;
import com.sowiks.feast_objects.Bottle;
import com.sowiks.monitors.monitor_helpers.FeastQueue;
import com.sowiks.monitors.monitor_helpers.PriorityElement;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WineBottleMonitor {
    private Lock lock;
    private FeastQueue knightsQueue;
    private Condition servantGate;
    private boolean servantWaiting;
    private Bottle bottle;

    public WineBottleMonitor() {
        int n = Main.N;
        lock = new ReentrantLock();
        knightsQueue = new FeastQueue(lock, n);
        servantGate = lock.newCondition();
        servantWaiting = false;
        bottle = new Bottle();
    }

    public void takeBottle(int i) throws InterruptedException {
        boolean firstTime = true;
        while (servantWaiting || bottle.isEmpty() || ! bottle.isFree()) {
            if (firstTime) {
                knightsQueue.updateTimeStamp(i);
                firstTime = false;
            }
            knightsQueue.markAsReady(i);
            knightsQueue.getConditional(i).await();
        }
        knightsQueue.resetTimeStamp(i);
        bottle.take();
    }

    public void releaseBottle() {
        bottle.putDown();
        if (servantWaiting) {
            servantGate.signal();
        } else {

            if (knightsQueue.isAnyWaitingKnightReady()) {
                knightsQueue.getNextElement().getNotifier().signal();
            }
        }
    }
    public void takeToRefill() throws InterruptedException {
        while (!bottle.isFree) {
            servantWaiting = true;
            servantGate.await();
        }
        bottle.take();
    }

    public void releaseAfterRefill() {
        bottle.putDown();
        if (knightsQueue.isAnyWaitingKnightReady()) {
            knightsQueue.getNextElement().getNotifier().signal();
        }
    }
}
