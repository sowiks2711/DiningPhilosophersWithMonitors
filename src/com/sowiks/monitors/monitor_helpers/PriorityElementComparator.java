package com.sowiks.monitors.monitor_helpers;

import java.util.Comparator;

public class PriorityElementComparator implements Comparator<PriorityElement> {
    @Override
    public int compare(PriorityElement o1, PriorityElement o2) {
        return o1.getArrivalTime().compareTo(o2.getArrivalTime());
    }
}
