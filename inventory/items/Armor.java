package inventory.items;

// item subclass, has damage reduction

public class Armor extends Item {

    private int damageReduction;

    public Armor(String name, int cost, int requiredLevel, int reduction) {
        super(name, cost, requiredLevel);
        this.damageReduction = reduction;
    }

    public int getDamageReduction() {
        return damageReduction;
    }

    @Override
    public String toString() {
        return name + " (Armor -" + damageReduction + ", lvl " + requiredLevel + ")";
    }
}
