package battle;

// implements the turn based combat between heroes and monsters
// uses state pattern for battle phases and command pattern for hero actions
// main function is fight(). it orchestrates the battle rounds, handles hero actions (attack, spells, potions, equips), 
// monster AI, end of round recovery.
// supports dodge mechanics, spells effects, inventory management

import pieces.parties.Party;
import pieces.creatures.Hero;
import pieces.creatures.Monster;
import inventory.items.*;
import services.LevelService;
import ui.GameUI;

import java.util.*;
import java.util.stream.Collectors;

public class Battle {
    private Party party;
    private List<Monster> monsters;
    private GameUI ui;
    private java.util.Scanner scanner;
    private LevelService levelService;
    private Random rng = new Random();

    public Battle(Party party, List<Monster> monsters, GameUI ui, java.util.Scanner scanner, LevelService levelService) {
        this.party = party;
        this.monsters = new ArrayList<>(monsters);
        this.ui = ui;
        this.scanner = scanner;
        this.levelService = levelService;
    }

    public boolean fight() {
        ui.println("Battle started against " + monsters.size() + " monsters!");
        final int originalMonsterCount = monsters.size();

        while (true) {
            // heroes turn
            for (Hero h : party.getHeroes()) {
                if (allMonstersDead()){
                    break; 
                }

                if (h.isFainted()){
                    continue;
                }
                boolean acted = false;

                while (!acted) {
                    ui.printHeroBar(h);
                    ui.println("Choose action for " + h.getName() + ": 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect");

                    String cmd = scanner.nextLine().trim();
                    if (cmd.isEmpty()){
                        cmd = "1";
                    }
                    char c = cmd.charAt(0);

                    switch (c) {
                        case '1': // attack
                            Monster t = chooseMonster();
                            // System.out.println("monster dodge rate: " + t.getDodge()*.01);
                            if (t == null) break;
                            if (Math.random() < Math.max(40, t.getDodge()) * 0.01) {
                                ui.println("-- " + t.getName() + " dodged the attack! --");
                            } else {
                                double dmg = h.attackDamage() - (t.getDefense() * 0.01);
                                if (dmg < 0){
                                    dmg = 0;
                                }
            
                                t.takeDamage(dmg);
                                ui.println("-- "  + h.getName() + " attacked " + t.getName() + " for " + (int)dmg + " dmg. --");
                                if (t.isFainted()) ui.println("-- " + t.getName() + " fainted! --");
                            }

                            acted = true;
                            break;

                        case '2': // cast spell
                            List<Spell> spells = h.getInventory().findAllOfType(Spell.class);
                            
                            if (spells.isEmpty()) { 
                                ui.println("No spells available."); 
                                continue; 
                            }

                            ui.println("Choose spell to cast:");
                            for (int i=0; i<spells.size(); i++) {
                                ui.println((i+1)+") "+spells.get(i).toString());
                            }

                            String sel = scanner.nextLine().trim(); 
                            if (sel.isEmpty()){
                                sel="1";
                            }

                            int sidx = safeParse(sel,1) - 1;
                            if (sidx < 0 || sidx >= spells.size()) { 
                                ui.println("Invalid selection."); 
                                continue; 
                            }

                            Spell sp = spells.get(sidx);

                            if (h.getMp() < sp.getManaCost()) { 
                                ui.println("Not enough MP to cast."); 
                                continue; 
                            }
                            
                            Monster targ = chooseMonster();
                            if (targ == null){
                                break;
                            }

                            // the actual dmg
                            double spellDamage = sp.getDamage() + (h.getDexterity()/10000.0) * sp.getDamage();
                            targ.takeDamage(spellDamage);
                            h.setMp(h.getMp() - sp.getManaCost());
                            ui.println("-- " + h.getName()+ " cast "+ sp.getName()+ " on "+ targ.getName() + " for "+ (int)spellDamage + " dmg. --");
                            
                            // apply effect
                            switch (sp.getType()) {
                                case ICE:
                                    targ.setDamage(targ.getDamage()*0.9);
                                    ui.println(targ.getName()+"'s damage reduced by ice.");
                                    acted = true;
                                    break;
                                case FIRE:
                                    targ.setDefense(targ.getDefense()*0.9);
                                    ui.println(targ.getName()+"'s defense reduced by fire.");
                                    acted = true;
                                    break;
                                case LIGHTNING:
                                    targ.setDodge(Math.max(0.0, targ.getDodge()*0.9));
                                    ui.println(targ.getName()+"'s dodge reduced by lightning.");
                                    acted = true;
                                    break;
                            }

                            // consume spell, single-use
                            h.getInventory().removeItem(sp);
                            if (targ.isFainted()){
                                ui.println(targ.getName()+" fainted!");
                            }

                            acted = true;
                            break;

                        case '3': // use potion
                            List<Potion> pots = h.getInventory().findAllOfType(Potion.class);
                            if (pots.isEmpty()) { 
                                ui.println("No potions."); 
                                continue; 
                            }

                            ui.println("Choose potion to use:");
                            for (int i=0; i < pots.size(); i++){
                                ui.println((i+1)+") "+pots.get(i).toString());
                            }

                            String psel = scanner.nextLine().trim(); if (psel.isEmpty()) psel = "1";
                            int pidx = safeParse(psel,1) - 1;

                            if (pidx < 0 || pidx >= pots.size()) { 
                                ui.println("Invalid selection."); 
                                continue; 
                            }

                            Potion pot = pots.get(pidx);
                            // apply potion effects
                            for (String attr : pot.getAffectedStats()) {
                                String a = attr.toUpperCase();
                                if (a.startsWith("HP")) { h.setHp(h.getHp() + pot.getIncreaseAmount()); ui.println("Recovered HP +"+pot.getIncreaseAmount()); }
                                else if (a.startsWith("MP")) { h.setMp(h.getMp() + pot.getIncreaseAmount()); ui.println("Recovered MP +"+pot.getIncreaseAmount()); }
                                else if (a.startsWith("STR")) { h.setStrength(h.getStrength() + pot.getIncreaseAmount()); ui.println("STR +"+pot.getIncreaseAmount()); }
                                else if (a.startsWith("DEX")) { h.setDexterity(h.getDexterity() + pot.getIncreaseAmount()); ui.println("DEX +"+pot.getIncreaseAmount()); }
                                else if (a.startsWith("AGI")) { h.setAgility(h.getAgility() + pot.getIncreaseAmount()); ui.println("AGI +"+pot.getIncreaseAmount()); }
                            }
                            h.getInventory().removeItem(pot);
                            
                            acted = true;
                            break;

                        // inside case '4': Equip
                        case '4':
                            ui.println("Equip menu: W) Weapons, A) Armors, L) Leave");
                            String em = scanner.nextLine().trim().toUpperCase();
                            if (em.equals("L")){
                                break;
                            }

                            // weapons
                            if (em.equals("W")) {
                                List<Weapon> available = h.getUnequippedWeapons();

                                if (available.isEmpty()) { 
                                    ui.println("No weapons to equip."); 
                                    break; 
                                }

                                ui.println("Weapons:");
                                for (int i=0;i<available.size();i++){
                                    ui.println((i+1)+") "+available.get(i));
                                }
                                ui.println("Enter <num> or <num>T for two-hands, or L to cancel");

                                String wchoice = scanner.nextLine().trim().toUpperCase();
                                if (wchoice.equals("L")) break;

                                boolean two = wchoice.endsWith("T");
                                String numPart = wchoice.replaceAll("[^0-9]", "");
                                int idx = safeParse(numPart, 1) - 1;

                                if (idx < 0 || idx >= available.size()) { 
                                    ui.println("Invalid."); 
                                    break; 
                                }

                                Weapon pick = available.get(idx);
                                boolean ok = h.equipWeapon(pick, two);

                                if (!ok) {
                                    ui.println("Both hands full. Replace Left (L) or Right (R)?");
                                    String hand = scanner.nextLine().trim().toUpperCase();
                                    boolean left = hand.equals("L");
                                    h.equipWeaponIntoSlot(pick, left);
                                }

                                ui.println("Equipped " + pick.getName());
                                acted = true;
                                break;
                            }
                            
                            // armor
                            if (em.equals("A")) {
                                List<Armor> available = h.getUnequippedArmors();

                                if (available.isEmpty()) { 
                                    ui.println("No armor to equip."); 
                                    break; 
                                }

                                ui.println("Armors:");
                                for (int i=0;i<available.size();i++){
                                    ui.println((i+1)+") "+available.get(i));
                                }

                                String achoice = scanner.nextLine().trim();
                                int idx = safeParse(achoice,1) - 1;
                                if (idx < 0 || idx >= available.size()) { 
                                    ui.println("Invalid."); 
                                    break; 
                                }

                                Armor pick = available.get(idx);
                                h.equipArmor(pick);
                                ui.println("Equipped "+pick.getName());

                                acted = true;
                                break;
                            }

                            break;


                        case '5': // inspect
                            ui.println(h.statusLine());
                            ui.println("Monsters:");
                            for (Monster mm : monsters){
                                 ui.println(mm.getName()+" L"+mm.getLevel()+" HP:"+ (int)mm.getHp()+" DMG:"+ (int)mm.getDamage()+" DEF:"+ (int)mm.getDefense()+" DODGE:"+ (int)mm.getDodge());
                            }
                            
                            break;

                        default:
                            ui.println("Defaulting to attack.");
                            acted = true;
                    }
                }

                if (allMonstersDead()){
                    break; 
                }
            }

            // remove dead monsters
            monsters.removeIf(Monster::isFainted);

            if (monsters.isEmpty()) {
                ui.println("All monsters defeated!");
                
                // reward survivors
        
                for (Hero hh : party.getHeroes()) {
                    if (!hh.isFainted()) {
                        levelService.addExp(hh, 2 * originalMonsterCount);
                        // give gold reward
                        hh.spendGold(-(originalMonsterCount * 50));
                    } else {
                        // revive with half HP/MP
                        hh.setHp(hh.getLevel()*100.0 * 0.5);
                        hh.setMp(hh.getLevel()*100.0 * 0.5);
                    }
                }

                
                return true;
            }

            

            // monsters turn
            for (Monster m : new ArrayList<>(monsters)) {

                if (allMonstersDead()){
                    break; 
                }
                
                if (m.isFainted()){
                    continue;
                }

                List<Hero> targets = party.getHeroes().stream().filter(x -> !x.isFainted()).collect(Collectors.toList());
                if (targets.isEmpty()) { 
                    ui.println("All heroes fainted :("); 
                    return false; 
                }

                Hero target = targets.get(rng.nextInt(targets.size()));
                // System.out.println("agility: " + target.getAgility());
                double heroDodge =  target.getAgility() * 0.0002;
                // System.out.println("hero dodge rate: " + heroDodge);
                if (Math.random() < heroDodge) {
                    ui.println("-- "+ target.getName() + " dodged " + m.getName() + "'s attack. --");
                    continue;
                }
                double dmg = m.getDamage();

                // monster crit
                if (Math.random() < 0.05) { 
                    ui.println("=- Monster critical! --"); 
                    dmg *= 1.5; 
                }

                // armor becomes % reduction
                double reductionPercent = target.getEquippedArmorReduction() * 0.005;  
                if (reductionPercent > 0.60){
                    reductionPercent = 0.60; // max 60% reduction
                }

                dmg = dmg * (1.0 - reductionPercent);

                dmg = Math.max(1, dmg);
                target.takeDamage(dmg);
                ui.println("-- "+ m.getName() + " attacked " + target.getName() + " for " + (int)dmg + " dmg. --");

                if (target.isFainted()) ui.println("-- " + target.getName() + " fainted! --");
            }

            // end of round recovery
            for (Hero hh : party.getHeroes()) {
                if (!hh.isFainted()) {
                    double maxHp = hh.getLevel() * 100.0;
                    double recoveredHp = Math.min(maxHp, hh.getHp() + 0.1 * maxHp);
                    hh.setHp(recoveredHp);
                    double maxMp = hh.getLevel() * 100.0; 
                    double recoveredMp = hh.getMp() + 0.1 * maxMp;
                    hh.setMp(recoveredMp);
                }
            }
        } 
    }

    private Monster chooseMonster() {
        List<Monster> alive = monsters.stream().filter(x -> !x.isFainted()).collect(Collectors.toList());
        if (alive.isEmpty()){
            return null;
        }

        ui.println("Choose target:");
        for (int i = 0; i < alive.size(); i++) {
            Monster m = alive.get(i);
            ui.println((i+1)+") "+m.getName()+" HP:"+ (int)m.getHp());
        }

        String s = scanner.nextLine().trim(); 
        
        if (s.isEmpty()){
            s="1";
        }

        int idx = 1;
        try { 
            idx = Integer.parseInt(s); 
        } catch (Exception ex) {
            idx = 1; 
        }

        idx = Math.max(1, Math.min(alive.size(), idx));
        return alive.get(idx - 1);
    }

    // implements the safeParse that isn't in java 1.8
    private int safeParse(String s, int def) {
        try { 
            return Integer.parseInt(s.trim()); 
        } catch (Exception e) {
            return def; 
        }
    }

    private boolean allMonstersDead() {
        return monsters.stream().allMatch(Monster::isFainted);
    }

    private boolean allHeroesDead() {
        return party.getHeroes().stream().allMatch(Hero::isFainted);
    }

}
