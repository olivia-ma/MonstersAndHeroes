package ui;

// concrete implementation of GameUI interface for terminal display
// uses ansi colors for visual enhanceement and implements health/mana bars
// printMap() displays world state
// printHeroBar() shows character status visually

import board.Board;
import board.Tile;
import pieces.parties.Party;
import pieces.creatures.Hero;

public class ConsoleUI implements GameUI {

    private final String RED = "\u001B[31m";
    private final String GREEN = "\u001B[32m";
    private final String YELLOW = "\u001B[33m";
    private final String BLUE = "\u001B[34m";
    private final String RESET = "\u001B[0m";

    @Override
    public void println(String s) {
        System.out.println(s + RESET);
    }

    @Override
    public void print(String s) {
        System.out.print(s + RESET);
    }

    @Override
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void printMap(Board world) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                Tile t = world.getTile(x, y);
                if (t.hasParty()){
                    sb.append(GREEN + " P " + RESET);
                }
                else if (t.getType() == board.TileType.INACCESSIBLE){
                    sb.append(RED + " # " + RESET);
                }
                else if (t.getType() == board.TileType.MARKET){
                    sb.append(YELLOW + " M " + RESET);
                }
                else{
                    sb.append(" . ");
                } 
            }
            sb.append("\n");
        }
        println(sb.toString());
    }

    @Override
    public void showPartyInfo(Party p) {
        println("--- Party Info ---");
        for (Hero h : p.getHeroes()) {
            println(h.statusLine());
        }
    }

    @Override
    public void printHeroBar(Hero h) {
        double maxHp = Math.max(1, h.getLevel()) * 100.0;
        double maxMp = Math.max(1, h.getLevel()) * 100.0;

        int hpBar = (int) Math.round((h.getHp() / maxHp) * 30);
        int mpBar = (int) Math.round((h.getMp() / maxMp) * 30);

        if (hpBar < 0){
            hpBar = 0;
        }
        if (hpBar > 30){
            hpBar = 30;
        } 
        if (mpBar < 0){
            mpBar = 0;
        }
        if (mpBar > 30){
            mpBar = 30;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(h.getName()).append(" ");

        sb.append(GREEN);
        sb.append("[");
        for (int i=0;i<hpBar;i++){
            sb.append('#');
        }
        for (int i=hpBar;i<30;i++){
            sb.append(' ');
        }
        sb.append("]");
        sb.append(RESET);
        sb.append(" HP:").append((int)h.getHp()).append(" ");

        sb.append(BLUE);
        sb.append("[");
        for (int i=0;i<mpBar;i++){
            sb.append('#');
        }
        for (int i=mpBar;i<30;i++){
            sb.append(' ');
        }
        sb.append("]");
        sb.append(RESET);
        sb.append(" MP:").append((int)h.getMp());

        println(sb.toString());
    }
}
