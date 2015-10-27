package game;
import java.util.HashMap;

import engine.physics.Collisions;
import game.entities.EntityFactory;
import game.entities.environment.Door;
import game.entities.environment.Entry;
import game.entities.npcs.Player;
import game.enums.ItemType;
import game.environment.Room;

public class Inventory {
    //private final ItemType[] items = {ItemType.BOMB, ItemType.CKEY, ItemType.DKEY, ItemType.GOLD, ItemType.SHIELD, ItemType.TOOL};
    private final HashMap<ItemType, Integer> items = new HashMap<ItemType, Integer>();
    private int selectedIndex = 0;
    private ItemType selectedItem = ItemType.getByIndex(selectedIndex);
    
    public Inventory(){
        items.put(ItemType.BOMB, 0);
        items.put(ItemType.CKEY, 0);
        items.put(ItemType.DKEY, 1);
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
        do {
            selectedIndex++;
            selectedIndex = (selectedIndex) % ItemType.getSize();
            selectedItem = ItemType.getByIndex(Math.abs(selectedIndex));

        } while(selectedItem.getIndex() == -1);
        System.out.println("Item "+selectedItem.name() + " " + items.get(selectedItem));
    }
    
    public void selectPrevItem(){
        do {
            selectedIndex--;
            selectedIndex = (selectedIndex) % ItemType.getSize();
            selectedItem = ItemType.getByIndex(Math.abs(selectedIndex));
        } while(selectedItem.getIndex() == -1);
        System.out.println("Item "+selectedItem.name() + items.get(selectedItem));
    }
    
    public void useSelectedItem(Player player, Room room){
        System.out.println("use pressed");
        if(items.get(selectedItem)>0){
            if(selectedItem.equals(ItemType.BOMB)){
                items.put(selectedItem, items.get(selectedItem)-1);
                room.addObstacle(EntityFactory.createBomb(player, room));
            }
            else if(selectedItem.equals(ItemType.TOOL)||selectedItem.equals(ItemType.DKEY)){//disguise a door
                for(Entry entry: room.getEntries()){
                    if(entry instanceof Door && Collisions.findDistance(player.getRigidBody(), entry.getRigidBody()) <= 30){
                        Door door = (Door) entry;
                        // TODO: This needs a better system rather than checking everything about the door. Should be internal to the class.
                        if(selectedItem.equals(ItemType.TOOL) && door.isActive()) {
                            door.disguise(); 
                        } else if(selectedItem.equals(ItemType.TOOL) && !door.isActive()) {
                            door.reveal();
                        } else if(selectedItem.equals(ItemType.DKEY) && door.isActive()) {
                            door.lock();
                        } else if(selectedItem.equals(ItemType.DKEY) && !door.isActive()) {
                            door.unlock();
                        }
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
