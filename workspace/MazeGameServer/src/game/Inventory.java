package game;
import java.util.ArrayList;
import java.util.List;

import items.NotConsumable;

public class Inventory {
    private ArrayList<NotConsumable> itemList = new ArrayList<NotConsumable>();
    private Player player;
    public Inventory(Player p){
        this.player = p;
    }
    
    public void addItem(NotConsumable item){
        System.out.println("Item added to player :" + getPlayer());
        itemList.add(item);
    }
    
    public void removeItem(NotConsumable item){
        itemList.remove(item);
    }
    
    public ArrayList<NotConsumable> getItem(){
        return itemList;
    }
    
    public Player getPlayer(){
        return player;//use this to display player's inventory information
    }
}
