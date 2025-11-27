package markets;

// implements market operations with strategy pattern for transactions
// openForHero() handles buy and sell flow, manages stock rotation, and validates purchase conditions (level, gold, ownership)

import inventory.items.*;
import ui.GameUI;
import pieces.creatures.Hero;

import java.util.ArrayList;
import java.util.List;

public class MarketInventory {

    private List<Item> stock = new ArrayList<Item>();

    public MarketInventory() { }

    public void seedDefault(List<Item> sample) {
        stock.clear();
        if (sample != null && !sample.isEmpty()) {
            stock.addAll(sample);
        } else {
            stock.add(new Weapon("Sword", 500, 1, 300, 1));
            stock.add(new Armor("Shield", 300, 1, 80));
            List<String> s = new ArrayList<String>(); s.add("HP");
            stock.add(new Potion("SmallHealing", 200, 1, 100, s));
            stock.add(new Spell("Spark", 400, 1, 200, 100, Spell.SpellType.LIGHTNING));
        }
    }

    public List<Item> getStock() { 
        return stock; 
    }

    public void openForHero(Hero h, java.util.Scanner scanner, GameUI ui) {
        boolean done = false;
        while (!done) {
            ui.println("\n--- Market ---");
            ui.println(h.getName() + " gold: " + h.getGold());

            ui.println("Market stock:");
            for (int i = 0; i < stock.size(); i++) {
                Item it = stock.get(i);
                ui.println((i + 1) + ") " + it.toString() + " Sell:" + it.getSellValue());
            }
            ui.println("Options: B<num> buy, S sell, V view inventory, L leave");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()){
                continue;
            }
            char c = Character.toUpperCase(line.charAt(0));

            try {
                if (c == 'L') {
                    done = true;
                } else if (c == 'B') {
                    int idx = Integer.parseInt(line.substring(1).trim()) - 1;
                    if (idx < 0 || idx >= stock.size()) { 
                        ui.println("Invalid item number."); 
                        continue; 
                    }
                    Item it = stock.get(idx);
                    if (h.ownsItem(it)) {
                        ui.println("You already own " + it.getName() + ". Cannot buy another one.");
                        continue;
                    }
                    if (h.getLevel() < it.getRequiredLevel()) { 
                        ui.println("Level too low to buy."); 
                        continue; 
                    }
                    if (h.getGold() >= it.getCost()) {
                        h.spendGold(it.getCost());
                        h.getInventory().addItem(it);
                        ui.println("Bought " + it.getName() + " for " + it.getCost() + " gold.");
                        stock.remove(idx);
                    } else {
                        ui.println("You do not have enough gold.");
                    }
                } else if (c == 'S') {
                    List<inventory.items.Item> inv = h.getInventory().getAll();

                    if (inv.isEmpty()) { 
                        ui.println("Your inventory is empty."); 
                        continue; 
                    }

                    ui.println("Your Inventory:");
                    for (int i = 0; i < inv.size(); i++){
                        ui.println((i + 1) + ") " + inv.get(i).toString() + " sell:" + inv.get(i).getSellValue());
                    }
                    ui.println("Enter item number to sell or L to cancel:");
                    String s2 = scanner.nextLine().trim();
                    if (s2.isEmpty()){
                        continue;
                    }
                    if (Character.toUpperCase(s2.charAt(0)) == 'L'){
                        continue;
                    }

                    int sellIdx = Integer.parseInt(s2) - 1;
                    if (sellIdx < 0 || sellIdx >= inv.size()) { 
                        ui.println("Invalid selection."); 
                        continue; 
                    }
                    Item sellItem = inv.get(sellIdx);
                    int sellPrice = Math.max(1, sellItem.getSellValue());
                    h.getInventory().removeItem(sellItem);
                    stock.add(sellItem);
                    h.spendGold(-sellPrice);
                    ui.println("Sold " + sellItem.getName() + " for " + sellPrice + " gold.");
                } else if (c == 'V') {
                    ui.println("--- " + h.getName() + " Inventory ---");

                    // equipped weapons
                    Weapon rh = h.getRightHand();
                    Weapon lh = h.getLeftHand();
                    Armor ar = h.getEquippedArmor();

                    if (rh != null && rh == lh) {
                        ui.println("Equipped Weapon: " + rh.getName() + " (DMG " + rh.getDamage() + ", Two-Handed)");
                    } else {
                        if (rh != null)
                            ui.println("Right Hand: " + rh.getName() + " (DMG " + rh.getDamage() + ", Hands " + rh.getHandsRequired() + ")");
                        else
                            ui.println("Right Hand: (empty)");

                        if (lh != null)
                            ui.println("Left Hand: " + lh.getName() + " (DMG " + lh.getDamage() + ", Hands " + lh.getHandsRequired() + ")");
                        else
                            ui.println("Left Hand: (empty)");
                    }

                    // equipped armor
                    if (ar != null)
                        ui.println("Equipped Armor: " + ar.getName() + " (Damage Reduction " + ar.getDamageReduction() + ")");
                    else
                        ui.println("Equipped Armor: (none)");

                    ui.println("");

                    // Inventory items
                    List items = h.getInventory().getAll();
                    if (items.isEmpty()) {
                        ui.println("Backpack: (empty)");
                    } else {
                        ui.println("Backpack items:");
                        for (int i = 0; i < items.size(); i++) {
                            ui.println((i+1) + ") " + items.get(i).toString());
                        }
                    }
                }
            } catch (NumberFormatException nfe) {
                ui.println("Number format error. Use the menu commands (B<num>, S, V, L).");
            } catch (Exception ex) {
                ui.println("Error: " + ex.getMessage());
            }
        }
    }
}
