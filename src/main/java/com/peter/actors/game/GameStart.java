package com.peter.actors.game;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.io.IOException;


/**
 * Game start point
 */
public class GameStart {

    public static void main(String[] args) {

        final ActorSystem system = ActorSystem.create("akkaGame");

        try {

            /**
             * We create a battleFiledActor and send a message to start the new game
             */

            final ActorRef battleFiledActor = system.actorOf(BattleFieldActor.props(), "BattleFieldActor");

            battleFiledActor.tell("start", ActorRef.noSender()); //ActorRef.noSender  - is a way to tell "the message sent not from a particular actor"

            System.in.read(); //only one purpose - leave the main thread running to avoid the premature system termination in the finally

        } catch (IOException ioe) {

            ioe.printStackTrace();

        } finally {

            system.terminate();
        }
    }
}
