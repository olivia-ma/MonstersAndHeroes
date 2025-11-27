package pieces.parties;

// manages groups of creatures with composite operations
// container supporting team wide operations, checks if all members are dead

import pieces.creatures.Hero;
import java.util.ArrayList;
import java.util.List;

public class Party {
    private List<Hero> heroes = new ArrayList<Hero>();

    public Party(int initialSize) {
        // empty; callers will add heroes
    }

    public void addHero(Hero h) { 
        if (h != null){
            heroes.add(h); 
        }
    }

    public List<Hero> getHeroes() { 
        return heroes; 
    }

    public int size() { 
        return heroes.size(); 
    }
}
