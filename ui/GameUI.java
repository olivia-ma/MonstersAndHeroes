package ui;

import board.Board; import pieces.parties.Party; import pieces.creatures.Hero;
public interface GameUI { void println(String s); void print(String s); void clearScreen(); void printMap(Board
b); void showPartyInfo(Party p); void printHeroBar(Hero h); }
