package wbe.laboursOfHercules.labours;

public class Reward {

    private int weight;

    private String command;

    public Reward(int weight, String command) {
        this.weight = weight;
        this.command = command;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
