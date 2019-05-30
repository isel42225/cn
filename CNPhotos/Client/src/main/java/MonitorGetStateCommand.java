public class MonitorGetStateCommand extends Command {
    private static  String COMMAND_TITLE = "Get monitor state";

    private MonitorClient monitorClient;

    public MonitorGetStateCommand(MonitorClient monitorClient) {
        super(COMMAND_TITLE);
        this.monitorClient = monitorClient;
    }

    @Override
    public void execute() {
        monitorClient.getMonitorState();
    }
}
