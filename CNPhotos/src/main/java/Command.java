public abstract class Command {

    String commandTitle;

    public Command(){

    }

    public abstract void execute();

    public void setCommandTitle(String title){
        commandTitle = title;
    }
}
