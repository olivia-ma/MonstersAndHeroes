package inventory.items;

// item subclass, has damage calculation and hand requirements

public class Weapon extends Item {

    private int damage;
    private int handsRequired;

    public Weapon(String name, int cost, int requiredLevel, int damage, int hands) {
        super(name, cost, requiredLevel);
        this.damage = damage;
        this.handsRequired = Math.max(1, hands);
    }

    public int getDamage() {
        return damage; 
    }

    public int getHandsRequired() {
        return handsRequired;
    }

    @Override
    public String toString() {
        return name + " (DMG " + damage + ", Hands " + handsRequired + ", lvl " + requiredLevel + ")";
    }
}
