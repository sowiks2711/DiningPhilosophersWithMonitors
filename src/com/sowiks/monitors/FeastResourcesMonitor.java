package com.sowiks.monitors;

import com.sowiks.feast_objects.Chalice;
import com.sowiks.feast_objects.ChalicesCyclicCollection;
import com.sowiks.feast_objects.CucumberPlate;
import com.sowiks.feast_objects.PlatesCyclicCollection;
import com.sowiks.Main;
import com.sowiks.monitors.monitor_helpers.KnightsPriorityQueue;
import com.sowiks.monitors.monitor_helpers.PriorityElement;

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
    private KnightsPriorityQueue knightsQueue;
    //nrOfTakenPlates
    private int nrOfPlatesTaken;
    //isPlatesRefillerWaiting
    private boolean isServantWaiting;
    //servantNotifier
    private Condition servantGate;
    public FeastResourcesMonitor() {
        int n = Main.N;
        lock = new ReentrantLock();
        plates = new PlatesCyclicCollection(n/2);
        chalices = new ChalicesCyclicCollection(n/2);
        knightsQueue = new KnightsPriorityQueue(lock, n);
        servantGate = lock.newCondition();
        nrOfPlatesTaken = 0;
    }
    private boolean hasIthKAvailableResources(int i) {
        return chalices.getChaliceForIthK(i).isFree() && !plates.getPlateForIthK(i).isEmpty() && plates.getPlateForIthK(i).isFree();
    }

    public void occupyResources(int i) throws InterruptedException {
        lock.lock();
        try {

            //if resources unavailable
            //    update arrival time
            //    add yourself to awaiting queue
            //    await on conditional
            boolean firstTime = true;
            while (!hasIthKAvailableResources(i) || anyNeighbourStarved(i)) {
                if (firstTime) {
                    knightsQueue.updateTimeStamp(i);
                    firstTime = false;
                }
                knightsQueue.markAsWaiting(i);
                knightsQueue.getConditional(i).await();
            }
            knightsQueue.resetTimeStamp(i);

            chalices.getChaliceForIthK(i).take();
            plates.getPlateForIthK(i).take();
            nrOfPlatesTaken++;
            //
            if (!isServantWaiting) {
                signalReadyKnight();
                //choose prioritized knight
                //block resources for his neighbours
                //notify prioritized knight
            }
        } finally {
            lock.unlock();
        }
    }

    private boolean anyNeighbourStarved(int i) {
        boolean neighbourStarved = false;
        Long myArrival = knightsQueue.getTimeStamp(i);
        Long neighbourOneArrival = knightsQueue.getTimeStamp(i-1);
        Long neighbourTwoArrival = knightsQueue.getTimeStamp(i+1);
        if (myArrival - neighbourOneArrival > Main.STARVATION_TRESHOLD)
            neighbourStarved = true;
        if (myArrival - neighbourTwoArrival > Main.STARVATION_TRESHOLD)
            neighbourStarved = true;

        return neighbourStarved;
    }

    public CucumberPlate takePlate(int i) {
        return plates.getPlateForIthK(i);
    }
    public Chalice takeChalice(int i) {
        return chalices.getChaliceForIthK(i);
    }
    public CucumberPlate[] takeAllPlates() {
        return plates.TakeAll();
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
                if (knightsQueue.isIthKWaitingForResources(i-1) && hasIthKAvailableResources(i-1)) {
                    knightsQueue.markAsReady(i-1);
                }
                if (knightsQueue.isIthKWaitingForResources(i+1) && hasIthKAvailableResources(i+1)) {
                    knightsQueue.markAsReady(i+1);
                }
                signalReadyKnight();
            }
        } finally {
            lock.unlock();
        }
    }

    public void OccupyResourcesToFillPlates() throws InterruptedException {
        lock.lock();
        try {
            while (nrOfPlatesTaken > 0) {
                isServantWaiting = true;
                servantGate.await();
            }
            isServantWaiting = false;
            plates.occupyAll();
        } finally {
            lock.unlock();
        }
    }

    public void ReleaseResourcesAfterRefill() {

        lock.lock();
        try {
            for (CucumberPlate cp:takeAllPlates()) {
                cp.putDown();
            }
            knightsQueue.moveAllWaitingToReady();
            signalReadyKnight();
        } finally {
            lock.unlock();
        }
    }

    private void signalReadyKnight() {
        if (knightsQueue.isAnyWaitingKnightReady()) {
            PriorityElement p = knightsQueue.getNextElement();
            int index = p.getIndex();
            // move to awaiting q if i-1, i+1 is ready
            if (knightsQueue.isIthKReady(index - 1)) {
                knightsQueue.markAsWaiting(index - 1);
            }
            if (knightsQueue.isIthKReady(index + 1)) {
                knightsQueue.markAsWaiting(index + 1);
            }
            p.getNotifier().signal();
        }
    }

}
