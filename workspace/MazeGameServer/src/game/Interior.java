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
    private ArrayList<Entity> enemies = new ArrayList<Entity>();
    private ArrayList<Entity> traps = new ArrayList<Entity>();
    private ArrayList<Portal> portals = new ArrayList<Portal>();
    private ArrayList<GateKeeper> gatekeepers = new ArrayList<GateKeeper>(); // Change to list of Negotiators (encompasses hostages/gatekeepers)
    
    public Interior(Vertex2 location, int layout) {
        super(layout);
        this.location = location;
        this.center = new Vertex2(location.getX() + OFFSET_X, location.getY() + OFFSET_Y);
    }
    
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
}