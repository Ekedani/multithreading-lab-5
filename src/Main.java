import java.util.ArrayList;

public class Main {
    // ServiceSystem parameters
    public final static int QUEUE_SIZE = 10;
    public final static int CHANNELS_NUMBER = 4;
    public final static int TASK_EXECUTION_TIME = 50;
    public final static int AVERAGE_TIME_BETWEEN_TASKS = 10;
    public final static int TIME_STANDARD_DEVIATION = 5;
    public final static int MINIMUM_PROCESSED_NUMBER = 1000;

    // Observer parameters
    public final static int CHECK_INTERVAL = 500;

    // Testing parameters
    public final static int PARALLEL_SIMULATIONS_NUMBER = 4;

    public static void main(String[] args) {
        //serialSimulationSingle();
        parallelSimulationMultiple();
    }

    public static void serialSimulationSingle() {
        SystemObserver systemObserver = new SystemObserver(CHECK_INTERVAL);
        var serviceSystem = new ServiceSystem(
                QUEUE_SIZE,
                CHANNELS_NUMBER,
                TASK_EXECUTION_TIME,
                AVERAGE_TIME_BETWEEN_TASKS,
                TIME_STANDARD_DEVIATION,
                MINIMUM_PROCESSED_NUMBER,
                systemObserver
        );
        serviceSystem.simulate();
        System.out.println("===== SIMULATION RESULTS =====");
        systemObserver.printSystemParameters();
    }

    public static void parallelSimulationMultiple() {
        ArrayList<SystemObserver> systemObservers = new ArrayList<>();
        ArrayList<ServiceSystem> serviceSystems = new ArrayList<>();
        for (int i = 0; i < PARALLEL_SIMULATIONS_NUMBER; i++) {
            var systemObserver = new SystemObserver(CHECK_INTERVAL);
            var serviceSystem = new ServiceSystem(
                    QUEUE_SIZE,
                    CHANNELS_NUMBER,
                    TASK_EXECUTION_TIME,
                    AVERAGE_TIME_BETWEEN_TASKS,
                    TIME_STANDARD_DEVIATION,
                    MINIMUM_PROCESSED_NUMBER,
                    systemObserver
            );
            systemObservers.add(systemObserver);
            serviceSystems.add(serviceSystem);
        }
        ArrayList<Thread> simulationThreads = new ArrayList<>();
        for (var system : serviceSystems) {
            simulationThreads.add(new Thread(system::simulate));
        }
        for (var thread : simulationThreads) {
            thread.start();
        }
        try {
            for (var thread : simulationThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("===== SIMULATIONS RESULTS =====");
        for (var observer : systemObservers) {
            observer.printSystemParameters();
        }
        System.out.println("===== GLOBAL RESULTS =====");
        double queueSizesSum = 0;
        double failureProbabilitiesSum = 0;
        for (var observer : systemObservers) {
            queueSizesSum += observer.getAverageQueueSize();
            failureProbabilitiesSum += observer.getFailureProbability();
        }
        System.out.println("Global Average queue size: " + queueSizesSum / PARALLEL_SIMULATIONS_NUMBER +
                " | Failure probability: " + failureProbabilitiesSum / PARALLEL_SIMULATIONS_NUMBER);
    }
}