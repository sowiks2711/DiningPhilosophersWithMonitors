package com.sowiks.monitors.monitor_helpers;

import com.sowiks.Remainder;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FeastQueue {
    private PriorityElement[] feasters;
    private boolean[] awaitingForResource;//awaitingForResources set
    private TreeMap<PriorityElement, PriorityElement> readyQueue;//ready priority queue

    public FeastQueue(Lock lock, int n) {
        feasters = new PriorityElement[n];
        for (int i = 0; i < n ; i++) {
            feasters[i] = new PriorityElement(lock, i);
        }
        awaitingForResource = new boolean[n];
        readyQueue = new TreeMap<>(new PriorityElementComparator());
    }
    //mark as waiting
    public void markAsWaiting(int i) {
        int k = mapIndex(i);
        PriorityElement rpe = feasters[k];
        if (readyQueue.containsKey(rpe)){
            readyQueue.remove(rpe);
        }
        awaitingForResource[k] = true;
    }
    public boolean isIthKWaitingForResources(int i) {
        int  k = mapIndex(i);
        return awaitingForResource[k];
    }


    //mark as ready
    public void markAsReady(int i) {
        int k = mapIndex(i);
        PriorityElement rpe = feasters[k];
        readyQueue.put(rpe,rpe);
        awaitingForResource[k] = false;
    }

    private int mapIndex(int i) {
        return Remainder.rem(i, feasters.length);
    }

    //get condition for the next feaster
    public PriorityElement getNextElement() {
        PriorityElement e = readyQueue.firstKey();
        readyQueue.remove(e);
        return e;
    }

    public void updateTimeStamp(int i) {
        feasters[i].setArrivalTime();
    }

    public Condition getConditional(int i) {
        int k = mapIndex(i);
        return feasters[k].getNotifier();
    }

    public boolean isAnyWaitingKnightReady() {
        return readyQueue.size() != 0;
    }

    public boolean isIthKReady(int i) {
        int k = mapIndex(i);
        return readyQueue.containsKey(feasters[k]);
    }

    public void moveAllWaitingToReady() {
        for (int i = 0; i < awaitingForResource.length ; i++) {
            if (awaitingForResource[i]) {
                markAsReady(i);
            }
        }
    }

    public Long getTimeStamp(int i) {
        int k = mapIndex(i);
        return feasters[k].getArrivalTime();
    }

    public void resetTimeStamp(int i) {
        int k = mapIndex(i);
        feasters[k].resetArrivalTime();
    }
}
