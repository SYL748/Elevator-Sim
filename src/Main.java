import java.util.List;

public class Main {
    public static void main(String[] args) {
        Elevator e1 = new Elevator(1);

        List<ReqEvent> test1 = List.of(
                new ReqEvent(0, 1, 10),
                new ReqEvent(2, 4, 9),
                new ReqEvent(3, 6, 7));
        List<ReqEvent> test2 = List.of(
                new ReqEvent(0, 10, 1),
                new ReqEvent(2, 11, 2),
                new ReqEvent(3, 7, 6));
        List<ReqEvent> test3 = List.of(
                new ReqEvent(0, 1, 5),
                new ReqEvent(5, 5, 9),
                new ReqEvent(3, 2, 11));
        List<ReqEvent> test4 = List.of(
                new ReqEvent(0, 5, 1),
                new ReqEvent(2, 6, 2),
                new ReqEvent(8, 6, 1));
        List<ReqEvent> test5 = List.of(
                new ReqEvent(0, 1, 10),
                new ReqEvent(2, 9, 2),
                new ReqEvent(3, 5, 6));
        List<ReqEvent> test6 = List.of(
                new ReqEvent(0, 2, 10),
                new ReqEvent(0, 2, 1),
                new ReqEvent(3, 5, 6));
        List<ReqEvent> test7 = List.of(
                new ReqEvent(0, 2, 10),
                new ReqEvent(4, true),
                new ReqEvent(5, 2, 1));
        List<ReqEvent> test8 = List.of(
                new ReqEvent(0, 1, 6),
                new ReqEvent(1, 3, 1),
                new ReqEvent(2, 4, 1),
                new ReqEvent(3, 4, 5),
                new ReqEvent(4, 1, 7),
                new ReqEvent(8, 2, 4));

        int tick = 0;
        while (tick < 25) {
            System.out.printf("TICK %d\n", tick);
            e1.progress();
            for (ReqEvent e : test5) {
                if (e.tick == tick)
                    if (e.emergency) {
                        e1.emergencyStop();
                    } else {
                        e1.addRequest(e.from, e.to);
                    }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            tick++;
        }
    }
}