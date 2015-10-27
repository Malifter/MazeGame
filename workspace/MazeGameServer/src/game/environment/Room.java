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
import game.entities.effects.Effect;
import game.entities.environment.Entry;
import game.entities.environment.Explosion;
import game.entities.environment.Obstacle;
import game.entities.environment.Tile;
import game.entities.npcs.Neutral;
import game.entities.npcs.Player;
import game.entities.projectiles.Projectile;
import game.enums.Sound;

import java.util.ArrayList;
import engine.serializable.SerializedObject;
import engine.serializable.SerializedRoom;

public class Room {
    protected ArrayList<Tile> foreground = new ArrayList<Tile>();
    protected ArrayList<Player> players = new ArrayList<Player>();
    protected ArrayList<Entry> entries = new ArrayList<Entry>();
    protected ArrayList<Neutral> neutrals = new ArrayList<Neutral>();
    protected ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    protected ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    protected ArrayList<Explosion> explosions = new ArrayList<Explosion>();
    protected ArrayList<Effect> effects = new ArrayList<Effect>();
    protected ArrayList<Sound> sounds = new ArrayList<Sound>();
    protected int roomID = 0;   
    public final int layout;
    
    public Room(int layout) {
        this.layout = layout;
    }
    
    public void update(long elapsedTime) {}
    
    public void applyCollisions() {}
    
    public void serialize() {}
    
    public void addToForeground(Tile tile) {
        foreground.add(tile);
    }
    
    public void addPlayer(Player player) {
        player.setRoom(this);
        players.add(player);
    }
    
    public void removePlayer(Entity player) {
        players.remove(player);
    }
    
    public int numPlayers() {
        return players.size();
    }
    
    public boolean hasPlayers() {
        return players.size() > 0;
    }
    
    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    public void addNeutral(Neutral neutral) {
        neutrals.add(neutral);
    }
    
    public void removeNeutral(Neutral neutral) {
        neutrals.remove(neutral);
    }
    
    public ArrayList<Neutral> getNeutrals() {
        return neutrals;
    }
    
    public void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }
    
    public void removeExplosion(Explosion explosion) {
        explosions.remove(explosion);
    }
    
    public ArrayList<Explosion> getExplosions() {
        return explosions;
    }
    
    public void addEffect(Effect effect) {
        effects.add(effect);
    }
    
    public void removeEffect(Effect effect) {
        effects.remove(effect);
    }
    
    public ArrayList<Effect> getEffects() {
        return effects;
    }
    
    public void addSound(Sound sound) {
        sounds.add(sound);
    }
    
    public void removeSound(Sound sound) {
        sounds.remove(sound);
    }
    
    public ArrayList<Sound> getSounds() {
        return sounds;
    }
    
    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }
    
    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }
    
    public void removeObstacle(Obstacle obstacle) {
        obstacles.remove(obstacle);
    }
    
    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }
    
    public void removeProjectile(Projectile projectile) {
        projectiles.remove(projectile);
    }
    
    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
    
    public void addEntry(Entry entry) {
        entries.add(entry);
    }
    
    public ArrayList<Tile> getForeground() {
        return foreground;
    }
    
    public ArrayList<Entry> getEntries() {
        return entries;
    }
    
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
    
    public int getRoomID() {
        return roomID;
    }
    
    public SerializedObject serialize(int index) {
        return new SerializedRoom(null, index);
    }
}
