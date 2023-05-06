import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

public class ServiceSystem {
    private final int channelsNumber;
    private final long modellingTime;
    private final int averageTimeBetweenTasks;

    ArrayBlockingQueue<Task> tasksQueue;

    private double totalFailures = 0;

    public ServiceSystem(int queueSize, int channelsNumber, long modellingTime, int averageTimeBetweenTasks) {
        this.channelsNumber = channelsNumber;
        this.modellingTime = modellingTime;
        this.averageTimeBetweenTasks = averageTimeBetweenTasks;
        tasksQueue = new ArrayBlockingQueue<>(queueSize);
    }

    public void launch() {
        var channels = new ArrayList<Channel>();
        var channelsPool = Executors.newFixedThreadPool(channelsNumber);
        for (int i = 0; i < channelsNumber; i++) {
            var channel = new Channel(tasksQueue);
            channels.add(channel);
            channelsPool.execute(channel);
        }
        var startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < modellingTime) {
            try {
                if (!tasksQueue.offer(new Task(50))) {
                    totalFailures++;
                }
                Thread.sleep(averageTimeBetweenTasks);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (var channel : channels) {
            channel.stop();
        }
        channelsPool.shutdown();
    }
}
