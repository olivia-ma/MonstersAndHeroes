package inventory.items;

// item subclass, has stat modification effects

import java.util.List;

public class Potion extends Item {

    private int increaseAmount;
    private List<String> affectedStats;

    public Potion(String name, int cost, int requiredLevel, int increaseAmount, List<String> affectedStats) {
        super(name, cost, requiredLevel);
        this.increaseAmount = increaseAmount;
        this.affectedStats = affectedStats;
    }

    public int getIncreaseAmount() {
        return increaseAmount;
    }

    public List<String> getAffectedStats() {
        return affectedStats;
    }

    @Override
    public String toString() {
        return name + " (Potion +" + increaseAmount + " to " + affectedStats + ")";
    }
}
