package pieces.creatures;

public class Monster extends Creature { private MonsterType type; private double damage; private double
defense; private double dodgeChance; public Monster(String name, MonsterType t, int level, double
damage, double defense, double dodge) { super(name, level); this.type=t; this.damage=damage;
this.defense=defense; this.dodgeChance=dodge; this.hp = level*100; } public double getDamage(){ return
damage; } public double getDefense(){ return defense; } public double getDodge(){ return dodgeChance; } }
