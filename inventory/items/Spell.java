package inventory.items;

public class Spell extends Item { public enum SpellType{ FIRE, ICE, LIGHTNING } private int damage, cost;
private SpellType type; public Spell(String name,int price,int level,int damage,int cost,SpellType type)
{ super(name,price,level); this.damage=damage; this.cost=cost; this.type=type; } public int getDamage()
{ return damage; } public int getCost(){ return cost; } public SpellType getSpellType(){ return type; } }
