package ui;

// interface defining ui contract, supporting multiple presentation layers
// allows for flexible ui implementations

import board.Board;
import pieces.parties.Party;
import pieces.creatures.Hero;

public interface GameUI {
    void println(String s);
    void print(String s);
    void clearScreen();
    void printMap(Board b);
    void showPartyInfo(Party p);
    void printHeroBar(Hero h);
}
