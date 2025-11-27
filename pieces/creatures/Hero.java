package pieces.creatures;

// player character with complex equipment and inventory management
// implement strategy pattern through hero types with different stat growth
// equipWeapon() handles two handed weapon logic
// attackDamage() calculates combat output
// addExp() manages level progression with stat scaling

import inventory.Inventory;
import inventory.items.Armor;
import inventory.items.Item;
import inventory.items.Potion;
import inventory.items.Spell;
import inventory.items.Weapon;

import java.util.List;

public class Hero extends Creature {

    private double mp;
    private double strength;
    private double dexterity;
    private double agility;

    private int gold;
    private int exp;

    private Inventory inventory = new Inventory();

    // equipment slots
    private Weapon rightHand = null;
    private Weapon leftHand = null;
    private Armor equippedArmor = null;

    public Hero(String name, int level, double mp, double strength, double agility, double dexterity, int gold) {
        super(name, Math.max(1, level));
        this.mp = mp;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.gold = gold;
        this.exp = 0;
        this.hp = this.level * 100.0;
    }

    public static Hero copyOf(Hero proto, String name) {
        Hero h = new Hero(name, proto.getLevel(), proto.getMp(), proto.getStrength(), proto.getAgility(), proto.getDexterity(), proto.getGold());
        for (inventory.items.Item it : proto.inventory.getAll()) {
            h.inventory.addItem(it);
        }
        h.setHp(proto.getHp());
        h.setExp(proto.getExp());
        return h;
    }

    public Inventory getInventory() { return inventory; }

    // equip logic 
    public List<Weapon> getUnequippedWeapons() {
        return inventory.findAllOfType(Weapon.class).stream()
            .filter(w -> w != rightHand && w != leftHand)
            .collect(java.util.stream.Collectors.toList());
    }

    public List<Armor> getUnequippedArmors() {
        return inventory.findAllOfType(Armor.class).stream()
            .filter(a -> a != equippedArmor)
            .collect(java.util.stream.Collectors.toList());
    }

    public boolean equipWeapon(Weapon w, boolean twoHanded) {
        if (w == null){
            return false;
        }

        // cannot equip if already equipped
        if (w == rightHand || w == leftHand){
            return false;
        }

        inventory.removeItem(w);

        // two-handed weapon or forced two-hand
        if (twoHanded || w.getHandsRequired() == 2) {
            if (rightHand != null){
                inventory.addItem(rightHand);
            }

            if (leftHand != null && leftHand != rightHand){
                inventory.addItem(leftHand);
            }

            rightHand = w;
            leftHand = w;
            return true;
        }

        // one-handed weapon
        if (rightHand == null || (rightHand.getHandsRequired() == 2)) {
            if (rightHand != null){
                inventory.addItem(rightHand);
            }
            rightHand = w;
            return true;
        }

        if (leftHand == null || (leftHand.getHandsRequired() == 2)) {
            if (leftHand != null){
                inventory.addItem(leftHand);
            }
            leftHand = w;
            return true;
        }

        // both hands full, ask left or right
        return false;
    }

    public boolean equipWeaponIntoSlot(Weapon w, boolean left) {
        if (left) {
            if (leftHand != null){
                inventory.addItem(leftHand);
            }
            leftHand = w;
        } else {
            if (rightHand != null){
                inventory.addItem(rightHand);
            }
            rightHand = w;
        }

        inventory.removeItem(w);
        return true;
    }

    public void equipArmor(Armor a) {
        if (a == null){
            return;
        }
        if (a == equippedArmor){
            return;
        }

        inventory.removeItem(a);

        if (equippedArmor != null){
            inventory.addItem(equippedArmor);
        }

        equippedArmor = a;
    }

    public Weapon getRightHand() { 
        return rightHand; 
    }

    public Weapon getLeftHand() { 
        return leftHand; 
    }

    public Armor getEquippedArmor() { 
        return equippedArmor; 
    }

    private boolean isDualGrip() {
        return rightHand != null && leftHand != null && rightHand == leftHand && rightHand.getHandsRequired() == 1;
    }

    // stats
    public double getMp() { 
        return mp; 
    }

    public void setMp(double mp) {
        double max = level * 100.0;
        this.mp = Math.max(0, Math.min(mp, max));
    }

    public double getStrength() { 
        return strength; 
    }

    public double getDexterity() { 
        return dexterity; 
    }

    public double getAgility() { 
        return agility; 
    }

    public int getGold() { 
        return gold; 
    }

    public int getExp() {
        return exp;
    }

    public void setStrength(double strength) { 
        this.strength = strength; 
    }

    public void setDexterity(double dexterity) { 
        this.dexterity = dexterity; 
    }

    public void setAgility(double agility) { 
        this.agility = agility; 
    }

    public void setGold(int gold) {
        this.gold = gold; 
    }

    public void setExp(int exp) { 
        this.exp = Math.max(0, exp); 
    }

    @Override
    public void setLevel(int lvl) {
        super.setLevel(lvl);
        this.hp = level * 100.0;
        this.mp = Math.min(mp, level * 100.0);
    }

    public void setHp(double hp) {
        double max = level * 100.0;
        this.hp = Math.max(0, Math.min(hp, max));
    }

    public int spendGold(int price) { 
        this.gold -= price; 
        return this.gold; 
    }

    public int getEquippedArmorReduction() {
        if (equippedArmor == null){
            return 0;
        }

        return equippedArmor.getDamageReduction();
    }

    // damage
    public double attackDamage() {
        double weaponDamage = 0.0;
        if (rightHand != null){
            weaponDamage += rightHand.getDamage();
        }

        if (leftHand != null && leftHand != rightHand){
            weaponDamage += leftHand.getDamage();
        }

        boolean dual = isDualGrip();
        double base = (strength + weaponDamage) * 0.05;
        if (dual){
            base *= 1.25;
        } 

        if (Math.random() < 0.10){
            base *= 2.0;
        }

        return Math.max(1.0, base);
    }

    public String statusLine() {
        String wname = (rightHand != null) ? rightHand.getName() : "Unarmed";
        String aname = (equippedArmor != null) ? equippedArmor.getName() : "NoArmor";
        return String.format("%s L%d HP:%.0f MP:%.0f Str:%.0f Dex:%.0f Agi:%.0f Gold:%d Weapon:%s Armor:%s",
            name, level, hp, mp, strength, dexterity, agility, gold, wname, aname);
    }

    public void addExp(int amount) { 
        this.exp += amount; 
        while (this.exp >= this.level * 10) { 
            this.exp -= this.level * 10; 
            this.level++; // stat increases 
            this.strength *= 1.05; 
            this.dexterity *= 1.05; 
            this.agility *= 1.05; // hp, mp adjustments 
            this.hp = this.level * 100.0; 
            this.mp = this.mp * 1.1; 
        } 
    }

    public boolean ownsItem(Item item) {
        List<Item> items = this.inventory.getAll();

        for (Item it : items) {
            if (it == item){
                return true;  // exact reference
            }

            if (it.getName().equalsIgnoreCase(item.getName())){
                // same name = same item type
                return true;             
            }
        }

        // also check equipped weapon(s) and armor
        if (rightHand != null && rightHand.getName().equalsIgnoreCase(item.getName())){
            return true;
        }
            
        if (leftHand != null && leftHand.getName().equalsIgnoreCase(item.getName())){
            return true;
        }

        if (equippedArmor != null && equippedArmor.getName().equalsIgnoreCase(item.getName())){
            return true;
        }
        
        return false;
    }
}
