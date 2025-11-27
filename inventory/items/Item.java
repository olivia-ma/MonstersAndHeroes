package inventory.items;

// abstract base class for all items using template method pattern
// each subclass implements type specfiic behavior

public abstract class Item {
    protected String name;
    protected int cost;
    protected int requiredLevel;

    public Item(String name, int cost, int requiredLevel) {
        this.name = name;
        this.cost = cost;
        this.requiredLevel = requiredLevel;
    }

    public String getName() { 
        return name; 
    }

    public int getCost() { 
        return cost; 
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public int getSellValue() {
        return (int)(cost * 0.5);
    }

    @Override
    public String toString() {
        return name + " (lvl " + requiredLevel + ", cost " + cost + ")";
    }
}
