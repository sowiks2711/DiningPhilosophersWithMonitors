package com.sowiks;

import com.sowiks.actors.Knight;

public class Main {
    private static final int N = 5;

    public static void main(String[] args) {
        if (N < 4 || (N%2) != 0)
            throw  new IllegalArgumentException(
                    "N=" + N + " should be even and greater than or equal to 4"
            );
        Thread[] knights = new
        (new Thread(new Knight("Lancelot"))).start();
        (new Thread(new Knight("Zawisza Czarny"))).start();
        (new Thread(new Knight("Ragnar"))).start();
        (new Thread(new Knight("Roland"))).start();
    }
}
