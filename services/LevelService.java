package services;

// service class handling progression and scaling logic
// addExp() manages hero leveling
// scaleMonsters() balances encounters to party level
// promotes single responsibility by isolating level related calcs

import pieces.creatures.Monster;
import pieces.creatures.Hero;

import java.util.List;

public class LevelService {

    public LevelService() { }

    public void addExp(Hero h, int amount) {
        h.addExp(amount);
    }

    
    public void scaleMonsters(List<Monster> monsters, int targetLevel) {
        if (monsters == null || monsters.isEmpty()){
            return;
        }

        for (Monster m : monsters) {

            int original = Math.max(1, m.getLevel());
            double factor = targetLevel / (double) original;

            // scale base monster stats
            m.setLevel(targetLevel);
            m.setHp(targetLevel * 100);

            m.setDamage(m.getDamage() * factor * 1.15);   // boost slightly
            m.setDefense(m.getDefense() * factor * 1.10);

            // dodge remains whatever type gives (but clamp)
            if (m.getDodge() > 70){
                m.setDodge(70);
            }
            
            if (m.getDodge() < 0){
                m.setDodge(0);
            }
        }
    }

}
