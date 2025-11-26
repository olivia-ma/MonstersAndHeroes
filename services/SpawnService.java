package services;

import java.util.*; 
import pieces.creatures.*;

public class SpawnService { private FileParserService parser; private Random rng = new Random(); public
SpawnService(FileParserService parser){ this.parser = parser; }

public SpawnService(){}
public List<Monster> spawnFor(pieces.parties.Party p){ List<Monster> res = new
ArrayList<>(); int count = Math.max(1, p.size()); int highest =
p.highestLevel(); for (int i=0;i<count;i++){ int level = Math.max(1, highest +
(rng.nextInt(3)-1)); MonsterType t = MonsterType.values()
[rng.nextInt(MonsterType.values().length)]; String name = t.toString()
+"#"+rng.nextInt(1000); double baseDamage = 100 + level*50; double def = 50 +
level*40; double dodge = 5 + level*2; if (t==MonsterType.DRAGON) baseDamage *=
1.2; if (t==MonsterType.EXOSKELETON) def *= 1.4; if (t==MonsterType.SPIRIT)
dodge *= 1.7; res.add(new Monster(name, t, level, baseDamage, def, dodge)); }
return res; }
}