package com.sowiks.monitors;

import com.sowiks.Main;
import com.sowiks.feast_objects.Bottle;
import com.sowiks.monitors.monitor_helpers.KnightsPriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WineBottleMonitor {
    private Lock lock;
    private KnightsPriorityQueue knightsQueue;
    private Condition servantGate;
    private boolean servantWaiting;
    private Bottle bottle;

    public WineBottleMonitor() {
        int n = Main.N;
        lock = new ReentrantLock();
        knightsQueue = new KnightsPriorityQueue(lock, n);
        servantGate = lock.newCondition();
        servantWaiting = false;
        bottle = new Bottle();
    }

    public void takeBottle(int i) throws InterruptedException {
        lock.lock();
        try {
            boolean firstTime = true;
            while (servantWaiting || bottle.isEmpty() || !bottle.isFree()) {
                if (firstTime) {
                    knightsQueue.updateTimeStamp(i);
                    firstTime = false;
                }
                knightsQueue.markAsReady(i);
                knightsQueue.getConditional(i).await();
            }
            knightsQueue.resetTimeStamp(i);
            bottle.take();
        } finally {
            lock.unlock();
        }
    }

    public void releaseBottle() {
        lock.lock();
        try {
            bottle.putDown();
            if (servantWaiting) {
                servantGate.signal();
            } else {

                if (knightsQueue.isAnyWaitingKnightReady()) {
                    knightsQueue.getNextElement().getNotifier().signal();
                }
            }
        } finally {
            lock.unlock();
        }
    }
    public void occupyToRefill() throws InterruptedException {
        lock.lock();
        try {
            while (!bottle.isFree()) {
                servantWaiting = true;
                servantGate.await();
            }
            bottle.take();
        } finally {
            lock.unlock();
        }
    }
    public Bottle takeWineBottle() {
        return bottle;
    }

    public void releaseAfterRefill() {
        lock.lock();
        try {
            bottle.putDown();
            if (knightsQueue.isAnyWaitingKnightReady()) {
                knightsQueue.getNextElement().getNotifier().signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
