package game;
import java.util.HashMap;

import engine.physics.Collisions;
import game.entities.EntityFactory;
import game.entities.environment.Door;
import game.entities.environment.Entry;
import game.entities.npcs.Player;
import game.enums.ItemType;
import game.environment.Interior;
import game.environment.Room;

public class Inventory {
    //private final ItemType[] items = {ItemType.BOMB, ItemType.CKEY, ItemType.DKEY, ItemType.GOLD, ItemType.SHIELD, ItemType.TOOL};
    private final HashMap<ItemType, Integer> items = new HashMap<ItemType, Integer>();
    private int selectedIndex = 0;
    private ItemType selectedItem = ItemType.getByIndex(selectedIndex);
    
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
   
    public void selectNextItem(){
        ItemType item;
        do {
            selectedIndex++;
            selectedIndex = (selectedIndex) % ItemType.getSize();
            selectedItem = ItemType.getByIndex(Math.abs(selectedIndex));

        } while(selectedItem.getIndex() == -1);
        System.out.println("Item "+selectedItem.name());
    }
    
    public void selectPrevItem(){
        ItemType item;
        do {
            selectedIndex--;
            selectedIndex = (selectedIndex) % ItemType.getSize();
            selectedItem = ItemType.getByIndex(Math.abs(selectedIndex));
        } while(selectedItem.getIndex() == -1);
        System.out.println("Item "+selectedItem.name());
    }
    
    public void useSelectedItem(Player player, Room room){
        if(items.get(selectedItem)>0){
            if(selectedItem.equals(ItemType.BOMB)){
                items.put(selectedItem, items.get(selectedItem)-1);
                player.getRoom().addItem(EntityFactory.createItem(player.getRigidBody().getLocation(), ItemType.A_BOMB));
            }
            
            else if(selectedItem.equals(ItemType.TOOL)){//disguish a door
                for(Entry entry: room.getEntries()){
                    if(entry instanceof Door && Collisions.findDistance(player.getRigidBody(), entry.getRigidBody()) <= 50){
                        System.out.println("door disguished");
                        Door door = (Door) entry;
                        door.setDisguished(true);
                        removeItem(selectedItem);
                    }
                }
                
            }
        }
    }

    public boolean hasItem(ItemType item) {
        if(items.get(item)>0) {return true;}
        return false;
    }
}
