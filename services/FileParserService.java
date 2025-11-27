package services;

// data access layer implementing repository pattern for game data loading
// loadAll() parses text files into game objects, sampleMarketStock() provides random item selection
// handles malformed data with default fallbacks

import inventory.items.*;
import pieces.creatures.*;

import java.io.*;
import java.util.*;

public class FileParserService {

    private static final String DATA_DIR = "data/";

    private List<Weapon> weapons = new ArrayList<>();
    private List<Armor> armors = new ArrayList<>();
    private List<Potion> potions = new ArrayList<>();
    private List<Spell> spells = new ArrayList<>();

    private List<Hero> warriors = new ArrayList<>();
    private List<Hero> sorcerers = new ArrayList<>();
    private List<Hero> paladins = new ArrayList<>();

    private List<Monster> dragons = new ArrayList<>();
    private List<Monster> spirits = new ArrayList<>();
    private List<Monster> exoskeletons = new ArrayList<>();

    public FileParserService() {
        loadAll();
    }

    private void loadAll() {
        loadWeapons();
        loadArmors();
        loadPotions();
        loadSpells();

        loadWarriors();
        loadSorcerers();
        loadPaladins();

        loadDragons();
        loadSpirits();
        loadExoskeletons();
    }

    // file loader
    private List<String[]> loadFile(String filename, int minFields) {
        List<String[]> rows = new ArrayList<>();
        File f = new File(DATA_DIR + filename);
        if (!f.exists()) {
            System.out.println("WARNING: " + filename + " missing. Using defaults.");
            return rows;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine();
            if (line == null){
                return rows;
            }
            // treat first line as header only if it contains "name" or "cost" keywords
            if (!looksLikeHeader(line)) processLine(rows, line, minFields);
            while ((line = br.readLine()) != null) processLine(rows, line, minFields);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rows;
    }

    private boolean looksLikeHeader(String line) {
        String t = line.toLowerCase();
        return t.contains("name") || t.contains("cost") || t.contains("level");
    }

    private void processLine(List<String[]> rows, String line, int minFields) {
        if (line == null){
            return;
        }
        line = line.trim();
        if (line.isEmpty()){
            return;
        }
        String[] parts = line.split("\\s+");
        if (parts.length >= minFields){
            rows.add(parts);
        }
    }

    // items
    private void loadWeapons() {
        List<String[]> rows = loadFile("Weaponry.txt", 5);
        for (String[] r : rows) {
            try {
                String name = r[0];
                int cost = Integer.parseInt(r[1]);
                int lvl = Integer.parseInt(r[2]);
                int dmg = Integer.parseInt(r[3]);
                int hands = Integer.parseInt(r[4]);
                weapons.add(new Weapon(name, cost, lvl, dmg, hands));
            } catch (Exception ex) {
                System.out.println("Skipping malformed weapon row: " + Arrays.toString(r));
            }
        }
        if (weapons.isEmpty()) {
            weapons.add(new Weapon("Sword", 500, 1, 300, 1));
            weapons.add(new Weapon("Bow", 300, 2, 500, 2));
        }
    }

    private void loadArmors() {
        List<String[]> rows = loadFile("Armory.txt", 4);
        for (String[] r : rows) {
            try {
                String name = r[0];
                int cost = Integer.parseInt(r[1]);
                int lvl = Integer.parseInt(r[2]);
                int red = Integer.parseInt(r[3]);
                armors.add(new Armor(name, cost, lvl, red));
            } catch (Exception ex) {
                System.out.println("Skipping malformed armor row: " + Arrays.toString(r));
            }
        }
        if (armors.isEmpty()) armors.add(new Armor("Leather Armor",100,1,50));
    }

    private void loadPotions() {
        List<String[]> rows = loadFile("Potions.txt", 5);
        for (String[] r : rows) {
            try {
                String name = r[0];
                int cost = Integer.parseInt(r[1]);
                int lvl = Integer.parseInt(r[2]);
                int inc = Integer.parseInt(r[3]);
                String[] attrs = r[4].split("/");
                potions.add(new Potion(name, cost, lvl, inc, Arrays.asList(attrs)));
            } catch (Exception ex) {
                System.out.println("Skipping malformed potion row: " + Arrays.toString(r));
            }
        }
        if (potions.isEmpty()) {
            List<String> a = new ArrayList<>(); a.add("HP");
            potions.add(new Potion("SmallHeal", 150, 1, 150, a));
        }
    }

    private void loadSpells() {
        loadSpellFile("FireSpells.txt", Spell.SpellType.FIRE);
        loadSpellFile("IceSpells.txt", Spell.SpellType.ICE);
        loadSpellFile("LightningSpells.txt", Spell.SpellType.LIGHTNING);
    }

    private void loadSpellFile(String filename, Spell.SpellType type) {
        List<String[]> rows = loadFile(filename, 5);
        for (String[] r : rows) {
            try {
                String name = r[0];
                int cost = Integer.parseInt(r[1]);
                int lvl = Integer.parseInt(r[2]);
                int dmg = Integer.parseInt(r[3]);
                int mp = Integer.parseInt(r[4]);
                spells.add(new Spell(name, cost, lvl, dmg, mp, type));
            } catch (Exception ex) {
                System.out.println("Skipping malformed spell row: " + Arrays.toString(r));
            }
        }
        if (spells.isEmpty()) spells.add(new Spell("Spark", 200,1,150,50, Spell.SpellType.LIGHTNING));
    }

    // heroes
    private void loadWarriors() {
        List<String[]> rows = loadFile("Warriors.txt", 6);
        for (String[] r : rows) {
            try {
                String name = r[0];
                int mp = Integer.parseInt(r[1]);
                int str = Integer.parseInt(r[2]);
                int agi = Integer.parseInt(r[3]);
                int dex = Integer.parseInt(r[4]);
                int startLevel = 1;
                int gold = Integer.parseInt(r[5]);
                int exp = Integer.parseInt(r[6]);
                
                Hero h = new Hero(name, startLevel, mp, str, agi, dex, gold);
                h.setExp(exp);
                h.setHp(startLevel * 100.0);
                warriors.add(h);
            } catch (Exception ex) {
                System.out.println("Skipping malformed warrior row: " + Arrays.toString(r));
            }
        }
        if (warriors.isEmpty()){
            warriors.add(new Hero("DefaultWarrior",5,300,700,600,400,1500));
        }
    }

    private void loadSorcerers() {
        List<String[]> rows = loadFile("Sorcerers.txt", 6);
        for (String[] r : rows) {
            try {
                String name = r[0];
                int mp = Integer.parseInt(r[1]);
                int str = Integer.parseInt(r[2]);
                int agi = Integer.parseInt(r[3]);
                int dex = Integer.parseInt(r[4]);
                int startLevel = 1;
                int gold = Integer.parseInt(r[5]);
                int exp = Integer.parseInt(r[6]);
                
                Hero h = new Hero(name, startLevel, mp, str, agi, dex, gold);
                h.setExp(exp);
                h.setHp(startLevel * 100.0);
                sorcerers.add(h);
            } catch (Exception ex) {
                System.out.println("Skipping malformed sorcerer row: " + Arrays.toString(r));
            }
        }
        if (sorcerers.isEmpty()){
            sorcerers.add(new Hero("DefaultSorcerer",5,500,300,400,800,1500));
        }
    }

    private void loadPaladins() {
        List<String[]> rows = loadFile("Paladins.txt", 6);
        for (String[] r : rows) {
            try {
                String name = r[0];
                int mp = Integer.parseInt(r[1]);
                int str = Integer.parseInt(r[2]);
                int agi = Integer.parseInt(r[3]);
                int dex = Integer.parseInt(r[4]);
                int startLevel = 1;
                int gold = Integer.parseInt(r[5]);
                int exp = Integer.parseInt(r[6]);
                
                Hero h = new Hero(name, startLevel, mp, str, agi, dex, gold);
                h.setExp(exp);
                h.setHp(startLevel * 100.0);
                paladins.add(h);
            } catch (Exception ex) {
                System.out.println("Skipping malformed paladin row: " + Arrays.toString(r));
            }
        }
        if (paladins.isEmpty()){
            paladins.add(new Hero("DefaultPaladin",5,400,600,500,600,1500));
        } 
    }

    // monsters
    private void loadDragons() {
        List<String[]> rows = loadFile("Dragons.txt", 5);
        for (String[] r : rows) {
            try {
                String name = r[0];
                int lvl = Integer.parseInt(r[1]);
                double dmg = Double.parseDouble(r[2]);
                double def = Double.parseDouble(r[3]);
                double dodge = Double.parseDouble(r[4]);
                dragons.add(new Monster(name, MonsterType.DRAGON, lvl, dmg, def, dodge));
            } catch (Exception ex) {
                System.out.println("Skipping malformed dragon row: " + Arrays.toString(r));
            }
        }
        if (dragons.isEmpty()){
            dragons.add(new Monster("TinyDragon", MonsterType.DRAGON, 3, 200, 50, 5.0));
        }
    }

    private void loadSpirits() {
        List<String[]> rows = loadFile("Spirits.txt", 5);
        for (String[] r : rows) {
            try {
                String name = r[0];
                int lvl = Integer.parseInt(r[1]);
                double dmg = Double.parseDouble(r[2]);
                double def = Double.parseDouble(r[3]);
                double dodge = Double.parseDouble(r[4]);
                spirits.add(new Monster(name, MonsterType.SPIRIT, lvl, dmg, def, dodge));
            } catch (Exception ex) {
                System.out.println("Skipping malformed spirit row: " + Arrays.toString(r));
            }
        }
        if (spirits.isEmpty()){
            spirits.add(new Monster("Ghost", MonsterType.SPIRIT, 2, 120, 20, 12.0));
        }
    }

    private void loadExoskeletons() {
        List<String[]> rows = loadFile("Exoskeletons.txt", 5);
        for (String[] r : rows) {
            try {
                String name = r[0];
                int lvl = Integer.parseInt(r[1]);
                double dmg = Double.parseDouble(r[2]);
                double def = Double.parseDouble(r[3]);
                double dodge = Double.parseDouble(r[4]);
                exoskeletons.add(new Monster(name, MonsterType.EXOSKELETON, lvl, dmg, def, dodge));
            } catch (Exception ex) {
                System.out.println("Skipping malformed exoskeleton row: " + Arrays.toString(r));
            }
        }
        if (exoskeletons.isEmpty()){
            exoskeletons.add(new Monster("Beetle", MonsterType.EXOSKELETON, 2, 100, 200, 1.0));
        }
    }

    // getters
    public List<Weapon> getWeapons() { 
        return weapons; 
    }
    public List<Armor> getArmors() { 
        return armors; 
    }
    public List<Potion> getPotions() { 
        return potions; 
    }
    public List<Spell> getSpells() {
        return spells; 
    }

    public List<Hero> getWarriors() {
        return warriors;
    }
    public List<Hero> getSorcerers() {
        return sorcerers; 
    }
    public List<Hero> getPaladins() {
        return paladins; 
    }

    public List<Monster> getDragons() { 
        return dragons;
    }
    public List<Monster> getSpirits() { 
        return spirits; 
    }
    public List<Monster> getExoskeletons() { 
        return exoskeletons; 
    }

    public Weapon getDefaultWeaponOrNull() { 
        return weapons.isEmpty() ? null : weapons.get(0); 
    }
    public Armor getDefaultArmorOrNull() { 
        return armors.isEmpty() ? null : armors.get(0); 
    }

    // sample n items for market stock (random unique selection, padded with repeats if needed)
    public List<inventory.items.Item> sampleMarketStock(int count) {
        List<inventory.items.Item> pool = new ArrayList<>();
        pool.addAll(weapons);
        pool.addAll(armors);
        pool.addAll(potions);
        pool.addAll(spells);
        if (pool.isEmpty()){
            return new ArrayList<>();
        }
        Collections.shuffle(pool, new Random());
        List<inventory.items.Item> out = new ArrayList<>();
        for (int i = 0; i < count && i < pool.size(); i++){
            out.add(pool.get(i));
        }
        Random r = new Random();
        while (out.size() < count){
            out.add(pool.get(r.nextInt(pool.size())));
        }
        return out;
    }
}
