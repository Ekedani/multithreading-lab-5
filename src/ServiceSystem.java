import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceSystem {
    private final int channelsNumber;
    private final int taskExecutionTime;
    private final int averageTimeBetweenTasks;
    private final int timeStandardDeviation;
    private final int minimumProcessedNumber;

    private final ArrayBlockingQueue<Task> tasksQueue;
    private final AtomicInteger processedCounter = new AtomicInteger(0);
    private int failureCounter = 0;

    public ServiceSystem(int queueSize, int channelsNumber, int taskExecutionTime, int averageTimeBetweenTasks, int timeStandardDeviation, int minimumProcessedNumber) {
        this.channelsNumber = channelsNumber;
        this.taskExecutionTime = taskExecutionTime;
        this.averageTimeBetweenTasks = averageTimeBetweenTasks;
        this.timeStandardDeviation = timeStandardDeviation;
        this.minimumProcessedNumber = minimumProcessedNumber;
        tasksQueue = new ArrayBlockingQueue<>(queueSize);
    }

    public void simulate() {
        var channels = new ArrayList<ServiceChannel>();
        var channelsPool = Executors.newFixedThreadPool(channelsNumber);

        for (int i = 0; i < channelsNumber; i++) {
            var channel = new ServiceChannel(tasksQueue, processedCounter);
            channels.add(channel);
            channelsPool.execute(channel);
        }

        var random = new Random();
        while (processedCounter.get() < minimumProcessedNumber) {
            try {
                if (!tasksQueue.offer(new Task(taskExecutionTime))) {
                    failureCounter++;
                }
                var waitingTime = (long) (averageTimeBetweenTasks + timeStandardDeviation * random.nextGaussian());
                Thread.sleep(waitingTime > 0 ? waitingTime : 1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        channelsPool.shutdownNow();
    }


    public void printStats() {
        // TODO: Remove this debug method later
        System.out.println("Total processed: " + processedCounter.get());
        System.out.println("Total failures: " + failureCounter);
    }

    public double getFailureProbability() {
        double failures = failureCounter;
        double successes = processedCounter.get();
        return failures / (failures + successes);
    }
}
