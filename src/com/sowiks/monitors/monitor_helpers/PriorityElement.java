package com.sowiks.monitors.monitor_helpers;

import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PriorityElement {
    private Long arrivalTime;
    private Condition gate;
    private int index;

    public PriorityElement(Lock lock, int i) {
        this.arrivalTime = Long.MAX_VALUE;
        gate = lock.newCondition();
        index = i;
    }

    public Long getArrivalTime() {
        return arrivalTime;
    }
    public Condition getNotifier() {
        return gate;
    }
    public int getIndex() {
        return index;
    }

    public void setArrivalTime(long l) {
        arrivalTime = l;
    }
}
