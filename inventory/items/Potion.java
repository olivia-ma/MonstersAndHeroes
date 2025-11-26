package inventory.items;

public class Potion extends Item { private int amount; private String stat; public Potion(String name,int
price,int level,int amount,String stat){ super(name,price,level); this.amount=amount; this.stat=stat; } public
int getAmount(){ return amount; } public String getStat(){ return stat; } }

