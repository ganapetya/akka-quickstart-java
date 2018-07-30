package com.peter.actors.game;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.io.IOException;

public class GameStart {

    public static void main(String[] args) {

        final ActorSystem system = ActorSystem.create("akkaGame");

        try {

            final ActorRef battleFiledActor = system.actorOf(BattleFieldActor.props(), "BattleFieldActor");

            battleFiledActor.tell("start", ActorRef.noSender());

            System.in.read();

        } catch (IOException ioe) {

            ioe.printStackTrace();

        } finally {

            system.terminate();
        }
    }
}
