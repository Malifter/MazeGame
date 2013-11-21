package game;
import java.util.ArrayList;
import java.util.List;

import items.NotConsumable;

public class Inventory {
    private List<NotConsumable> itemList = new ArrayList<NotConsumable>();
    private Player player;
    public Inventory(Player p){
        this.player = p;
    }
    
    public void addItem(NotConsumable item){
        itemList.add(item);
    }
    
    public void removeItem(NotConsumable item){
        
    }
    
    public List<NotConsumable> getItem(){
        return itemList;
    }
    
    public Player getPlayer(){
        return player;//use this to display player's inventory information
    }
}
