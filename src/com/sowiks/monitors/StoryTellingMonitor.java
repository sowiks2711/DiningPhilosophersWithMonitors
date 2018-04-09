package com.sowiks.monitors;

import com.sowiks.Main;
import com.sowiks.monitors.monitor_helpers.KnightsPriorityQueue;
import com.sowiks.monitors.monitor_helpers.PriorityElement;
import com.sowiks.monitors.monitor_helpers.TalkingKnightsCyclicFlags;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StoryTellingMonitor {
    private Lock lock;
    KnightsPriorityQueue knightsQueue;
    TalkingKnightsCyclicFlags knightsTalking;
    public StoryTellingMonitor() {
        lock = new ReentrantLock();
        knightsQueue = new KnightsPriorityQueue(lock, Main.N);
        knightsTalking = new TalkingKnightsCyclicFlags(Main.N);
    }
    public void occupyAttention(int i) throws InterruptedException {
        lock.lock();
        try {
            boolean firstTime = true;
            while (isKingTalking() || areNeighboursTalking(i) || anyNeighbourStarved(i)) {
                if (firstTime) {
                    knightsQueue.updateTimeStamp(i);
                    firstTime = false;
                }
                knightsQueue.markAsWaiting(i);
                knightsQueue.getConditional(i).await();
            }
            knightsTalking.ithKnightTalkingStart(i);
            knightsQueue.resetTimeStamp(i);
            if (!isKingTalking())
                signalReadyKnight();
        } finally {
            lock.unlock();
        }
    }
    public void releaseAttention(int i) {
        lock.lock();
        try {
                //add neighbours to ready queue if resources available
                //take prioritised ready knight
                //move his neighbours to awaitingForResources
                //signal knight
            knightsTalking.ithKnightsTalkingStop(i);

            if (knightsQueue.isIthKWaitingForResources(i-1) && !areNeighboursTalking(i-1)) {
                knightsQueue.markAsReady(i-1);
            }
            if (knightsQueue.isIthKWaitingForResources(i+1) && !areNeighboursTalking(i+1)) {
                knightsQueue.markAsReady(i+1);
            }
            if (!isKingTalking())
                signalReadyKnight();
        } finally {
            lock.unlock();
        }
    }
    private boolean isKingTalking() {
        return knightsTalking.isIthKnightTalking(Main.KING_INDEX);
    }
    private boolean areNeighboursTalking(int i) {
        return  (knightsTalking.isIthKnightTalking(i-1) || knightsTalking.isIthKnightTalking(i+1));
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
