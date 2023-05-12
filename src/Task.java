// Is not used anymore because of time, needed to create an object
public class Task {
    private final long executionTime;

    public Task(long executionTime) {
        this.executionTime = executionTime;
    }

    public void execute() throws InterruptedException {
        Thread.sleep(executionTime);
    }
}
