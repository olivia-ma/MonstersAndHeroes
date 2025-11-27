package inventory;

// manages item collections with business logic
// uses generic type method for filtering(findAllOfType()) and supports equipment tracking
// implements composite pattern for item management

import inventory.items.Item;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<Item> items = new ArrayList<Item>();

    public void addItem(Item i) {
        if (i != null){
            items.add(i);
        }
    }

    public boolean removeItem(Item i) {
        return items.remove(i);
    }

    public List<Item> getAll() {
        return new ArrayList<Item>(items);
    }

    public <T extends Item> T findFirstOfType(Class<T> cls) {
        for (Item it : items) {
            if (cls.isInstance(it)){
                return cls.cast(it);
            }
        }

        return null;
    }

    public <T extends Item> List<T> findAllOfType(Class<T> cls) {
        List<T> res = new ArrayList<T>();
        for (Item it : items) {
            if (cls.isInstance(it)){
                res.add(cls.cast(it));
            }
        }
        
        return res;
    }
}
