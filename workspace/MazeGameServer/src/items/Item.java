package items;

import game.Entity;
import game.Game;
import game.MazeGameServer;
import game.Player;

/*
* Classname:            Item.java
*
* Version information:  1.0
*
* Date:                 11/17/2013
*
* Copyright notice:     Copyright (c) 2013 Lizhu Ma
*/

public abstract class Item extends Entity{

    MazeGameServer game;
    private int imageIndex = 0;
    private int itemId = 0;
    private String name = null;
    private Player player;
    
    //public enum ItemType {HEALTHBOOSTER, WEAPON, KEY};
    //public ItemType type;
    public boolean isDestroyed = false;
    

    public Item(Game g, String anImage, int x, int y, float w, float h, int itemId, String name, Player player) {
        super(g, anImage, x, y, w, h);
        game = (MazeGameServer) g;
        imageIndex = imageIndex + 10; 
        image = null;
        minX = x;
        minY = y;
        width = w;
        height = h;
        
        calculateBounds();
        
        this.itemId  = itemId;
        this.name  = name;
        this.player = player;

    }

    public int getId() { 
        return itemId;
    }
    
    public String getName() { 
        return name;
    }
    
}
    
    
    



