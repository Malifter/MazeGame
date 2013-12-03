package game;
import java.util.HashMap;

import game.enums.ItemType;

public class Inventory {
    //private final ItemType[] items = {ItemType.BOMB, ItemType.CKEY, ItemType.DKEY, ItemType.GOLD, ItemType.SHIELD, ItemType.TOOL};
    private final HashMap<ItemType, Integer> items = new HashMap<ItemType, Integer>();
    public Inventory(){
        items.put(ItemType.BOMB, 0);
        items.put(ItemType.CKEY, 0);
        items.put(ItemType.DKEY, 0);
        items.put(ItemType.GOLD, 0);
        items.put(ItemType.SHIELD, 0);
        items.put(ItemType.TOOL, 0);
    }
    
    public void addItem(ItemType item){
        //exception
        items.put(item, items.get(item)+1);
    }
    
    public void removeItem(ItemType item){
        //exception handle
        items.put(item, items.get(item)-1);
    }
    
    public HashMap<ItemType, Integer> getItem(){
        return items;
    }

    public int getQuantity(ItemType item){
        return items.get(item);
    }

    public void setQuantity(ItemType item, int quantity){
        items.put(item, quantity);
    }
   //set quantity (ItemType, int)
}
