package ui;

import board.Board; import pieces.parties.Party;
import board.Tile;

import pieces.creatures.Hero;
public class ConsoleUI implements GameUI { private final String RED="\u001B[31m", GREEN="\u001B[32m",
YELLOW="\u001B[33m", BLUE="\u001B[34m", RESET="\u001B[0m"; public void println(String s)
{ System.out.println(s+RESET); } public void print(String s){ System.out.print(s+RESET); } public void
clearScreen(){ System.out.print("\033[H\033[2J"); System.out.flush(); } public void printMap(Board world)
{ StringBuilder sb = new StringBuilder(); for (int y=0;y<world.getHeight();y++){ for (int
x=0;x<world.getWidth();x++){ Tile t = world.getTile(x,y);
; if (t.hasParty()) sb.append(GREEN+" P "+RESET); else if
(t.getType()==board.TileType.INACCESSIBLE) sb.append(RED+" # "+RESET); else if
(t.getType()==board.TileType.MARKET) sb.append(YELLOW+" M "+RESET); else sb.append(" . "); }
sb.append("\n"); } println(sb.toString()); } public void showPartyInfo(Party p){ println("-- Party Info --"); for
(Hero h : p.getHeroes()) println(h.statusLine()); } public void printHeroBar(Hero h){ int hpBar =
(int)Math.min(30, Math.max(0, h.hp / (h.getLevel()/100) * 30)); int mpBar = (int)Math.min(30, Math.max(0,
h.getMp() / Math.max(1,h.getLevel()/100) * 30)); StringBuilder sb = new StringBuilder(); sb.append(h.getName()+" ");
sb.append(GREEN); sb.append("["+repeat('#',hpBar)+repeat(' ',30-hpBar)+"]"); sb.append(RESET);
sb.append(" HP:"+(int)h.hp+" "); sb.append(BLUE); sb.append("["+repeat('#',mpBar)+repeat(' ',30-mpBar)
+"]"); sb.append(RESET); sb.append(" MP:"+(int) h.getMp()); println(sb.toString()); } private String repeat(char c,int
n){ StringBuilder b=new StringBuilder(); for (int i=0;i<n;i++) b.append(c); return b.toString(); } }
