package game;

/*
* Classname:            Interior.java
*
* Version information:  1.0
*
* Date:                 11/15/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import items.*;

import java.util.ArrayList;
import java.util.Random;

import engine.Vertex2;


public class Interior extends Room {

    private static final int OFFSET_X = 120;
    private static final int OFFSET_Y = 72;
    public static final int HEIGHT = 144;
    public static final int WIDTH = 240;
    public static final int MAX_ENTRIES = 4;
    
    private Vertex2 center;
    private Vertex2 location;
    private Vertex2 portalExit;
    // private ArrayList<Vertex2> enemySpawns; // Only needed if enemies can respawn
    private ArrayList<Entity> enemies = new ArrayList<Entity>();
    private ArrayList<Entity> traps = new ArrayList<Entity>();
    private ArrayList<Portal> portals = new ArrayList<Portal>();
    private ArrayList<GateKeeper> gatekeepers = new ArrayList<GateKeeper>(); // Change to list of Negotiators (encompasses hostages/gatekeepers)
    private ArrayList<Item> items = new ArrayList<Item>();
    
    public Interior(Vertex2 location, int layout) {
        super(layout);
        this.location = location;
        this.center = new Vertex2(location.getX() + OFFSET_X, location.getY() + OFFSET_Y);
    }
    
    public static enum ItemType {
        BOMB("items/bomb/"),
        GOLD("items/gold/"),
        DKEY("items/dkey/"),
        CKEY("items/ckey/"),
        TOOL("items/tool/"),
        LIFE("items/elife/"),
        BOOSTER("items/booster/"),
        SHIELD("items/shield/");
        
        private final String path;
        private ItemType(String path) {
            this.path = path;
        }
        public String getPath() {
            return path;
        }
        private static final ItemType[] VALUES = values();
        private static final int SIZE = VALUES.length;
        private static final Random RANDOM = new Random();
        public static ItemType randomItem() {
            return VALUES[RANDOM.nextInt(SIZE)];
        }
    };
    
    public Vertex2 getLocation() {
        return location;
    }
    
    public Vertex2 getCenter() {
        return center;
    }
    
    public void addTrap(Entity trap) {
        traps.add(trap);
    }
    
    public ArrayList<Entity> getTraps() {
        return traps;
    }
    
    public void addEnemy(Entity enemy) {
        enemies.add(enemy);
    }
    
    public void removeEnemy(Entity enemy) {
        enemies.remove(enemy);
    }
    
    public ArrayList<Entity> getEnemies() {
        return enemies;
    }
    
    public void addPortals(Portal portal) {
        portals.add(portal);
        
    }
    
    public ArrayList<Portal> getPortals() {
        return portals;
    }
    
    public void addGateKeeper(GateKeeper gatekeeper) {
        gatekeepers.add(gatekeeper);
    }
    
    public ArrayList<GateKeeper> getGateKeepers() {
        return gatekeepers;
    }
    
    public void setPortalExit(Vertex2 portalExit) {
        this.portalExit = portalExit;
    }
    
    public Vertex2 getPortalExit() {
        return portalExit;
    }
    

    public void addItem(Item item) {
        items.add(item);
    }
    
    public void removeItem(Item item) {
        items.remove(item);
    }
    
    public ArrayList<Item> getItems() {
        return items;
    }
    
    public void generateRandomItems(Game game, int x, int y){
        if(ItemType.randomItem().equals(ItemType.BOMB)) {
            System.out.println("BOMB ADDED");
            this.addItem(new Bomb(game, x, y, x+1, y+2));
        } else if(ItemType.randomItem().equals(ItemType.GOLD)) {
            System.out.println("GOLD ADDED");
            this.addItem(new Gold(game, x, y, x+1, y+2));
        } else if(ItemType.randomItem().equals(ItemType.DKEY)) {
            System.out.println("DKEY ADDED");
            this.addItem(new DoorKey(game, x, y, x+1, y+2));
        }
        else if(ItemType.randomItem().equals(ItemType.TOOL)) {
            System.out.println("TOOL ADDED");
            this.addItem(new DisguiseTool(game, x, y, x+1, y+2));
        } else if(ItemType.randomItem().equals(ItemType.LIFE)) {
            System.out.println("LIFE ADDED");
            this.addItem(new ExtraLife(game, x, y, x+1, y+2));
        }
        else if(ItemType.randomItem().equals(ItemType.BOOSTER)) {
            System.out.println("BOOSTER ADDED");
            this.addItem(new HealthBooster(game, x, y, x+1, y+2));
        }
        else if(ItemType.randomItem().equals(ItemType.SHIELD)) {
            System.out.println("SHIELD ADDED");
            this.addItem(new Shield(game, x, y, x+1, y+2));
        }
        
    }
}
