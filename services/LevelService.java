package services;

import pieces.creatures.Hero;

/**
 * Handles all leveling logic for heroes.
 * 
 * Leveling rules implemented according to assignment spec:
 *  - Hero levels up when EXP >= level * 10
 *  - On level up:
 *      * Strength, Dexterity, Agility increase by 5%
 *      * Mana increases by 10%
 *      * HP resets to level * 100
 *  - EXP rolls over after each level
 *
 * This service does NOT modify default attributes loaded from files;
 * each new game reloads defaults via FileParserService.
 */
public class LevelService {

    /**
     * Adds experience to a hero and applies level-up rules.
     *
     * @param hero The hero gaining experience.
     * @param exp Amount of EXP gained.
     */
    public void addExperience(Hero hero, int exp) {
        if (hero == null)
            return;

        hero.addRawExp(exp);   
        tryLevelUp(hero);
    }

    /**
     * Checks if the hero has enough EXP to level up and applies the level up
     * process repeatedly if needed.
     */
    private void tryLevelUp(Hero hero) {
        while (hero.getExp() >= hero.getLevel() * 10) {
            hero.setExp(hero.getExp() - hero.getLevel() * 10);
            hero.setLevel(hero.getLevel() + 1);

            applyLevelUpBonuses(hero);
        }
    }

    /**
     * Applies the stat boosts for a level-up event.
     */
    private void applyLevelUpBonuses(Hero hero) {
        hero.setStrength(hero.getStrength() * 1.05);
        hero.setDexterity(hero.getDexterity() * 1.05);
        hero.setAgility(hero.getAgility() * 1.05);

        hero.setMp(hero.getMp() * 1.10);

        // HP resets to base formula: level * 100
        hero.setHp(hero.getLevel() * 100);
    }
}
