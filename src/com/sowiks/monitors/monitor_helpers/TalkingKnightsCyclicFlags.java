package com.sowiks.monitors.monitor_helpers;

import com.sowiks.Remainder;

public class TalkingKnightsCyclicFlags {
    boolean[] talkingKnightsFlags;
    public TalkingKnightsCyclicFlags(int n) {
        talkingKnightsFlags = new boolean[n];
    }
    public boolean isIthKnightTalking(int i) {
        int k = Remainder.rem(i, talkingKnightsFlags.length);
        return talkingKnightsFlags[k];
    }
    public void ithKnightTalkingStart(int i) {
        int k = Remainder.rem(i, talkingKnightsFlags.length);
        talkingKnightsFlags[k] = true;
    }
    public void ithKnightsTalkingStop(int i) {
        int k = Remainder.rem(i, talkingKnightsFlags.length);
        talkingKnightsFlags[k] = false;
    }
}
