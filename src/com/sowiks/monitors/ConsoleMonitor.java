package com.sowiks.monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConsoleMonitor {
    private Lock lock;
    public ConsoleMonitor() {
       lock = new ReentrantLock();
    }
    public void log(String name, String action) {
        lock.lock();
        System.out.println(name + "\t\t:" + action);
        lock.unlock();
    }
}
