package com.peter.actors.game;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class ShellActor extends AbstractActor {

    private int distance;
    private int step;
    private int maxDistance;

    public ShellActor(int tankDistance) {
        step = 3;
        maxDistance = tankDistance + 3000;
        this.distance = tankDistance;
    }

    static public Props props(int tankDistance) {
        return Props.create(ShellActor.class, () -> new ShellActor(tankDistance));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        String.class,
                        command -> command.equals("shoot"),
                        command -> {
                            self().tell("advance", self());
                        }
                ).match(
                        String.class,
                        command -> command.equals("advance"),
                        command -> {
                            distance = distance + step;
                            if (distance < maxDistance) {
                                self().tell("advance", self());
                            } else {
                                System.out.println("Shell " + self() + " arrived - distance " + distance + " !!!");
                                context().parent().tell("shell_arrived", self());
                            }
                        }
                ).build();
    }


}
