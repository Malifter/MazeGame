package game.environment;

/*
* Classname:            Interior.java
*
* Version information:  1.0
*
* Date:                 11/15/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import game.entities.environment.Obstacle;
import game.entities.items.*;
import game.entities.npcs.GateKeeper;
import game.entities.npcs.Hostile;
import java.util.ArrayList;
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
    private ArrayList<Hostile> enemies = new ArrayList<Hostile>();
    private ArrayList<Obstacle> traps = new ArrayList<Obstacle>();
    private ArrayList<GateKeeper> gatekeepers = new ArrayList<GateKeeper>(); // Change to list of Negotiators (encompasses hostages/gatekeepers)
    private ArrayList<Item> items = new ArrayList<Item>();
    
    public Interior(Vertex2 location, int layout) {
        super(layout);
        this.location = location;
        this.center = new Vertex2(location.x + OFFSET_X, location.y + OFFSET_Y);
    }
    
    public Vertex2 getLocation() {
        return location;
    }
    
    public Vertex2 getCenter() {
        return center;
    }
    
    public void addTrap(Obstacle trap) {
        traps.add(trap);
    }
    
    public ArrayList<Obstacle> getTraps() {
        return traps;
    }
    
    public void addEnemy(Hostile enemy) {
        enemies.add(enemy);
    }
    
    public void removeEnemy(Hostile enemy) {
        enemies.remove(enemy);
    }
    
    public ArrayList<Hostile> getEnemies() {
        return enemies;
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
}
