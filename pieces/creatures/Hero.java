package pieces.creatures;

import inventory.Inventory;
import inventory.items.*;

public class Hero extends Creature {

    private double mp;
    private double strength, dexterity, agility;
    private int gold;
    private int exp;

    private Inventory inventory = new Inventory();
    private Weapon equippedWeapon = null;
    private Armor equippedArmor = null;

    public Hero(String name, int level, double mp, double str, double agi, double dex, int gold) {
        super(name, level);
        this.mp = mp;
        this.strength = str;
        this.agility = agi;
        this.dexterity = dex;
        this.gold = gold;
        this.exp = 0;
    }

    public static Hero createWarrior(String name) {
        return new Hero(name, 1, 200, 700, 600, 400, 1500);
    }

    public static Hero createSorcerer(String name) {
        return new Hero(name, 1, 800, 300, 400, 800, 1500);
    }

    public static Hero createPaladin(String name) {
        return new Hero(name, 1, 400, 600, 500, 600, 1500);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void equipWeapon(Weapon w) {
        if (w != null)
            equippedWeapon = w;
    }

    public void equipArmor(Armor a) {
        if (a != null)
            equippedArmor = a;
    }

    public double attackDamage() {
        double base = (strength + (equippedWeapon != null ?
                equippedWeapon.getDamage() : 50)) * 0.05;
        if (Math.random() < 0.1)
            base *= 2.0; // critical hit
        return Math.max(1, base);
    }

    public double spellDamage(Spell s) {
        double base = s.getDamage() + (dexterity / 10000.0) * s.getDamage();
        return Math.max(1, base);
    }

    public boolean isFainted() {
        return hp <= 0;
    }

    /* ===========================================
     * LEVEL-UP & EXP (OLD METHOD)
     * Still keeping this, but LevelService is now preferred.
     * =========================================== */
    public void addExp(int e) {
        exp += e;
        int needed = level * 10;

        while (exp >= needed) {
            exp -= needed;
            level++;

            strength *= 1.05;
            dexterity *= 1.05;
            agility *= 1.05;

            hp = level * 100;
            mp *= 1.1;

            needed = level * 10;
        }
    }

    /* ===========================================
     * REQUIRED GETTERS / SETTERS FOR LevelService
     * =========================================== */

    public int getLevel() {
        return level;
    }

    public void setLevel(int lvl) {
        this.level = lvl;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void addRawExp(int amount) {
        this.exp += amount;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double val) {
        this.strength = val;
    }

    public double getDexterity() {
        return dexterity;
    }

    public void setDexterity(double val) {
        this.dexterity = val;
    }

    public double getAgility() {
        return agility;
    }

    public void setAgility(double val) {
        this.agility = val;
    }

    public double getMp() {
        return mp;
    }

    public void setMp(double val) {
        this.mp = val;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double val) {
        this.hp = val;
    }

    public int getGold() {
        return gold;
    }

    public int spendGold(int price) {
        gold -= price;
        return gold;
    }

   
    public String statusLine() {
        return String.format(
            "%s L%d HP:%.0f MP:%.0f Str:%.0f Dex:%.0f Agi:%.0f Gold:%d",
            name, level, hp, mp, strength, dexterity, agility, gold
        );
    }
}
