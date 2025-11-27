package main;

// main game controller implementing facade pattern to simplify complex subsystems
// start() initalizes game
// mainLoop() handles input/movement/battles
// manageInventory() has equipment management
// basically just manages the entire game flow!!!

import board.Board;
import pieces.creatures.Hero;
import pieces.parties.Party;
import services.FileParserService;
import services.LevelService;
import ui.ConsoleUI;
import ui.GameUI;
import battle.Battle;
import pieces.creatures.Monster;
import inventory.items.*;

import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class Game {

    private GameUI ui = new ConsoleUI();
    private Scanner scanner = new Scanner(System.in);
    private Party party;
    private Board board;
    private FileParserService parser;
    private LevelService levelService = new LevelService();
    private Random rng = new Random();

    public void start() {
        ui.println("\n**********************************\r\n" + //
                        "*                                *\r\n" + //
                        "*  Legends: Monsters and Heroes  *\r\n" + //
                        "*                                *\r\n" + //
                        "**********************************\n");

        ui.println("A tactical RPG where heroes battle monsters, gain experience,\r\n" + //
                        "and buy gear in markets. Explore the grid world, fight random\r\n" + //
                        "encounters, and level up indefinitely!\r\n" + //
                        "\r\n" + //
                        "CONTROLS:\r\n" + //
                        "W/A/S/D - Move | I - Inventory | M - Enter Market\r\n" + //
                        "Q - Quit | R - Reset Map\n");
        
        parser = new FileParserService();
        setupParty(parser);

        int W = 8, H = 6;
        board = new Board(W, H, party, ui, parser);
        board.generate(true);

        mainLoop();
    }

    private void mainLoop() {
        boolean running = true;
        while (running) {
            ui.clearScreen();
            ui.printMap(board);
            ui.showPartyInfo(party);
            
            ui.println("Commands: W/A/S/D move, I inventory, R restart map, Q quit");
            String cmd = scanner.nextLine().trim();
            if (cmd.isEmpty()){
                continue;
            }
            char c = Character.toUpperCase(cmd.charAt(0));

            if (c == 'Q') { 
                ui.println("Quitting game."); 
                running = false; 
                break; 
            }

            if (c == 'I') {
                manageInventory();
                continue;
            }

            if (c == 'R') {
                ui.println("Restarting map...");
                board.generate(true);
                continue;
            }

            boolean moved = false;

            if (c == 'W' || c=='A' || c=='S' || c=='D') {
                moved = board.moveParty(c);
                if (!moved){
                    ui.println("Cannot move there.");
                }
            }

            // market only if we moved
            if (moved && board.isOnMarket()) {
                ui.println("You find a market here.");
                board.enterMarket(scanner, ui);
                continue;
            }

            // random encounter only if we moved
            if (moved && board.checkEncounter()) {
                ui.println("-- A wild group of monsters appears! --");
                List<Monster> monsters = spawnMonstersForParty();
                Battle battle = new Battle(party, monsters, ui, scanner, levelService);
                boolean won = battle.fight();
                if (!won) {
                    ui.println("Game over. All heroes have fallen.");
                    running = false;
                    break;
                } else {
                    ui.println("Battle won. Continuing...");
                }
            }
        }
    }

    private void manageInventory() {
        ui.println("--- Inventory Management ---");
        for (Hero h : party.getHeroes()) {
            ui.println("Hero: " + h.getName());
            ui.println("1) View Inventory 2) Equip 3) Use Potion 4) Skip");
            String s = scanner.nextLine().trim();
            if (s.isEmpty()){
                continue;
            }
            char c = s.charAt(0);

            if (c == '1') {
                List inventory = h.getInventory().getAll();
                ui.println("Items:");
                for (int i=0;i<inventory.size();i++){
                    ui.println((i+1)+") " + inventory.get(i).toString());
                }
            } else if (c == '2') {
                ui.println("Equip Menu:");
                ui.println("W) Weapons  A) Armors  L) Leave");
                String em = scanner.nextLine().trim().toUpperCase();

                if (em.equals("L")){
                    continue;
                }

                if (em.equals("W")) {
                    List<Weapon> available = h.getUnequippedWeapons();
                    
                    if (available.isEmpty()) { 
                        ui.println("No weapons available.");
                        continue; 
                    }

                    ui.println("Weapons:");
                    for (int i=0;i<available.size();i++){
                        ui.println((i+1)+") "+available.get(i));
                    }

                    ui.println("Enter <num> or <num>T for two-handed:");
                    String wchoice = scanner.nextLine().trim().toUpperCase();
                    if (wchoice.equals("L")){
                        continue;
                    }

                    boolean two = wchoice.endsWith("T");
                    String numPart = wchoice.replaceAll("[^0-9]", "");
                    int idx = safeParse(numPart,1) - 1;

                    if (idx < 0 || idx >= available.size()) { 
                        ui.println("Invalid."); 
                        continue; 
                    }

                    Weapon pick = available.get(idx);
                    boolean ok = h.equipWeapon(pick, two);

                    if (!ok) {
                        ui.println("Hands full. Replace Left (L) or Right (R)?");
                        String slot = scanner.nextLine().trim().toUpperCase();
                        h.equipWeaponIntoSlot(pick, slot.equals("L"));
                    }

                    ui.println("Equipped " + pick.getName());
                }

                else if (em.equals("A")) {
                    List<Armor> available = h.getUnequippedArmors();
                    if (available.isEmpty()) { 
                        ui.println("No armors."); continue; 
                    }

                    ui.println("Armors:");
                    for (int i = 0; i < available.size(); i++){
                        ui.println((i+1) + ") " + available.get(i));
                    }

                    String achoice = scanner.nextLine().trim();
                    int idx = safeParse(achoice,1) - 1;
                    if (idx < 0 || idx >= available.size()) {
                        ui.println("Invalid."); 
                        continue; 
                    }

                    Armor pick = available.get(idx);
                    h.equipArmor(pick);
                    ui.println("Equipped " + pick.getName());
                }

            } else if (c == '3') {
                List<inventory.items.Potion> pots = h.getInventory().findAllOfType(inventory.items.Potion.class);
                
                if (pots.isEmpty()) {
                    ui.println("No potions.");
                    continue; 
                }

                ui.println("Potions:");
                for (int i = 0; i < pots.size(); i++){
                    ui.println((i + 1) + ") "+pots.get(i).toString());
                }

                ui.println("Pick potion number (or enter):");
                String pick = scanner.nextLine().trim(); if (pick.isEmpty()) continue;
                int idx = -1;
                try { 
                    idx = Integer.parseInt(pick) - 1; 
                } catch(Exception ex) {
                    continue; 
                }

                if (idx >= 0 && idx < pots.size()) {
                    inventory.items.Potion pot = pots.get(idx);
                    for (String attr : pot.getAffectedStats()) {
                        String a = attr.toUpperCase();
                        if (a.startsWith("HP")){
                            h.setHp(h.getHp()+pot.getIncreaseAmount());
                        }
                        else if (a.startsWith("MP")){
                            h.setMp(h.getMp()+pot.getIncreaseAmount());
                        }
                        else if (a.startsWith("STR")){
                            h.setStrength(h.getStrength()+pot.getIncreaseAmount());
                        }
                        else if (a.startsWith("DEX")){
                            h.setDexterity(h.getDexterity()+pot.getIncreaseAmount());
                        }
                        else if (a.startsWith("AGI")){ 
                            h.setAgility(h.getAgility()+pot.getIncreaseAmount());
                        }
                    }

                    h.getInventory().removeItem(pot);
                    ui.println("Used potion: "+pot.getName());
                }
            }
        }
    }

    private List<Monster> spawnMonstersForParty() {
        List<Monster> res = new java.util.ArrayList<Monster>();
        int count = Math.max(1, party.size());
        List<Monster> pool = new java.util.ArrayList<Monster>();
        pool.addAll(parser.getDragons());
        pool.addAll(parser.getSpirits());
        pool.addAll(parser.getExoskeletons());
        if (pool.isEmpty()) {
            res.add(new Monster("Dragon DN", pieces.creatures.MonsterType.DRAGON, 1, 100, 10, 5.0));
            return res;
        }

        for (int i = 0; i < count; i++) {
            Monster m = pool.get(rng.nextInt(pool.size()));
            res.add(new Monster(m.getName()+"#"+rng.nextInt(1000), m.getType(), Math.max(1, m.getLevel()), m.getDamage(), m.getDefense(), m.getDodge()));
        }

        int targetLevel = computeMaxLevel();
        levelService.scaleMonsters(res, targetLevel);
        // ensure hp and stats set (level -> hp = level*100)
        for (Monster m : res) {
            m.setLevel(targetLevel);
            m.setHp(targetLevel * 100.0);
            // clamp dodge so not > 90% nor < 0
            if (m.getDodge() < 0){
                m.setDodge(0.0);
            }
            if (m.getDodge() > 90.0){
                m.setDodge(90.0);
            }
        }
        return res;
    }

    private int computeMaxLevel() {
        int mx = 1;
        for (Hero h : party.getHeroes()){
            if (h.getLevel() > mx){
                mx = h.getLevel();
            }
        }

        return Math.max(1, mx);
    }

    private void setupParty(FileParserService parser) {
        ui.println("How many heroes in party? (1-3) [default 2]: ");
        int n = 2;
        try {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) n = Integer.parseInt(line);
        } catch(Exception e) {
            n = 2; 
        }

        n = Math.max(1, Math.min(3, n));
        party = new Party(n);
        ui.println("Creating " + n + " heroes. Choose types and names:");

        for (int i = 0; i < n; i++) {
            int classChoice = 1;
            while (true) {
                ui.println(String.format("Hero %d: pick class: 1)Warrior 2)Sorcerer 3)Paladin [default 1]: ", i+1));
                String in = scanner.nextLine().trim();
                if (in.isEmpty()) {
                    classChoice = 1; break; 
                }
                if (in.equalsIgnoreCase("1") || in.equalsIgnoreCase("W") ) { 
                    classChoice = 1; 
                    break; 
                }
                if (in.equalsIgnoreCase("2") || in.equalsIgnoreCase("S") ) { 
                    classChoice = 2; 
                    break; 
                }
                if (in.equalsIgnoreCase("3") || in.equalsIgnoreCase("P") ) {
                    classChoice = 3; 
                    break; 
                }

                ui.println("Invalid selection. Type 1,2,3 or W,S,P.");
            }

            List<Hero> prototypes;
            String kind;
            switch (classChoice) {
                case 2: 
                    prototypes = parser.getSorcerers(); 
                    kind = "Sorcerer"; 
                    break;
                case 3: 
                    prototypes = parser.getPaladins(); 
                    kind = "Paladin"; 
                    break;
                default: 
                    prototypes = parser.getWarriors(); 
                    kind = "Warrior"; 
                    break;
            }

            ui.println("Choose a " + kind + " from the list below (type number or letter):");
            for (int j = 0; j < prototypes.size(); j++) {
                Hero p = prototypes.get(j);
                char letter = (char)('A' + j);
                ui.println(String.format("%c) %s (Lvl %d HP:%.0f MP:%.0f) ", letter, p.getName(), p.getLevel(), p.getHp(), p.getMp()));
            }

            int pickedIndex = 0;
            while (true) {
                String choice = scanner.nextLine().trim();
                if (choice.isEmpty()) { 
                    pickedIndex = 0; 
                    break; 
                }
                if (choice.length() == 1 && Character.isLetter(choice.charAt(0))) {
                    char ch = Character.toUpperCase(choice.charAt(0));
                    int idx = ch - 'A';
                    if (idx >= 0 && idx < prototypes.size()) { 
                        pickedIndex = idx; 
                        break; 
                    }
                }
                try {
                    int num = Integer.parseInt(choice) - 1;
                    if (num >= 0 && num < prototypes.size()) { 
                        pickedIndex = num; 
                        break; 
                    }
                } catch (Exception ex) {}
                ui.println("Invalid choice. Enter a letter (A,B,...) or number.");
            }

            Hero proto = prototypes.get(pickedIndex);
            ui.println("Enter name [default " + proto.getName() + "]: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = proto.getName();
            Hero h = Hero.copyOf(proto, name);

            inventory.items.Weapon w = parser.getDefaultWeaponOrNull();
            inventory.items.Armor a = parser.getDefaultArmorOrNull();
            if (w != null) { 
                h.getInventory().addItem(w); 
                h.equipWeapon(w, false); 
            }  
            if (a != null) { 
                h.getInventory().addItem(a); 
                h.equipArmor(a);
             }

            // ensure level 1 and caps
            h.setLevel(1);
            h.setHp(1 * 100.0);
            h.setMp(Math.min(h.getMp(), h.getLevel() * 100.0));

            party.addHero(h);
        }
    }

    private int safeParse(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }
    
    public static void main(String[] args) {
        new Game().start();
    }
}
