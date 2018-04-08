package com.sowiks.monitors.monitor_helpers;

import java.util.Date;
import java.util.concurrent.locks.Condition;

public class PriorityElement {
    Date arrivalTime;
    Condition gate;
}
