import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceChannel implements Runnable {
    private final ArrayBlockingQueue<Task> taskQueue;
    private final AtomicInteger processedCounter;
    private boolean isStopped = false;

    public ServiceChannel(ArrayBlockingQueue<Task> taskQueue, AtomicInteger processedCounter) {
        this.taskQueue = taskQueue;
        this.processedCounter = processedCounter;
    }

    @Override
    public void run() {
        while (!isStopped) {
            try {
                var task = taskQueue.take();
                task.execute();
                processedCounter.incrementAndGet();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        this.isStopped = true;
    }
}
