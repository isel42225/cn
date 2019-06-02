public class MonitorSetThresholdCommand extends Command {
    private static String COMMAND_TITLE = "Set monitor image threshold";

    private final MonitorClient monitorClient;

    public MonitorSetThresholdCommand(MonitorClient monitorClient) {
        super(COMMAND_TITLE);
        this.monitorClient = monitorClient;
    }

    @Override
    public void execute() {
        monitorClient.setThreshold();
    }
}
