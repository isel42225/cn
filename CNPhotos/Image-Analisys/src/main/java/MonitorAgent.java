import pubsub.PubSubManager;

public class MonitorAgent {

    private final PubSubManager pubSubManager = new PubSubManager();


    public void notifyWork() {
        pubSubManager.publishMetric();
    }
}
