public abstract class Command {

    public final String commandTitle;

    public Command(String commandTitle){
        this.commandTitle = commandTitle;
    }

    public abstract void execute();

}
