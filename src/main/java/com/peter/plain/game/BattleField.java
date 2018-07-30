package com.peter.plain.game;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The game where Tank is approaching you
 * Tank displays the distance (from 500 to 100 m)
 * You have 3 times to throw a grenade
 * (by typing number between 3 and 10)
 * If tank distance dividable by this number you win
 * If you failed 3 times - you fail.
 * *
 *
 */
public class BattleField {

    public static ExecutorService theExecutor = Executors.newFixedThreadPool(10);

    private static int maxTanks = 10;

    public static void main(String[] args) {

         for(int i=1; i <= maxTanks; i++){
             Tank tank = new Tank(i);
             theExecutor.submit(tank);
         }

    }

}
