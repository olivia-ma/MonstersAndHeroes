**Legends: Monsters and Heroes**



* Name: Olivia Ma
* Email: oliviama@bu.edu
* BUID: U27241905



**Files**

Core Game Structure:

* Main.java: Application entry point, minimal bootstrap following single responsibility principle.

* Game.java: Main game controller implementing Facade pattern to orchestrate all subsystems (world, battle, market, UI).

* GameConfig.java: Centralized configuration management with constants for easy game balancing and modification.


World/Board Components: 

* Board.java: Manages game grid using Composite pattern, handles map generation with pathfinding validation and movement.

* Tile.java: Implements State pattern for different tile behaviors (common, market, inaccessible).

* Position.java: Immutable value object for spatial coordinates ensuring data integrity.

* TileType.java: Type-safe enumeration enabling polymorphic tile handling.


Entity/Pieces Management: 

* Creature.java: Abstract base class using Template Method pattern for common hero/monster behaviors.

* Hero.java: Player character with complex equipment system supporting dual-wielding and stat progression.

* Monster.java: Single class handling all monster types via composition (type field) rather than inheritance hierarchy.

* HeroType.java / MonsterType.java: Enums supporting behavioral variations without class explosion.

* Party.java: Composite container for managing hero/monster groups with team-wide operations.


Inventory System: 

* Inventory.java: Business logic container (not just a list) with type-safe item filtering and equipment tracking.

* Item.java: Abstract base class with Template Method pattern for all game items.

* Weapon.java / Armor.java / Potion.java / Spell.java: Specialized item implementations with type-specific behaviors.

Battle System:

* Battle.java: Turn-based combat engine using State pattern for battle phases and Command pattern for actions. Implements hero actions (attack, spell, potion, equip), monster AI, dodge mechanics, and round-based recovery.

Market System: 
* Market.java: Facade coordinating marketplace operations with Factory method for random market generation.

* MarketInventory.java: Implements Strategy pattern for buy/sell transactions with validation logic.

Service Layer: 
* FileParserService.java: Repository pattern for data access, loads game data from text files with graceful error handling.

* LevelService.java: Handles progression logic and monster scaling for balanced encounters.

* SpawnService.java: Factory for monster generation using Prototype pattern with level-appropriate scaling.

User Interface: 
* GameUI.java: Interface defining UI contract supporting multiple presentation implementations.

* ConsoleUI.java: Concrete terminal implementation with ANSI colors, health/mana bars, and visual map display.


**Object-Oriented Design Notes**

Scalability:
* Single Responsibility: Each class has one reason to change (Battle handles combat, Board manages world, Market handles transactions)

* Open/Closed Principle: Easy to add new hero types, monsters, items, or spells without modifying existing code

* Repository Pattern: FileParserService allows easy addition of new data files and formats

* Strategy Pattern: Shuffle algorithms, market behaviors, and combat calculations can be extended

Extendibility:
* Interface Segregation: GameUI interface allows swapping console implementation for GUI

* Factory Methods: Market.randomMarket() and monster spawning support varied implementations

* Template Method: Creature and Item base classes enable consistent extension

* Component Architecture: Clear separation allows independent enhancement of battle, market, or world systems

Design Patterns Implemented:
* Facade: Game and Market classes simplify complex subsystem interactions

* Strategy: Hero types with different stat growth, transaction services

* State: Tile behaviors, battle phases

* Factory: Object creation (markets, monsters)

* Template Method: Base class behaviors (Creature, Item)

* Repository: Data access abstraction

* Composite: Inventory and board management


SOLID Principles Compliance:
* Single Responsibility: Each service class handles one domain (LevelService for progression, SpawnService for monsters)

* Open/Closed: New item types can be added without modifying Inventory class

* Interface Segregation: GameUI provides only necessary methods for UI implementations

* Dependency Inversion: High-level modules depend on abstractions (GameUI interface)



**How to compile and run**

Simply just type: ``java -cp bin main.Main``


If the steps above didn't work, try the steps below:

1. Manually delete the bin folder
2. ``mkdir bin``
3. ``javac -d bin (Get-ChildItem -Recurse -Filter *.java | Select-Object -Expand FullName)``
4. ``java -cp bin main.Main``




**Input/Output Example**

(I think what is input and output is kind of self explanatory... so I'll summarize it here! Everything that ends with an indent, '?', or ':' is an INPUT. Everything else is an OUTPUT. Hope that makes sense! I didn't want to clutter up the example and make it confusing ;w;)

```

(base) PS C:\Users\17815\Desktop\CS611\5Assignment> java -cp bin main.Main

**********************************
*                                *
*  Legends: Monsters and Heroes  *
*                                *
**********************************

How many heroes in party? (1-3) [default 2]:
2
Creating 2 heroes. Choose types and names:
Hero 1: pick class: 1)Warrior 2)Sorcerer 3)Paladin [default 1]:
1 
Choose a Warrior from the list below (type number or letter):
A) Gaerdal_Ironhand (Lvl 1 HP:100 MP:100)
B) Sehanine_Monnbow (Lvl 1 HP:100 MP:600)
C) Muamman_Duathall (Lvl 1 HP:100 MP:300)
D) Flandal_Steelskin (Lvl 1 HP:100 MP:200)
E) Undefeated_Yoj (Lvl 1 HP:100 MP:400)
F) Eunoia_Cyn (Lvl 1 HP:100 MP:400)
a
Enter name [default Gaerdal_Ironhand]: 
Olivia
Hero 2: pick class: 1)Warrior 2)Sorcerer 3)Paladin [default 1]: 
2
Choose a Sorcerer from the list below (type number or letter):
A) Rillifane_Rallathil (Lvl 1 HP:100 MP:1300)
B) Segojan_Earthcaller (Lvl 1 HP:100 MP:900)
C) Reign_Havoc (Lvl 1 HP:100 MP:800)
D) Reverie_Ashels (Lvl 1 HP:100 MP:900)
E) Kalabar (Lvl 1 HP:100 MP:800)
F) Skye_Soar (Lvl 1 HP:100 MP:1000)
a
Enter name [default Rillifane_Rallathil]: 
Joseph
 .  .  .  .  .  #  #  #
 M  .  .  .  M  M  #  .
 M  .  #  .  #  M  #  M
 .  .  M  P  .  M  .  .
 M  .  .  .  M  .  M  .
 .  .  .  .  M  .  .  .

--- Party Info ---
Olivia L1 HP:100 MP:100 Str:700 Dex:600 Agi:500 Gold:1354 Weapon:Sword Armor:Platinum_Shield
Joseph L1 HP:100 MP:100 Str:750 Dex:500 Agi:450 Gold:2500 Weapon:Sword Armor:Platinum_Shield
Commands: W/A/S/D move, I inventory, R restart map, Q quit
w 
-- A wild group of monsters appears! --
Battle started against 2 monsters!
Olivia [##############################] HP:100 [##############################] MP:100
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
5
Olivia L1 HP:100 MP:100 Str:700 Dex:600 Agi:500 Gold:1354 Weapon:Sword Armor:Platinum_Shield
Monsters:
BunsenBurner#298 L1 HP:100 DMG:86 DEF:137 DODGE:45
Kas-Ethelinh#494 L1 HP:100 DMG:69 DEF:110 DODGE:60
Olivia [##############################] HP:100 [##############################] MP:100
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
a
Defaulting to attack.
Joseph [##############################] HP:100 [##############################] MP:100
Choose action for Joseph: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
Exception in thread "main" java.util.NoSuchElementException: No line found
        at java.util.Scanner.nextLine(Scanner.java:1540)
        at battle.Battle.fight(Battle.java:55)
        at main.Game.mainLoop(Game.java:103)
(base) PS C:\Users\17815\Desktop\CS611\5Assignment> java -cp bin main.Main

**********************************
*                                *
*  Legends: Monsters and Heroes  *
*                                *
**********************************

How many heroes in party? (1-3) [default 2]: 
2
Creating 2 heroes. Choose types and names:
Hero 1: pick class: 1)Warrior 2)Sorcerer 3)Paladin [default 1]: 
1
Choose a Warrior from the list below (type number or letter):
A) Gaerdal_Ironhand (Lvl 1 HP:100 MP:100)
B) Sehanine_Monnbow (Lvl 1 HP:100 MP:600)
C) Muamman_Duathall (Lvl 1 HP:100 MP:300)
D) Flandal_Steelskin (Lvl 1 HP:100 MP:200)
E) Undefeated_Yoj (Lvl 1 HP:100 MP:400)
F) Eunoia_Cyn (Lvl 1 HP:100 MP:400)
a
Enter name [default Gaerdal_Ironhand]: 
Olivia
Hero 2: pick class: 1)Warrior 2)Sorcerer 3)Paladin [default 1]: 
2
Choose a Sorcerer from the list below (type number or letter):
A) Rillifane_Rallathil (Lvl 1 HP:100 MP:1300)
B) Segojan_Earthcaller (Lvl 1 HP:100 MP:900)
C) Reign_Havoc (Lvl 1 HP:100 MP:800)
D) Reverie_Ashels (Lvl 1 HP:100 MP:900)
E) Kalabar (Lvl 1 HP:100 MP:800)
F) Skye_Soar (Lvl 1 HP:100 MP:1000)
a
Enter name [default Rillifane_Rallathil]: 
Joseph
 M  .  M  M  #  M  .  .
 M  .  #  .  #  P  .  .
 .  .  .  .  M  .  #  .
 .  M  #  .  M  .  M  .
 M  #  .  .  .  .  .  .
 .  #  M  .  .  M  .  .

--- Party Info ---
Olivia L1 HP:100 MP:100 Str:700 Dex:600 Agi:500 Gold:1354 Weapon:Sword Armor:Platinum_Shield
Joseph L1 HP:100 MP:100 Str:750 Dex:500 Agi:450 Gold:2500 Weapon:Sword Armor:Platinum_Shield
Commands: W/A/S/D move, I inventory, R restart map, Q quit
s
-- A wild group of monsters appears! --
Battle started against 2 monsters!
Olivia [##############################] HP:100 [##############################] MP:100
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
5
Olivia L1 HP:100 MP:100 Str:700 Dex:600 Agi:500 Gold:1354 Weapon:Sword Armor:Platinum_Shield
Monsters:
WickedWitch#541 L1 HP:100 DMG:86 DEF:192 DODGE:25
Ereshkigall#821 L1 HP:100 DMG:67 DEF:82 DODGE:35
Olivia [##############################] HP:100 [##############################] MP:100
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) WickedWitch#541 HP:100
2) Ereshkigall#821 HP:100
2
-- Ereshkigall#821 dodged the attack! --
Joseph [##############################] HP:100 [##############################] MP:100
Choose action for Joseph: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) WickedWitch#541 HP:100
2) Ereshkigall#821 HP:100
2
-- Ereshkigall#821 dodged the attack! --
=- Monster critical! --
-- WickedWitch#541 attacked Joseph for 51 dmg. --
-- Ereshkigall#821 attacked Olivia for 26 dmg. --
Olivia [#########################     ] HP:83 [##############################] MP:100
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) WickedWitch#541 HP:100
2) Ereshkigall#821 HP:100
2
-- Olivia attacked Ereshkigall#821 for 74 dmg. --
Joseph [#################             ] HP:58 [##############################] MP:100
Choose action for Joseph: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) WickedWitch#541 HP:100
2) Ereshkigall#821 HP:25
2
-- Ereshkigall#821 dodged the attack! --
-- WickedWitch#541 attacked Olivia for 34 dmg. --
-- Joseph dodged Ereshkigall#821's attack. --
Olivia [##################            ] HP:58 [##############################] MP:100
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) WickedWitch#541 HP:100
2) Ereshkigall#821 HP:25
2
-- Olivia attacked Ereshkigall#821 for 149 dmg. --
-- Ereshkigall#821 fainted! --
Joseph [####################          ] HP:68 [##############################] MP:100
Choose action for Joseph: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) WickedWitch#541 HP:100
1
-- Joseph attacked WickedWitch#541 for 75 dmg. --
-- WickedWitch#541 attacked Olivia for 34 dmg. --
Olivia [##########                    ] HP:34 [##############################] MP:100
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) WickedWitch#541 HP:24
1
-- Olivia attacked WickedWitch#541 for 73 dmg. --
-- WickedWitch#541 fainted! --
All monsters defeated!
Battle won. Continuing...
 M  .  M  M  #  M  .  .
 M  .  #  .  #  .  .  .
 .  .  .  .  M  P  #  .
 .  M  #  .  M  .  M  .
 M  #  .  .  .  .  .  .
 .  #  M  .  .  M  .  .

--- Party Info ---
Olivia L2 HP:200 MP:110 Str:735 Dex:630 Agi:525 Gold:1454 Weapon:Sword Armor:Platinum_Shield
Joseph L2 HP:200 MP:110 Str:788 Dex:525 Agi:473 Gold:2600 Weapon:Sword Armor:Platinum_Shield
Commands: W/A/S/D move, I inventory, R restart map, Q quit
w
 M  .  M  M  #  M  .  .
 M  .  #  .  #  P  .  .
 .  .  .  .  M  .  #  .
 .  M  #  .  M  .  M  .
 M  #  .  .  .  .  .  .
 .  #  M  .  .  M  .  .

--- Party Info ---
Olivia L2 HP:200 MP:110 Str:735 Dex:630 Agi:525 Gold:1454 Weapon:Sword Armor:Platinum_Shield
Joseph L2 HP:200 MP:110 Str:788 Dex:525 Agi:473 Gold:2600 Weapon:Sword Armor:Platinum_Shield
Commands: W/A/S/D move, I inventory, R restart map, Q quit
w
You find a market here.
Opening market for Olivia

--- Market ---
Olivia gold: 1454
Market stock:
1) Snow_Cannon (ICE, DMG 650, MP 250) Sell:250
2) Strength_Potion (Potion +75 to [Strength]) Sell:100
3) Electric_Arrows (LIGHTNING, DMG 650, MP 200) Sell:275
4) Full_Body_Armor (Armor -1100, lvl 8) Sell:500
5) Healing_Potion (Potion +100 to [Health]) Sell:125
6) Breastplate (Armor -600, lvl 3) Sell:175
Options: B<num> buy, S sell, V view inventory, L leave
B1
Bought Snow_Cannon for 500 gold.

--- Market ---
Olivia gold: 954
Market stock:
1) Strength_Potion (Potion +75 to [Strength]) Sell:100
2) Electric_Arrows (LIGHTNING, DMG 650, MP 200) Sell:275
3) Full_Body_Armor (Armor -1100, lvl 8) Sell:500
4) Healing_Potion (Potion +100 to [Health]) Sell:125
5) Breastplate (Armor -600, lvl 3) Sell:175
Options: B<num> buy, S sell, V view inventory, L leave
B4
Bought Healing_Potion for 250 gold.

--- Market ---
Olivia gold: 704
Market stock:
1) Strength_Potion (Potion +75 to [Strength]) Sell:100
2) Electric_Arrows (LIGHTNING, DMG 650, MP 200) Sell:275
3) Full_Body_Armor (Armor -1100, lvl 8) Sell:500
4) Breastplate (Armor -600, lvl 3) Sell:175
Options: B<num> buy, S sell, V view inventory, L leave
l
Opening market for Joseph

--- Market ---
Joseph gold: 2600
Market stock:
1) Strength_Potion (Potion +75 to [Strength]) Sell:100
2) Electric_Arrows (LIGHTNING, DMG 650, MP 200) Sell:275
3) Full_Body_Armor (Armor -1100, lvl 8) Sell:500
4) Breastplate (Armor -600, lvl 3) Sell:175
Options: B<num> buy, S sell, V view inventory, L leave
l
 M  .  M  M  #  P  .  .
 M  .  #  .  #  .  .  .
 .  .  .  .  M  .  #  .
 .  M  #  .  M  .  M  .
 M  #  .  .  .  .  .  .
 .  #  M  .  .  M  .  .

--- Party Info ---
Olivia L2 HP:200 MP:110 Str:735 Dex:630 Agi:525 Gold:704 Weapon:Sword Armor:Platinum_Shield
Joseph L2 HP:200 MP:110 Str:788 Dex:525 Agi:473 Gold:2600 Weapon:Sword Armor:Platinum_Shield
Commands: W/A/S/D move, I inventory, R restart map, Q quit
d
-- A wild group of monsters appears! --
Battle started against 2 monsters!
Olivia [##############################] HP:200 [#################             ] MP:110
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
2
Choose spell to cast:
1) Snow_Cannon (ICE, DMG 650, MP 250)
1
Not enough MP to cast.
Olivia [##############################] HP:200 [#################             ] MP:110
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
4
Equip menu: W) Weapons, A) Armors, L) Leave
w
No weapons to equip.
Olivia [##############################] HP:200 [#################             ] MP:110
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Aasterinian#957 HP:200
2) Chronepsish#615 HP:200
1
-- Olivia attacked Aasterinian#957 for 74 dmg. --
Joseph [##############################] HP:200 [#################             ] MP:110
Choose action for Joseph: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Aasterinian#957 HP:126
2) Chronepsish#615 HP:200
1
-- Aasterinian#957 dodged the attack! --
-- Aasterinian#957 attacked Joseph for 46 dmg. --
-- Chronepsish#615 attacked Joseph for 53 dmg. --
Olivia [##############################] HP:200 [####################          ] MP:130
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Aasterinian#957 HP:126
2) Chronepsish#615 HP:200
1
-- Aasterinian#957 dodged the attack! --
Joseph [##################            ] HP:120 [####################          ] MP:130
Choose action for Joseph: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Aasterinian#957 HP:126
2) Chronepsish#615 HP:200
1
-- Joseph attacked Aasterinian#957 for 76 dmg. --
-- Aasterinian#957 attacked Olivia for 46 dmg. --
-- Joseph dodged Chronepsish#615's attack. --
Olivia [##########################    ] HP:174 [#######################       ] MP:150
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Aasterinian#957 HP:49
2) Chronepsish#615 HP:200
1
-- Aasterinian#957 dodged the attack! --
Joseph [#####################         ] HP:140 [#######################       ] MP:150
Choose action for Joseph: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Aasterinian#957 HP:49
2) Chronepsish#615 HP:200
1
-- Joseph attacked Aasterinian#957 for 76 dmg. --
-- Aasterinian#957 fainted! --
-- Chronepsish#615 attacked Joseph for 53 dmg. --
Olivia [############################# ] HP:194 [##########################    ] MP:170
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Chronepsish#615 HP:200
1
-- Olivia attacked Chronepsish#615 for 74 dmg. --
Joseph [################              ] HP:106 [##########################    ] MP:170
Choose action for Joseph: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Chronepsish#615 HP:126
1
-- Chronepsish#615 dodged the attack! --
-- Chronepsish#615 attacked Olivia for 53 dmg. --
Olivia [########################      ] HP:160 [############################# ] MP:190
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Chronepsish#615 HP:126
1
-- Chronepsish#615 dodged the attack! --
Joseph [###################           ] HP:126 [############################# ] MP:190
Choose action for Joseph: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Chronepsish#615 HP:126
1
-- Chronepsish#615 dodged the attack! --
-- Chronepsish#615 attacked Olivia for 53 dmg. --
Olivia [###################           ] HP:126 [##############################] MP:200
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Chronepsish#615 HP:126
1
-- Chronepsish#615 dodged the attack! --
Joseph [######################        ] HP:146 [##############################] MP:200
Choose action for Joseph: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Chronepsish#615 HP:126
1
-- Joseph attacked Chronepsish#615 for 76 dmg. --
-- Chronepsish#615 attacked Joseph for 53 dmg. --
Olivia [######################        ] HP:146 [##############################] MP:200
Choose action for Olivia: 1)Attack 2)Cast Spell 3)Use Potion 4)Equip 5)Inspect
1
Choose target:
1) Chronepsish#615 HP:49
1
-- Olivia attacked Chronepsish#615 for 74 dmg. --
-- Chronepsish#615 fainted! --
All monsters defeated!
Battle won. Continuing...
 M  .  M  M  #  M  P  .
 M  .  #  .  #  .  .  .
 .  .  .  .  M  .  #  .
 .  M  #  .  M  .  M  .
 M  #  .  .  .  .  .  .
 .  #  M  .  .  M  .  .

--- Party Info ---
Olivia L2 HP:147 MP:200 Str:735 Dex:630 Agi:525 Gold:804 Weapon:Sword Armor:Platinum_Shield
Joseph L2 HP:113 MP:200 Str:788 Dex:525 Agi:473 Gold:2700 Weapon:Sword Armor:Platinum_Shield
Commands: W/A/S/D move, I inventory, R restart map, Q quit
i
--- Inventory Management ---
Hero: Olivia
1) View Inventory 2) Equip 3) Use Potion 4) Skip
1
Items:
1) Snow_Cannon (ICE, DMG 650, MP 250)
2) Healing_Potion (Potion +100 to [Health])
Hero: Joseph
1) View Inventory 2) Equip 3) Use Potion 4) Skip
4
 M  .  M  M  #  M  P  .
 M  .  #  .  #  .  .  .
 .  .  .  .  M  .  #  .
 .  M  #  .  M  .  M  .
 M  #  .  .  .  .  .  .
 .  #  M  .  .  M  .  .

--- Party Info ---
Olivia L2 HP:147 MP:200 Str:735 Dex:630 Agi:525 Gold:804 Weapon:Sword Armor:Platinum_Shield
Joseph L2 HP:113 MP:200 Str:788 Dex:525 Agi:473 Gold:2700 Weapon:Sword Armor:Platinum_Shield
Commands: W/A/S/D move, I inventory, R restart map, Q quit
i
--- Inventory Management ---
Hero: Olivia
1) View Inventory 2) Equip 3) Use Potion 4) Skip
3
Potions:
1) Healing_Potion (Potion +100 to [Health])
Pick potion number (or enter):
1
Used potion: Healing_Potion
Hero: Joseph
1) View Inventory 2) Equip 3) Use Potion 4) Skip
4
 M  .  M  M  #  M  P  .
 M  .  #  .  #  .  .  .
 .  .  .  .  M  .  #  .
 .  M  #  .  M  .  M  .
 M  #  .  .  .  .  .  .
 .  #  M  .  .  M  .  .

--- Party Info ---
Olivia L2 HP:147 MP:200 Str:735 Dex:630 Agi:525 Gold:804 Weapon:Sword Armor:Platinum_Shield
Joseph L2 HP:113 MP:200 Str:788 Dex:525 Agi:473 Gold:2700 Weapon:Sword Armor:Platinum_Shield
Commands: W/A/S/D move, I inventory, R restart map, Q quit
q
Quitting game.
(base) PS C:\Users\17815\Desktop\CS611\5Assignment> 

```


