package pieces.creatures;

// handles all monster types using composition over inheritance
// uses type field to differentiate behaviors rather than subclass hierachy
// attack() implements monster combat with dodge and critical mechanics

public class Monster extends Creature {

    private MonsterType type;
    private double damage;
    private double defense;
    private double dodge; // percent 0..100

    public Monster(String name, MonsterType type, int level, double damage, double defense, double dodge) {
        super(name, Math.max(1, level));
        this.type = type;
        this.damage = damage;
        this.defense = defense;
        this.dodge = dodge;
        this.hp = this.level * 100.0;
    }

    public MonsterType getType() { 
        return type; 
    }

    public double getDamage() { 
        return damage; 
    }

    public void setDamage(double damage) { 
        this.damage = damage; 
    }

    public double getDefense() { 
        return defense; 
    }

    public void setDefense(double defense) { 
        this.defense = defense; 
    }

    public double getDodge() { 
        return dodge; 
    }

    public void setDodge(double dodge) { 
        this.dodge = dodge; 
    }

    public void setHp(double hp) { 
        this.hp = hp; 
    }

    @Override
    public void setLevel(int lvl) {
        super.setLevel(lvl);
        this.hp = this.level * 100.0;
    }

    public boolean isFainted() { 
        return hp <= 0.0; 
    }

    public void takeDamage(double d) {
        this.hp -= d;
    }

    // monster attacks hero with proper formula 
    public int attack(Hero target) {
        // hero dodge check
        double heroDodge = target.getAgility()*0.002; 
        if (Math.random() < heroDodge) {
            return -1; // dodged
        }

        double reduction = 100.0 / (100.0 + target.getEquippedArmorReduction());

        double dmg = this.damage * reduction;

        // 20% crit chance
        if (Math.random() < 0.20) {
            dmg *= 2.0;
            target.takeDamage(dmg);
            return (int)Math.round(dmg) * 1000; // special marker for crit
        }

        if (dmg < 1){
            dmg = 1;
        }
        target.takeDamage(dmg);
        // System.out.println(dmg);
        return (int)Math.round(dmg);
    }
}
