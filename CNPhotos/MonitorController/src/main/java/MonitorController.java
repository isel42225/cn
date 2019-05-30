import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import jdk.nashorn.internal.codegen.CompilerConstants;
import pubsub.PubSubManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class MonitorController implements MessageReceiver {

    private class MyTask extends TimerTask {
        private int DECREASE_THRESHOLD = 5;
        private int decreaseTendency = 0;

        public MyTask() {
        }


        @Override
        public void run() {
            try {
                int nOfVms = state.getnOfVms();
                int load = nOfImgs / nOfVms;
                System.out.println("Nr of vms : " + nOfVms);
                System.out.println("Load : " + load);
                if (load > state.getImgThreshold()) {
                    decreaseTendency = 0;
                    increaseVmSize();
                }
                else if(nOfVms > 1){
                    decreaseTendency++;
                    if(decreaseTendency > DECREASE_THRESHOLD){
                        // ver se load / vms - 1 é maior que threshold
                        decreaseVmSize();
                        decreaseTendency = 0;
                    }
                }
                nOfImgs = 0;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private static String PROJECT_ID = ServiceOptions.getDefaultProjectId();
    private static String APP_NAME = "APP";
    private static String VM_GROUP = "instance-group-photo-workers";
    private static String ZONE_NAME = "europe-west2-b";

    private Compute computeService;
    private PubSubManager pubSubManager;
    private MonitorState state;
    private Timer timer ;
    private int nOfImgs = 0;

    public MonitorController() {
        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleCredential credential = GoogleCredential.getApplicationDefault();
            if (credential.createScopedRequired()) {
                List<String> scopes = new ArrayList<>();
                scopes.add(ComputeScopes.COMPUTE);
                credential = credential.createScoped(scopes);
            }
            state = new MonitorState();
            computeService = new Compute
                    .Builder(httpTransport, JacksonFactory.getDefaultInstance(),credential)
                    .setApplicationName(APP_NAME)
                    .build();

            pubSubManager = new PubSubManager();
            pubSubManager.subscribeToMonitor(this);
            timer = new Timer();
            timer.schedule(new MyTask(),0, 1000 * 5);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
        try {
            nOfImgs++;
            ackReplyConsumer.ack();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void increaseVmSize() throws java.io.IOException {
        System.out.println("Increasing vms");
        int currSize = this.state.getnOfVms();
        computeService.instanceGroupManagers()
                .resize(PROJECT_ID, ZONE_NAME, VM_GROUP, currSize + 1)
        .execute();
        this.state.setnOfVms(currSize + 1);
    }

    private void decreaseVmSize() {
        try{
            System.out.println("Decreasing vms");
            int currSize = this.state.getnOfVms();
            computeService.instanceGroupManagers()
                    .resize(PROJECT_ID, ZONE_NAME, VM_GROUP, currSize - 1)
                    .execute();
            this.state.setnOfVms(currSize - 1);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setImgThreshold(int newThreshold) {
        this.state.setImgThreshold(newThreshold);
    }

    public MonitorState getState() {
        return state;
    }

}
