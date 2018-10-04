package com.peter.actors.game;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;


public class BattleFieldActor extends AbstractActor {

    private int maxTanks = 10;

    /**
     * BattleFieldActor factory
     *
     */
    static public Props props() {
         return Props.create(BattleFieldActor.class, () -> new BattleFieldActor());
    }

    /**
     *
     * Here we receive different messages:
     */
    @Override
    public Receive createReceive() {

       return receiveBuilder().match(String.class, //type of the message
                command -> command.equals("start"), //check some message parameter o filter out inappropriate messages
                command -> {                        //what to do if the message received
                    System.out.println("Starting the game...");
                    for (int i = 1; i <= maxTanks; i++) {
                        final ActorRef tankActor = context().actorOf(TankActor.props(), "tank"+i); //Create number of tank actors
                        tankActor.tell("start", self());                                            //send them command to start
                    }
                    sender().tell("started", self());                                               //report the sender (BattleField) that the game is started
                }
        ).build();

    }


}
