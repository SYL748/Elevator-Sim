import java.util.Collections;
import java.util.PriorityQueue;

public class Elevator {
    private final int id;
    private int currFloor = 1;
    private ElevatorStatus stat = ElevatorStatus.IDLE;
    private final PriorityQueue<Integer> upQueue = new PriorityQueue<>();
    private final PriorityQueue<Integer> downQueue = new PriorityQueue<>(Collections.reverseOrder());
    private final PriorityQueue<Integer> backUpupQueue = new PriorityQueue<>();
    private final PriorityQueue<Integer> backUpdownQueue = new PriorityQueue<>(Collections.reverseOrder());
    private boolean emergencyStop = false;
    private boolean goingUp = true;
    int next = -1;

    // disguish different elevators by their ids
    public Elevator(int id) {
        this.id = id;
    }

    // add request
    public void addRequest(int from, int to) {
        // emergency stop prevent inputs
        if (emergencyStop && stat == ElevatorStatus.EMERGENCY_STOP) {
            System.out.printf("Elevator-%d is in EMERGENCY STOP, unable to take requests%n", id);
            return;
        }

        boolean userGoUp = to > from;
        boolean userGoDown = to < from;

        // check the initial stat of the elevator, up means carrying passenger that wanna go up, and vice versa for down
        if (stat == ElevatorStatus.IDLE && upQueue.isEmpty() && downQueue.isEmpty()
                && backUpupQueue.isEmpty() && backUpdownQueue.isEmpty()) {
            if (userGoUp) {
                goingUp = true;
                stat = ElevatorStatus.UP;
            } else if (userGoDown) {
                goingUp = false;
                stat = ElevatorStatus.DOWN;
            }
            System.out.printf("Elevator %d initial direction set to %s based on first request.%n",
                    id, goingUp ? "UP" : "DOWN");
        }


        if (userGoUp) {      // passeanger is going up
            if (goingUp) {   // flow of the elevator (carry passenger that goes up)
                // normal case handling, append up requests if from request is higher than elevator's current floor
                // handles the case where elevator try going to bottom floor to handle passengers going up and accumulate up request
                if (from >= currFloor || (upQueue.isEmpty()) || (next < currFloor)) {
                    addUnique(upQueue, from);
                    addUnique(upQueue, to);
                // pressed up when elevator goes pass this floor, need to wait for next round
                } else {
                    addUnique(backUpupQueue, from);
                    addUnique(backUpupQueue, to);
                }
                // handle case where passenger tries to go up but elevator is going downward at the moment
            } else {
                    addUnique(upQueue, to);
                    addUnique(upQueue, from);
            }
        }

        else if (userGoDown) {        // passeanger is going down
            if (!goingUp) {           //flow of the elevator (carry passenger that goes down)
                // normal case handling, append down requests if from request is lower than elevator's current floor
                // handles the case where elevator try going to top floor to handle passengers going down and accumulate down request
                if (from <= currFloor || (downQueue.isEmpty()) || (next > currFloor)) {
                    addUnique(downQueue, from);
                    addUnique(downQueue, to);
                    // pressed down when elevator goes pass this floor, need to wait for next round
                } else {
                    addUnique(backUpdownQueue, from);
                    addUnique(backUpdownQueue, to);
                }
                // handle case where passenger tries to go down but elevator is going upward at the moment
            } else {
                    addUnique(downQueue, to);
                    addUnique(downQueue, from);
            }
        }
        System.out.printf("%12sElevator %d request added: from %d to %d%n", " ", id, from, to);
        displayAllQueues();
    }

    // handle cases where multiple input of the same floor
    private void addUnique(PriorityQueue<Integer> queue, int floor) {
        if (!queue.contains(floor)) {
            queue.add(floor);
        }
    }

    // emergency stop pressed
    public void emergencyStop() {
        emergencyStop = true;
        stat = ElevatorStatus.EMERGENCY_STOP;
        upQueue.clear();
        downQueue.clear();
        backUpdownQueue.clear();
        backUpupQueue.clear();
        System.out.printf("\n\n\nElevator %d EMERGENCY STOP Triggered\n\n\n", id);
    }

    // elevator status floor by floor
    public void progress() {
        // do nothing if emergency stopped
        if (emergencyStop && stat == ElevatorStatus.EMERGENCY_STOP)
            return;

        // go idle if this boolean is true
        if (stat_IDLE()) {
            print();
            return;
        }

        // elevator in upward motion
        if (goingUp) {
            stat = ElevatorStatus.UP;
            // see the next stop
            if (!upQueue.isEmpty()) {
                next = upQueue.peek();
            // upQueue all finished
            } else {
                stat = ElevatorStatus.DOWN;
                goingUp = false;
                // check if any passenger are waiting to go down
                if (downQueue.isEmpty()) {
                    // if not then check anyone waiting for the next round going up
                    if (!backUpupQueue.isEmpty()) {
                        stat = ElevatorStatus.UP;
                        upQueue.addAll(backUpupQueue);
                        backUpupQueue.clear();
                        goingUp = true;
                        next = upQueue.peek();
                    }
                // if yes travel to the passenger that wanna go down
                } else {
                    next = downQueue.peek();
                }
            }
        } else {
            stat = ElevatorStatus.DOWN;
            // see the next stop
            if (!downQueue.isEmpty()) {
                next = downQueue.peek();
            // downQueue all finished
            } else {
                stat = ElevatorStatus.UP;
                goingUp = true;
                // check if any passenger are waiting to go up
                if (upQueue.isEmpty()) {
                    // if not then check anyone waiting for the next round going down
                    if (!backUpdownQueue.isEmpty()) {
                        stat = ElevatorStatus.DOWN;
                        downQueue.addAll(backUpdownQueue);
                        backUpdownQueue.clear();
                        goingUp = false;
                        next = downQueue.peek();
                    }
                // if yes travel to the passenger that wanna go up
                } else {
                    next = upQueue.peek();
                }
            }
        }

        // move elevator according to the next passenger
        if (currFloor < next)
            currFloor++;
        else if (currFloor > next)
            currFloor--;

        // move all next round passenger that tries to go up to upQueue
        if (!goingUp && upQueue.isEmpty() && !backUpupQueue.isEmpty()) {
            upQueue.addAll(backUpupQueue);
            backUpupQueue.clear();
        }
        // move all next round passenger that tries to go down to downQueue
        if (goingUp && downQueue.isEmpty() && !backUpdownQueue.isEmpty()) {
            downQueue.addAll(backUpdownQueue);
            backUpdownQueue.clear();
        }
        print();
        // if next passenger reached
        if (currFloor == next) {
            // poll upQueue since reached
            if (goingUp && !upQueue.isEmpty() && upQueue.peek() == currFloor)
                upQueue.poll();
            // poll downQueue since reached
            else if (!goingUp && !downQueue.isEmpty() && downQueue.peek() == currFloor) {
                downQueue.poll();
            }
            System.out.printf("%12sEXTRACT FLOOR %d FROM %sQUEUE\n", " ", currFloor, stat);
            displayAllQueues();
        }
    }

    public boolean stat_IDLE() {
        if (upQueue.isEmpty() && downQueue.isEmpty() && backUpdownQueue.isEmpty() && backUpupQueue.isEmpty()) {
            stat = ElevatorStatus.IDLE;
            return true;
        }
        return false;
    }

    public void print() {
        System.out.printf("\n_________________Elevator %d → Floor %d (%s)_________________\n", id, currFloor, stat);
    }

    public void displayAllQueues() {
        System.out.printf("%12sUp Queue:           %s%n", " ", upQueue);
        System.out.printf("%12sDown Queue:         %s%n", " ", downQueue);
        System.out.printf("%12sBackup Up Queue:    %s%n", " ", backUpupQueue);
        System.out.printf("%12sBackup Down Queue:  %s%n\n", " ", backUpdownQueue);
    }
}