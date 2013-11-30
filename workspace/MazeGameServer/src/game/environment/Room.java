package game.environment;

/*
* Classname:            Room.java
*
* Version information:  1.0
*
* Date:                 11/3/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import game.entities.Entity;
import game.entities.environment.Entry;
import game.entities.environment.Tile;
import game.entities.npcs.Player;
import java.util.ArrayList;
import engine.serializable.SerializedObject;
import engine.serializable.SerializedRoom;

public class Room {
    private ArrayList<Tile> foreground = new ArrayList<Tile>();
    //private ArrayList<Entity> background = new ArrayList<Entity>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Entry> entries = new ArrayList<Entry>();
    
    public final int layout; // temporary
    
    public Room(int layout) {
        this.layout = layout;
    }
    
    public void addToForeground(Tile tile) {
        foreground.add(tile);
    }
    /*public void addToBackground(Entity tile) {
        background.add(tile);
    }*/
    
    public void addPlayer(Player player) {
        players.add(player);
    }
    
    public void removePlayer(Entity player) {
        players.remove(player);
    }
    
    public void addEntry(Entry entry) {
        entries.add(entry);
    }
    
    public int numPlayers() {
        return players.size();
    }
    
    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    /*public ArrayList<Entity> getBackground() {
        return background;
    }*/
    
    public ArrayList<Tile> getForeground() {
        return foreground;
    }
    
    public ArrayList<Entry> getEntries() {
        return entries;
    }
    
    public SerializedObject serialize(int index) {
        return new SerializedRoom(null, index);
    }
}
