public class ReqEvent {
    int tick;
    int from, to;
     boolean emergency;

    ReqEvent(int tick, int from, int to) {
        this.tick = tick;
        this.from = from;
        this.to = to;
    }

    ReqEvent(int tick, boolean emergency) {
        this.tick = tick;
        this.emergency = emergency;
    }
}