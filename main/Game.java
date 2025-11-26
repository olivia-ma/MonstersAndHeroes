package main;

import ui.ConsoleUI; 
import ui.GameUI; 
import board.Board; 
import pieces.parties.Party; 
import services.FileParserService; 
import services.SpawnService; 
import services.LevelService; 
import utils.RandomGenerator; 
import pieces.creatures.Hero;
import pieces.creatures.Monster;
import inventory.items.*;

import java.util.List;
import java.util.Scanner;
public class Game { private final GameConfig config; private final GameUI ui; private final Scanner scanner =
new Scanner(System.in); private Board board; private Party party;
public Game(GameConfig config) {
this.config = config;
this.ui = new ConsoleUI();
RandomGenerator.init(config.RANDOM_SEED);
}
public void start() {
ui.clearScreen();
ui.println("Welcome to LegendsRPG (multi-file).\n");
// parse default files if present
FileParserService parser = new FileParserService(config.DEFAULT_DATA_PATHS,
ui);
parser.loadAllDefaults();
setupParty(parser);
board = new Board(config.MAP_WIDTH, config.MAP_HEIGHT, party, ui);
board.generate(true);
mainLoop:
while (true) {
ui.printMap(board);
ui.println("Enter command (W/A/S/D/M/I/R/Q): ");
String cmd = scanner.nextLine().trim().toUpperCase();
if (cmd.isEmpty()) continue;
char c = cmd.charAt(0);
switch (c) {
case 'W': case 'A': case 'S': case 'D':
boolean moved = board.moveParty(c);
if (!moved) ui.println("Can't move there.");
else {
if (board.checkEncounter()) {
// spawn monsters using SpawnService and fight (Battle)
services.SpawnService spawn = new SpawnService(parser);
List<Monster> monsters = spawn.spawnFor(party);
battle.Battle b = new battle.Battle(party, monsters,
ui, scanner, parser);
boolean heroesWon = b.fight();
if (!heroesWon) { ui.println("All heroes dead. Game over."); break mainLoop; }
}
}
break;
case 'M':
if (board.isOnMarket()) {
board.enterMarket(scanner, ui);
} else ui.println("You are not on a market tile.");
break;
case 'I': ui.showPartyInfo(party); break;
case 'R': board.generate(true); ui.println("Map regenerated.");
break;
case 'Q': ui.println("Quitting. Goodbye."); break mainLoop;
default: ui.println("Unknown command.");
}
}
}
private void setupParty(FileParserService parser) {
ui.println("How many heroes in party? (1-3) [default 2]: ");
int n = 2;
try { String line = scanner.nextLine().trim(); if (!line.isEmpty()) n =
Integer.parseInt(line); } catch(Exception e) { n = 2; }
n = Math.max(1, Math.min(3, n));
party = new Party(n);
ui.println("Creating " + n + " heroes. Choose types and names:");
for (int i=0;i<n;i++) {
ui.println(String.format("Hero %d: pick class: 1)Warrior 2)Sorcerer 3)Paladin [default 1]: ", i+1));
String in = scanner.nextLine().trim();
int choice = 1; try { if (!in.isEmpty()) choice =
Integer.parseInt(in); } catch(Exception ex){choice=1;}
ui.println("Enter name [default Hero"+(i+1)+"]: ");
String name = scanner.nextLine().trim(); if (name.isEmpty()) name =
"Hero"+(i+1);
Hero h;
switch(choice){
case 2: h = Hero.createSorcerer(name); break;
case 3: h = Hero.createPaladin(name); break;
default: h = Hero.createWarrior(name); break;
}
// give starter equipment from parser defaults
Weapon starterWeapon = parser.getDefaultWeaponOrNull();
if (starterWeapon!=null) { h.getInventory().addItem(starterWeapon);
h.equipWeapon(starterWeapon); }
Armor starterArmor = parser.getDefaultArmorOrNull();
if (starterArmor!=null) { h.getInventory().addItem(starterArmor);
h.equipArmor(starterArmor); }
party.addHero(h);
}
}
}