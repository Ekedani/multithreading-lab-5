import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceSystem {
    private final int channelsNumber;
    private final int taskExecutionTime;
    private final int averageTimeBetweenTasks;
    private final int minimumProcessedNumber;

    ArrayBlockingQueue<Task> tasksQueue;

    private final AtomicInteger processedCounter = new AtomicInteger(0);
    private int failureCounter = 0;

    public ServiceSystem(int queueSize, int channelsNumber, int taskExecutionTime, int averageTimeBetweenTasks, int minimumProcessedNumber) {
        this.channelsNumber = channelsNumber;
        this.taskExecutionTime = taskExecutionTime;
        this.averageTimeBetweenTasks = averageTimeBetweenTasks;
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

        while (processedCounter.get() < minimumProcessedNumber) {
            try {
                if (!tasksQueue.offer(new Task(taskExecutionTime))) {
                    failureCounter++;
                }
                Thread.sleep(averageTimeBetweenTasks);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (var channel : channels) {
            channel.stop();
        }
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
