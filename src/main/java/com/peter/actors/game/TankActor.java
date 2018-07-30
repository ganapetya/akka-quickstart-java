package com.peter.actors.game;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.peter.actors.task.ComputerFSM;
import com.peter.actors.task.ComputerMessages;

public class TankActor extends AbstractActor {

    private int distance;
    private int step;
    private int maxDistance;
    private int shell;
    private int arrivedShellsCounter;
    private boolean computerInterrupt;

    private ActorRef computerFsm;

    public TankActor() {
        distance = 0;
        step = 2;
        maxDistance = 500;
        computerFsm = context().actorOf(ComputerFSM.props(self()));
        computerFsm.tell(ComputerMessages.StartComputer, self());
    }

    static public Props props() {
        return Props.create(TankActor.class, () -> new TankActor());
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        String.class,
                        command -> command.equals("start"),
                        command -> {
                            System.out.println("Starting the tank...");
                            self().tell("go", self());
                        }
                ).match(
                        String.class,
                        command -> command.equals("go"),
                        command -> {
                            distance = distance + step;
                            if (distance % 10 == 0) {
                                System.out.println("Tank " + self() + " shoots!");
                                final ActorRef shellActor = context().actorOf(ShellActor.props(distance), "shell-" + (++shell));
                                shellActor.tell("shoot", self());
                            }
                            if ((distance < maxDistance) && !computerInterrupt) {
                                self().tell("go", self());
                            } else {
                                System.out.println("Tank " + self() + " stops at distance " + distance);
                            }
                        }
                ).match(
                        String.class,
                        command -> command.equals("shell_arrived"),
                        command -> {
                            arrivedShellsCounter++;
                            System.out.println("Arrived shell number " + arrivedShellsCounter + " with id " + sender());
                            computerFsm.tell(ComputerMessages.Strike, self());
                        }
                ).match(
                        ComputerMessages.class,
                        command -> command == ComputerMessages.StopTank,
                        command -> {
                            computerInterrupt = true;
                            System.out.println("Tank " + self() + " interrupted by computer ");
                        }
                ).build();
    }

}
