package main;

public class GameDriver { public static void main(String[] args) { GameConfig config = new GameConfig();
Game game = new Game(config); game.start(); } }
