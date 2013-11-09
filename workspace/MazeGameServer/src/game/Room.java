package game;

/*
* Classname:            Room.java
*
* Version information:  1.0
*
* Date:                 11/3/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import java.util.ArrayList;

import engine.Position;

public class Room {
    private static final int OFFSET_X = 120;
    private static final int OFFSET_Y = 72;
    public static final int HEIGHT = 144;
    public static final int WIDTH = 240;
    private Position<Integer, Integer> center;
    private Position<Integer, Integer> location;
    private ArrayList<Entity> enemies = new ArrayList<Entity>();;
    //ArrayList<Entity> spawnLocations;
    private ArrayList<Entity> foreground = new ArrayList<Entity>();;
    private ArrayList<Entity> background = new ArrayList<Entity>();;
    private ArrayList<Entity> traps = new ArrayList<Entity>();;
    private ArrayList<Entity> players = new ArrayList<Entity>();
    ArrayList<Door> doors = new ArrayList<Door>();
    ArrayList<Portal> portals = new ArrayList<Portal>();
    ArrayList<GateKeeper> gatekeepers = new ArrayList<GateKeeper>();
    
    public Room(Position<Integer, Integer> location) {
        this.location = location;
        this.center = new Position<Integer, Integer>(location.getX() + OFFSET_X, location.getY() + OFFSET_Y);
    }
    
    public void addToForeground(Entity tile) {
        foreground.add(tile);
    }
    
    public void addToBackground(Entity tile) {
        background.add(tile);
    }
    
    public void addTrap(Entity trap) {
        traps.add(trap);
    }
    
    public void addPlayer(Entity player) {
        players.add(player);
    }
    
    public void removePlayer(Entity player) {
        players.remove(player);
    }
    
    public void addEnemy(Entity enemy) {
        enemies.add(enemy);
    }
    
    public void removeEnemy(Entity enemy) {
        enemies.remove(enemy);
    }
    
    public void addDoor(Door door) {
        doors.add(door);
    }
    
    public int numPlayers() {
        return players.size();
    }
    
    public ArrayList<Entity> getPlayers() {
        return players;
    }
    
    public ArrayList<Entity> getEnemies() {
        return enemies;
    }
    
    public ArrayList<Entity> getTraps() {
        return traps;
    }
    
    public ArrayList<Entity> getBackground() {
        return background;
    }
    
    public ArrayList<Entity> getForeground() {
        return foreground;
    }
    
    public Position<Integer, Integer> getLocation() {
        return location;
    }
    
    public Position<Integer, Integer> getCenter() {
        return center;
    }
    
    public ArrayList<Door> getDoors() {
        return doors;
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
    
}
