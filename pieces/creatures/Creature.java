package pieces.creatures;

public abstract class Creature { protected String name; protected int level; public double hp; 
public Creature(String name, int level) { this.name=name; this.level=level;
this.hp = level*100; }
public boolean isFainted(){ return hp <= 0; }
public void takeDamage(double d){ hp -= d; }
public String getName(){ return name; }
public int getLevel(){ return level; }
}
