package com.sowiks;

public class Remainder {
    public static int rem(int x, int n) {
        int r = x % n;
        if (r < 0)
        {
            r += n;
        }
        return r;
    }
}
