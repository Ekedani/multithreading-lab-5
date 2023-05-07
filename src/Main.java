public class Main {
    public final static int QUEUE_SIZE = 10;
    public final static int CHANNELS_NUMBER = 4;
    public final static int TASK_EXECUTION_TIME = 50;
    public final static int AVERAGE_TIME_BETWEEN_TASKS = 10;
    public final static int TIME_STANDARD_DEVIATION = 5;
    public final static int MINIMUM_PROCESSED_NUMBER = 1000;

    public static void main(String[] args) {
        var serviceSystem = new ServiceSystem(
                QUEUE_SIZE,
                CHANNELS_NUMBER,
                TASK_EXECUTION_TIME,
                AVERAGE_TIME_BETWEEN_TASKS,
                TIME_STANDARD_DEVIATION,
                MINIMUM_PROCESSED_NUMBER
        );

        serviceSystem.simulate();
        System.out.println("===== PARALLEL IMITATION RESULT =====");
        serviceSystem.printStats();
        System.out.println(serviceSystem.getFailureProbability());
    }
}