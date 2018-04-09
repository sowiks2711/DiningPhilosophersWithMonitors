package com.sowiks.monitors.monitor_helpers;

import com.sowiks.Remainder;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class KnightsPriorityQueue {
    private PriorityElement[] knights;
    private boolean[] awaitingForResource;//awaitingForResources set
    private TreeMap<PriorityElement, PriorityElement> readyQueue;//ready priority queue

    public KnightsPriorityQueue(Lock lock, int n) {
        knights = new PriorityElement[n];
        for (int i = 0; i < n ; i++) {
            knights[i] = new PriorityElement(lock, i);
        }
        awaitingForResource = new boolean[n];
        readyQueue = new TreeMap<>(new PriorityElementComparator());
    }
    //mark as waiting
    public void markAsWaiting(int i) {
        int k = mapIndex(i);
        PriorityElement rpe = knights[k];
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
        PriorityElement rpe = knights[k];
        readyQueue.put(rpe,rpe);
        awaitingForResource[k] = false;
    }

    private int mapIndex(int i) {
        return Remainder.rem(i, knights.length);
    }

    //get condition for the next feaster
    public PriorityElement getNextElement() {
        PriorityElement e = readyQueue.firstKey();
        readyQueue.remove(e);
        return e;
    }

    public void updateTimeStamp(int i) {
        knights[i].setArrivalTime();
    }

    public Condition getConditional(int i) {
        int k = mapIndex(i);
        return knights[k].getNotifier();
    }

    public boolean isAnyWaitingKnightReady() {
        return readyQueue.size() != 0;
    }

    public boolean isIthKReady(int i) {
        int k = mapIndex(i);
        return readyQueue.containsKey(knights[k]);
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
        return knights[k].getArrivalTime();
    }

    public void resetTimeStamp(int i) {
        int k = mapIndex(i);
        knights[k].resetArrivalTime();
    }
}
