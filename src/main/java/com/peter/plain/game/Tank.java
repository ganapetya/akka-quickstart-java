package com.peter.plain.game;

import java.util.concurrent.atomic.AtomicInteger;

public class Tank implements Runnable {
    private int distance;
    private int step;
    private int macDistance;
    private int id;
    private volatile AtomicInteger arrivedCounter = new AtomicInteger();//?
    volatile boolean stopped = false;//?


    public Tank(int id) {
        distance = 0;
        step = 2;
        macDistance = 500;
        this.id = id;
    }


    @Override
    public void run() {

        while (!stopped) {

            distance = distance + step;

            if (distance % 10 == 0) {
                shot();
            }

            if (distance >= macDistance) {
                stopMe();
            }

            System.out.println("Arrived till now "+ arrivedCounter.get()+ " shells!");

        }

    }

    private void stopMe() {
        stopped = true;
        System.out.println("Tank " + id + " stops at distance " + distance);
    }

    private void shot() {
        System.out.println("Tank " + id + " shoots!");
        Shell shell = new Shell(id, distance, this);
        BattleField.theExecutor.submit(shell);
    }


    public void reportShellArrived() {
        arrivedCounter.incrementAndGet();
    }
}
