package markets;

import inventory.items.*;
import ui.GameUI;
import pieces.creatures.Hero;
import java.util.*;

public class MarketInventory {
    private List<Item> stock = new ArrayList<>();

    public void seedDefault() {
        stock.add(new Weapon("Sword", 500, 1, 300, 1));
        stock.add(new Armor("Shield", 300, 1, 80));
        stock.add(new Potion("Healing", 200, 1, 100, "health"));
        stock.add(new Spell("Spark", 400, 1, 200, 100, Spell.SpellType.LIGHTNING));
    }

    public void openForHero(Hero h, java.util.Scanner scanner, GameUI ui) {
        boolean done = false;
        while (!done) {
            // FIX: Access the hero's gold using a method like getGold() or similar
            // Assuming h.getGold() exists based on typical class structure
            ui.println("You have " + h.getGold() + " gold. Market stock:");

            for (int i = 0; i < stock.size(); i++) {
                ui.println((i + 1) + ") " + stock.get(i).desc());
            }
            ui.println("Options: B<num> buy, S<num> sell, L leave");

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            char c = Character.toUpperCase(line.charAt(0));

            if (c == 'L') {
                done = true;
                break;
            } else if (c == 'B') {
                try {
                    int idx = Integer.parseInt(line.substring(1).trim()) - 1;
                    if (idx < 0 || idx >= stock.size()) {
                        ui.println("Invalid item number.");
                        continue;
                    }
                    Item it = stock.get(idx);
                    
                    // FIX: Ensure the hero has enough gold before buying
                    if (h.getGold() >= it.getPrice()) {
                        // Assuming h.spendGold(amount) and h.getInventory().addItem(it) exist
                        h.spendGold(it.getPrice()); 
                        h.getInventory().addItem(it);
                        ui.println("Bought " + it.getName() + " for " + it.getPrice() + " gold.");
                        stock.remove(idx);
                    } else {
                        ui.println("You do not have enough gold to buy " + it.getName() + ".");
                    }

                } catch (NumberFormatException ex) { // Catch more specific exception
                    ui.println("Buy syntax error. Use B<num>.");
                } catch (Exception ex) {
                    ui.println("An error occurred during purchase: " + ex.getMessage());
                }
            } else if (c == 'S') {
                ui.println("Sell not implemented in MarketInventory demo.");
            } else {
                ui.println("Invalid option.");
            }
        }
    }
}
