public class SystemObserver extends Thread {
    private final long checkInterval;
    private ServiceSystem system;

    private int totalChecks = 0;
    private double queueSizesSum = 0;

    public SystemObserver(long checkInterval) {
        this.checkInterval = checkInterval;
    }

    @Override
    public void run() {
        try {
            while (system.getIsRunning().get()) {
                queueSizesSum += system.getQueueSize();
                totalChecks++;
                Thread.sleep(checkInterval);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSystem(ServiceSystem system) {
        this.system = system;
    }

    public void printFinalResults() {
        System.out.println("System #" + system.getId() + " average queue size: " + (queueSizesSum / totalChecks));
        System.out.println("System #" + system.getId() + " failure probability: " + system.getFailureProbability());
    }
}
