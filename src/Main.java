import java.util.concurrent.Executors;

public class Main {
    public static int TESTS = 4;
    public static int MODELLING_TIME = 1000 * 50;

    public static void main(String[] args) {
        var threadPool = Executors.newFixedThreadPool(TESTS);

        double finalFailureProbability = 0;
        double finalAverageQueueSize = 0;

        System.out.println("===== PARALLEL IMITATION RESULT =====");
    }
}