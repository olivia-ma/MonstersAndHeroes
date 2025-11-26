package pieces.parties;

import pieces.creatures.Hero; import java.util.*;
public class Party { private List<Hero> heroes = new ArrayList<>(); public Party(int cap) {} public void
addHero(Hero h){ heroes.add(h); } public List<Hero> getHeroes(){ return
Collections.unmodifiableList(heroes); } public int size(){ return heroes.size(); } public int highestLevel(){ int
m=1; for (Hero h: heroes) m = Math.max(m, h.getLevel()); return m; } }