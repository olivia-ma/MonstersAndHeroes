package markets;

import ui.GameUI;
import pieces.creatures.Hero;
import pieces.parties.Party; import inventory.items.Item; import java.util.*;
public class Market { private MarketInventory inventory = new MarketInventory(); public static Market
randomMarket(){ Market m=new Market(); m.inventory.seedDefault(); return m; } public void
openForParty(Party p, java.util.Scanner scanner, GameUI ui){ // open for each hero 
for (Hero h : p.getHeroes()) 
{ ui.println("Entering market with: "+ h.getName()); inventory.openForHero(h, scanner, ui); } } }

