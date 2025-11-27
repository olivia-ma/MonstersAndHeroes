package main;

// centralized configuration using singleton like constants
// promotes maintainability by isolating game parameters for easy modification

public class GameConfig { 
    public final int MAP_WIDTH = 8; 
    public final int MAP_HEIGHT = 8; 
    public final double ENCOUNTER_CHANCE = 0.40; 
}