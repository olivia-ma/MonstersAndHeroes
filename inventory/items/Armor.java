package inventory.items;

public class Armor extends Item { private int reduction; public Armor(String name,int price,int level,int
reduction){ super(name,price,level); this.reduction=reduction; } public int getReduction(){ return
reduction; } }

