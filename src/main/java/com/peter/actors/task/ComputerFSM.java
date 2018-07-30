package com.peter.actors.task;

import akka.actor.AbstractFSM;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.time.Duration;


//FSM states

enum State {
    INACTIVE, ON_DUTY, HIT
};

//data stored in FSM

interface Data {
};

enum Uninitialized implements Data {
    Uninitialized
};

class StrikesDataStorage implements Data {

    private long startTime;

    private int shellsCounter;

    StrikesDataStorage(long startTime) {

        this.startTime = startTime;

    }

    void addShell() {
        shellsCounter++;
    }

    int getShellsCounter() {
        return shellsCounter;
    }

}


public class ComputerFSM extends AbstractFSM<State, Data> {

    private ActorRef tank;

    public ComputerFSM(ActorRef tank) {

        this.tank = tank;

        startWith(com.peter.actors.task.State.INACTIVE, Uninitialized.Uninitialized);

        when(com.peter.actors.task.State.INACTIVE,
                matchEvent(ComputerMessages.class,
                        (message, stateDate) -> (message == ComputerMessages.StartComputer && stateDate == Uninitialized.Uninitialized),
                        (message, stateDate) -> {
                            System.out.println("ON_DUTY state begins now");
                            return goTo(com.peter.actors.task.State.ON_DUTY).using(new StrikesDataStorage(System.currentTimeMillis()));
                        }
                )
        );

        when(com.peter.actors.task.State.INACTIVE,
                matchEvent(ComputerMessages.class,
                        (message, stateDate) -> (message == ComputerMessages.Strike),
                        (message, stateDate) -> {
                            System.out.println("Strike in inactive state");
                            return stay();
                        }
                )
        );


        onTransition(
                matchState(com.peter.actors.task.State.ON_DUTY,
                        com.peter.actors.task.State.HIT,
                        () -> {
                            System.out.println("sends stop command to tank");
                            tank.tell(ComputerMessages.StopTank, self());
                        }
                )
        );

        when(com.peter.actors.task.State.ON_DUTY,
                matchEvent(ComputerMessages.class,
                        (message, stateDate) -> (message == ComputerMessages.Strike && stateDate instanceof StrikesDataStorage),
                        (message, stateDate) -> {
                            StrikesDataStorage storage = (StrikesDataStorage) stateDate;
                            storage.addShell();
                            int shells = storage.getShellsCounter();
                            if (shells >= 3) {
                                System.out.println("HIT state begins now");
                                return goTo(com.peter.actors.task.State.HIT).using(storage);
                            } else {
                                System.out.println("ON_DUTY state continues");
                                return stay().using(storage);
                            }
                        }

                )
        );

        when(com.peter.actors.task.State.HIT, Duration.ofSeconds(5L), matchAnyEvent((message, stateData) -> goTo(com.peter.actors.task.State.INACTIVE)));


        initialize();


    }

    static public Props props(ActorRef tank) {
        return Props.create(ComputerFSM.class, () -> new ComputerFSM(tank));
    }


}
