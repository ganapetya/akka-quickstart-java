package com.peter.plain.game;

public class Shell implements Runnable{
    private int distance;
    private int step;
    private int maxDistance;
    private int tankId;
    private int shellId;
    private Tank tank;
    volatile boolean stopped = false;



    public Shell(int tankId, int tankDistance, Tank tank){
        step = 3;
        maxDistance = tankDistance + 3000;
        this.distance = tankDistance;
        this.tankId = tankId;
        shellId = hashCode();
        this.tank = tank;
    }

    @Override
    public void run() {
        while(!stopped) {
            distance = distance + step;
            if (distance >= maxDistance) {
                stopMe();
            }
        }
    }

    private void stopMe() {
        stopped  = true;
        System.out.println("Shell of tank "+tankId+ " "+ shellId+" arrived - distance "+ distance +" !!!");
        tank.reportShellArrived();
    }
}
