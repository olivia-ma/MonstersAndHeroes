package inventory.items;

// item subclass, has mana based combat with secondary effects

public class Spell extends Item {

    public enum SpellType { FIRE, ICE, LIGHTNING }

    private int damage;
    private int manaCost;
    private SpellType type;

    public Spell(String name, int cost, int requiredLevel, int damage, int manaCost, SpellType type) {
        super(name, cost, requiredLevel);
        this.damage = damage;
        this.manaCost = manaCost;
        this.type = type;
    }

    public int getDamage() {
        return damage;
    }

    public int getManaCost() {
        return manaCost;
    }

    public SpellType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " (" + type + ", DMG " + damage + ", MP " + manaCost + ")";
    }
}
