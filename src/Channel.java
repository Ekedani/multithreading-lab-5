import java.util.concurrent.ArrayBlockingQueue;

public class Channel implements Runnable {
    private final ArrayBlockingQueue<Task> taskQueue;
    private boolean isStopped = false;

    public Channel(ArrayBlockingQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (isStopped) {
            var task = taskQueue.poll();
            try {
                assert task != null;
                task.execute();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        this.isStopped = true;
    }
}
