package pieces.creatures;

// abstract base class for all living entities using template method pattern
// define common attributes (name, level, hp) and behaviors (takeDamage, isFainted)
// promotes code reuse across hero and monster types

public abstract class Creature {
    protected String name;
    protected int level;
    protected double hp;

    public Creature(String name, int level) {
        this.name = name;
        this.level = Math.max(1, level);
        this.hp = this.level * 100.0;
    }

    public String getName() { 
        return name; 
    }

    public int getLevel() {
        return level; 
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
        if (this.hp < this.level * 100.0) { 
            this.hp = this.level * 100.0; 
        }
    }

    public double getHp() { 
        return hp; 
    }

    public void setHp(double hp) { 
        this.hp = hp; 
    }

    public boolean isFainted() { 
        return hp <= 0.0; 
    }

    public void takeDamage(double d) { 
        this.hp -= d; 
    }
}
