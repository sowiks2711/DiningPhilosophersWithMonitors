package com.sowiks.actors;

import com.sowiks.monitors.ConsoleMonitor;

public class NamedLoggable {
    private String name;
    private ConsoleMonitor logger;

    public NamedLoggable(String name, ConsoleMonitor logger) {
        this.name = name;
        this.logger = logger;
    }
    protected void log(String action) {
        logger.log(name, action);
    }
}
