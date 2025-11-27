package markets;

// marketplace facade coordinating buying and selling operations
// uses factory method (randomMarket()) for market generation
// delegates to MarketInventory for the actual transaction logic 

import inventory.items.Item;
import services.FileParserService;
import ui.GameUI;
import pieces.parties.Party;
import java.util.List;

public class Market {
    private MarketInventory inventory = new MarketInventory();

    public static Market randomMarket(FileParserService parser) {
        Market m = new Market();
        List<Item> sample = null;
        if (parser != null){
            sample = parser.sampleMarketStock(6);
        }
        m.inventory.seedDefault(sample);
        return m;
    }

    public void openForParty(Party p, java.util.Scanner scanner, GameUI ui) {
        for (pieces.creatures.Hero h : p.getHeroes()) {
            ui.println("Opening market for " + h.getName());
            inventory.openForHero(h, scanner, ui);
        }
    }
}
