package com.sowiks.monitors.monitor_helpers;

import java.util.Comparator;

public class PriorityElementComparator implements Comparator<PriorityElement> {
    @Override
    public int compare(PriorityElement o1, PriorityElement o2) {
        Long o1Time = o1.getArrivalTime();
        Long o2Time = o2.getArrivalTime();
        return o1Time.compareTo(o2Time);
    }
}
