package battle;

import pieces.parties.Party;
import pieces.creatures.Hero;
import pieces.creatures.Monster; import ui.GameUI; import java.util.*;
import java.util.stream.Collectors;

import
services.FileParserService;
public class Battle { private Party party; private List<Monster> monsters; private GameUI ui; private Scanner
scanner; private FileParserService parser; private Random rng = new Random(); public Battle(Party p,
List<Monster> monsters, GameUI ui, Scanner scanner, FileParserService parser){ this.party=p;
this.monsters=monsters; this.ui=ui; this.scanner=scanner; this.parser=parser; }

public boolean fight(){ ui.println("Battle started against " + monsters.size()
+ " monsters!"); while(true){
// heroes
for (Hero h : party.getHeroes()) {
if (h.isFainted()) continue;
ui.printHeroBar(h);
ui.println("Choose action for " + h.getName() + ": 1)Attack 2)Cast Spell 3)Potion 4)Equip");
String cmd = scanner.nextLine().trim(); if (cmd.isEmpty()) cmd="1";
switch(cmd.charAt(0)){
case '1': // attack
Monster target = chooseMonster(); if (target==null) break;
if (Math.random() < target.getDodge()*0.01) {
ui.println(target.getName() + " dodged the attack!"); } else { double dmg =
h.attackDamage() - target.getDefense()*0.01; if (dmg<0) dmg=0;
target.takeDamage(dmg); ui.println(h.getName() + " attacked "+target.getName()
+" for "+(int)dmg+" dmg."); if (target.isFainted()) ui.println(target.getName()
+" fainted!"); } break;
case '2': ui.println("Cast not implemented in full here.");
break;
case '3': ui.println("Potion not implemented in full here.");
break;
case '4': ui.println("Equip not implemented in full here.");
break;
default: ui.println("Defaulting to attack.");
}
}
monsters.removeIf(m->m.isFainted()); if (monsters.isEmpty()){
ui.println("Heroes won the battle!"); for (Hero h : party.getHeroes()) if (!
h.isFainted()) h.addExp(2); for (Hero h : party.getHeroes()) if (h.isFainted())
{ /* revive small amount */ } return true; }
// monsters
for (Monster m : new ArrayList<>(monsters)) {

if (m.isFainted()) continue;
List<Hero> targets = party.getHeroes().stream()
    .filter(x -> !x.isFainted())
    .collect(Collectors.toList()); if (targets.isEmpty()) { ui.println("All heroes fainted."); return false; }
Hero target = targets.get(rng.nextInt(targets.size()));
if
(Math.random() < target.attackDamage()*0.001) { ui.println(target.getName()+"dodged "+m.getName()+"'s attack."); continue; }
double dmg = m.getDamage() - 0; if (Math.random() < 0.05) {
ui.println("Monster critical!"); dmg *= 1.5; } dmg = Math.max(1, dmg);
target.takeDamage(dmg); ui.println(m.getName() + " attacked " +
target.getName() + " for " + (int)dmg + " dmg."); if (target.isFainted())
ui.println(target.getName()+" fainted!");
}
}
}
private Monster chooseMonster(){ List<Monster> alive =
    monsters.stream()
            .filter(x -> !x.isFainted())
            .collect(Collectors.toList());
if (alive.isEmpty())
return null; ui.println("Choose target:"); for (int i=0;i<alive.size();i++)
ui.println((i+1)+") "+alive.get(i).getName()+" HP:"+(int)alive.get(i).hp);
String s = scanner.nextLine().trim(); if (s.isEmpty()) s="1"; int idx=1; try{
idx=Integer.parseInt(s);}catch(Exception ex){idx=1;}
idx=Math.max(1,Math.min(alive.size(),idx)); return alive.get(idx-1); }
}
