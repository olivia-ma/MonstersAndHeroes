package main;

public class GameConfig { 
    public final int MAP_WIDTH = 8; 
    public final int MAP_HEIGHT = 8; 
    public final double ENCOUNTER_CHANCE = 0.40; 
    public final String[] DEFAULT_DATA_PATHS = new String[] { "/mnt/data/LegendsMonstersHeroesTxtFiles.pdf", "/mnt/data/Monsters and Heroes - 2025.pdf" }; 
    public final long RANDOM_SEED = System.currentTimeMillis(); 
}