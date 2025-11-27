package services;

// factory for monster generation with level appropriate scaling
// uses protype pattern by cloning base monsters and adjusting stats
// ensures balanced encounters based on party strength

import java.util.*;
import pieces.creatures.*;
import pieces.parties.Party;

public class SpawnService {

    private FileParserService parser;
    private LevelService levelService = new LevelService();
    private Random rng = new Random();

    public SpawnService(FileParserService parser) {
        this.parser = parser;
    }

    public SpawnService() { }

    public List<Monster> spawnFor(Party party) {
        List<Monster> result = new ArrayList<Monster>();
        int count = Math.max(1, party.size());

        int highestHeroLevel = computeHighestHeroLevel(party);

        // gather all prototypes
        List<Monster> pool = new ArrayList<Monster>();
        if (parser != null) {
            pool.addAll(parser.getDragons());
            pool.addAll(parser.getSpirits());
            pool.addAll(parser.getExoskeletons());
        }

        if (pool.isEmpty()) {
            result.add(new Monster("Fallback#0", MonsterType.DRAGON, 1, 200, 100, 10));
            return result;
        }

        // spawn monsters
        for (int i = 0; i < count; i++) {
            Monster proto = pool.get(rng.nextInt(pool.size()));

            // clone monster
            Monster clone = new Monster(
                proto.getName() + "#" + rng.nextInt(1000),
                proto.getType(),
                proto.getLevel(),
                proto.getDamage(),
                proto.getDefense(),
                proto.getDodge()
            );

            result.add(clone);
        }

        // scale to hero level
        levelService.scaleMonsters(result, highestHeroLevel);

        return result;
    }

    private int computeHighestHeroLevel(Party p) {
        int max = 1;
        for (Hero h : p.getHeroes()) {
            if (h.getLevel() > max){
                max = h.getLevel();
            }
        }
        return max;
    }
}
