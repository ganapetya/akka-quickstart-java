package com.peter.actors.game;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class BattleFieldActor extends AbstractActor {

    private int maxTanks = 10;

    static public Props props() {
        return Props.create(BattleFieldActor.class, () -> new BattleFieldActor());
    }

    @Override
    public Receive createReceive() {

        return receiveBuilder().match(String.class,
                command -> command.equals("start"),
                command -> {
                    System.out.println("Starting the game...");
                    for (int i = 1; i <= maxTanks; i++) {
                        final ActorRef tankActor = context().actorOf(TankActor.props(), "tank"+i);
                        tankActor.tell("start", self());
                    }
                    sender().tell("started", self());
                }
        ).build();

    }


}
