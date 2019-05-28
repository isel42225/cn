import pubsub.PubSubManager;

public class MonitorAgent {

    private final PubSubManager pubSubManager = new PubSubManager();
    private long nOfImgs = 0;
    private long startTime = System.currentTimeMillis();

    public void notifyWork() {
        nOfImgs +=1;
        long now = System.currentTimeMillis();
        float elapsedInSecs = (now - startTime)/ 1000;
        float elapsedInMins = elapsedInSecs / 60;
        float imgsPerMin = nOfImgs / elapsedInMins;
        pubSubManager.publishMetric(imgsPerMin);
    }
}
