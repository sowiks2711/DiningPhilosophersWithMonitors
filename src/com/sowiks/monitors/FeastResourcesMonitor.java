package com.sowiks.monitors;

import com.sowiks.FeastObjects.Chalice;
import com.sowiks.FeastObjects.ChalicesCyclicCollection;
import com.sowiks.FeastObjects.CucumberPlate;
import com.sowiks.FeastObjects.PlatesCyclicCollection;
import com.sowiks.monitors.monitor_helpers.FeastQueue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FeastResourcesMonitor {
    private Lock lock;
    //platesCyclicCollection
    private PlatesCyclicCollection plates;
    //chalicesCyclicCollection
    private ChalicesCyclicCollection chalices;
    //FeastQueueKi
    private FeastQueue knightsQueue;
    //nrOfTakenPlates
    private int nrOfPlatesTaken;
    //isPlatesRefillerWaiting
    private boolean isServantWaiting;
    //servantNotifier
    private Condition servantGate;
    public FeastResourcesMonitor(int n) {
        lock = new ReentrantLock();
        plates = new PlatesCyclicCollection(n);
        chalices = new ChalicesCyclicCollection(n);
        knightsQueue = new FeastQueue(lock, n);
        servantGate = lock.newCondition();
        nrOfPlatesTaken = 0;
    }

    public void occupyResources(int i) throws InterruptedException {
        lock.lock();
        try {

            //if resources unavailable
            //    update arrival time
            //    add yourself to awaiting queue
            //    await on conditional
            if (!chalices.getChaliceForIthK(i).isFree() || plates.getPlateForIthK(i).isEmpty() || !plates.getPlateForIthK(i).isFree()) {
                knightsQueue.updateTimeStamp(i);
                knightsQueue.markAsWaiting(i);
                knightsQueue.getConditional(i).await();
            }

            chalices.getChaliceForIthK(i).take();
            plates.getPlateForIthK(i);
            nrOfPlatesTaken++;
            //
            if (!isServantWaiting) {
                //choose prioritized knight
                //block resources for his neighbours
                //notify prioritized knight
            }
        } finally {
            lock.unlock();
        }
    }
    public CucumberPlate takePlate(int i) {
        return plates.getPlateForIthK(i);
    }
    public Chalice takeChalice(int i) {
        return chalices.getChaliceForIthK(i);
    }

    public void ReleaseResources(int i) {

        lock.lock();
        try {
            plates.getPlateForIthK(i).putDown();
            chalices.getChaliceForIthK(i).putDown();
            nrOfPlatesTaken--;
            if (isServantWaiting) {
                if (nrOfPlatesTaken == 0) {
                    servantGate.signal();
                }
            } else {
                //add neighbours to ready queue if resources available
                //take prioritised ready knight
                //move his neighbours to awaitingForResources
                //signal knight

            }

        } finally {
            lock.unlock();
        }
    }

}
