package inventory;

import inventory.items.Item; import java.util.*;
public class Inventory { private List<Item> items = new ArrayList<>(); public void addItem(Item i)
{ items.add(i); } public boolean remove(Item i){ return items.remove(i); } public <T extends Item> T
findFirstByType(Class<T> cls){ for(Item it: items) if (cls.isInstance(it)) return cls.cast(it); return null; } public
List<Item> getAll(){ return new ArrayList<>(items); } }
