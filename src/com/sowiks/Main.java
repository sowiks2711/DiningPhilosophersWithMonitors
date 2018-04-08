package com.sowiks;

import com.sowiks.actors.Knight;

public class Main {
    private static final int N = 4;
    public static final int PLATE_CAPACITY = 4;
    public static long POUR_TIME = 200;
    public static long DRINK_TIME = 200;
    public static void main(String[] args) {
        if (N < 4 || (N%2) != 0)
            throw  new IllegalArgumentException(
                    "N=" + N + " should be even and greater than or equal to 4"
            );
        (new Thread(new Knight("Lancelot"))).start();
        (new Thread(new Knight("Zawisza Czarny"))).start();
        (new Thread(new Knight("Ragnar"))).start();
        (new Thread(new Knight("Roland"))).start();
        System.out.println(-1%2);
    }
}
