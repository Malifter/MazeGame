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

import game.MazeGameServer;
import game.entities.EntityFactory;
import game.entities.environment.Entry;
import game.entities.environment.Obstacle;
import game.entities.environment.Tile;
import game.entities.items.*;
import game.entities.npcs.Hostage;
import game.entities.npcs.Hostile;
import game.entities.npcs.Neutral;
import game.entities.npcs.Player;
import game.entities.projectiles.Projectile;
import game.enums.ItemType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import engine.Vector2i;
import engine.Vector2f;
import engine.physics.Collisions;
import engine.serializable.SerializeFactory;
import engine.serializable.SerializedObject;


public class Interior extends Room {

    private static final int OFFSET_X = 120;
    private static final int OFFSET_Y = 72;
    public static final int HEIGHT = 144;
    public static final int WIDTH = 240;
    public static final int MAX_ENTRIES = 4;
    
    private Vector2i center;
    private Vector2i location;
    // private ArrayList<Vertex2> enemySpawns; // Only needed if enemies can respawn
    private ArrayList<Hostile> enemies = new ArrayList<Hostile>();
    private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    
    public Interior(Vector2i location, int layout) {
        super(layout);
        this.location = location;
        this.center = new Vector2i(location.x + OFFSET_X, location.y + OFFSET_Y);
    }
    
    @Override
    public void update(long elapsedTime) {
        if(hasPlayers()) {
            // players
            Iterator<Player> playerItr = players.iterator();
            while(playerItr.hasNext()) {
                Player player = playerItr.next();
                if(player.isEnabled()) {
                    player.update(elapsedTime);
                } else {
                    if(player.getLives() > 0) {
                        player.reset();
                        player.removeLife();
                        player.update(elapsedTime);
                    }
                    else {
                        // THIS PLAYER LOSES
                        playerItr.remove();
                        continue;
                    }
                }
                
                for(Entry entry: entries) {
                    if(entry.transport(player)) {
                        if(player.hasFollower()) {
                            this.removeNeutral(player.getFollower());
                            player.getRoom().addNeutral(player.getFollower());
                            player.getFollower().getRigidBody().setLocation(player.getRigidBody().getLocation());
                        }
                        playerItr.remove();
                        continue;
                    }
                }
            }
            
            // hostile
            Iterator<Hostile> hostileItr = enemies.iterator();
            while(hostileItr.hasNext()) {
                Hostile enemy = hostileItr.next();
                if(enemy.isEnabled()) {
                    enemy.update(elapsedTime);
                } else {
                    if(enemies.size() == 1) {
                        addItem(EntityFactory.createItem(new Vector2f(center), ItemType.randomItem()));
                    }
                    hostileItr.remove();
                    continue;
                }
            }
            
            // neutrals
            Iterator<Neutral> neutralItr = neutrals.iterator();
            while(neutralItr.hasNext()) {
                Neutral neutral = neutralItr.next();
                if(neutral.isEnabled()) {
                    neutral.update(elapsedTime);
                } else {
                    neutralItr.remove();
                    continue;
                }
            }
            
            // projectiles
            Iterator<Projectile> projectileItr = projectiles.iterator();
            while(projectileItr.hasNext()) {
                Projectile projectile = projectileItr.next();
                if(projectile.isEnabled()) {
                    projectile.update(elapsedTime);
                } else {
                    projectileItr.remove();
                    continue;
                }
            }
            
            // items
            Iterator<Item> itemItr = items.iterator();
            while(itemItr.hasNext()) {
                Item item = itemItr.next();
                if(item.isEnabled()) {
                    item.update(elapsedTime);
                } else {
                    itemItr.remove();
                    continue;
                }
            }
            
            // obstacles
            Iterator<Obstacle> obstacleItr = obstacles.iterator();
            while(obstacleItr.hasNext()) {
                Obstacle obstacle = obstacleItr.next();
                if(!obstacle.isEnabled()) {
                    obstacleItr.remove();
                    continue;
                }
            }
        }
    }
    
    @Override
    public void applyCollisions() {
        if(hasPlayers()) {
            // COLLISIONS WITH ENTITES
            // player
            for(Player player: players) {
                if(player.getRigidBody().isEnabled()) {
                    // other players
                    for(Player other: players) {
                        if(!player.equals(other) && other.getRigidBody().isEnabled()) {
                            Collisions.detectAndApplyEqualRadialCorrection(player, other);
                        }
                    }
                    // enemies
                    for(Hostile enemy: enemies) {
                        if(enemy.getRigidBody().isEnabled() && Collisions.detectCollision(player, enemy)) {
                            Collisions.applyEqualRadialCorrection(player, enemy);
                            enemy.attack(player);
                        }
                    }
                    // projectiles
                    for(Projectile projectile: projectiles) {
                        if(projectile.getRigidBody().isEnabled() && projectile.dangerousTo(player) && Collisions.detectCollision(player, projectile)) {
                            projectile.collide(player);
                        }
                    }
                    // neutrals
                    for(Neutral neutral: neutrals) {
                        if(neutral.getRigidBody().isEnabled() && Collisions.detectCollision(player, neutral)) {
                            Collisions.applyEqualRadialCorrection(player, neutral);
                            neutral.interact(player);
                        }
                    }
                    // items
                    Iterator<Item> itemItr = items.iterator();
                    while(itemItr.hasNext()) {
                        Item item = itemItr.next();
                        if(item.getRigidBody().isEnabled() && Collisions.detectCollision(player, item)) {
                            item.pickUp(player);
                            //itemItr.remove();
                        }
                    }
                    // obstacles
                    for(Obstacle obstacle: obstacles) {
                        if(obstacle.getRigidBody().isEnabled() && (obstacle.isDangerous() || obstacle.isBlocking()) && Collisions.detectCollision(player, obstacle)) {
                            if(obstacle.isDangerous()) {
                                obstacle.collide(player);
                            } else if(obstacle.isOpenable()) {
                                obstacle.interact(player);
                            }
                            if(obstacle.isBlocking()) {
                                Collisions.applySingleCorrection(player, obstacle);
                            }
                        }
                    }
                    // entries
                    for(Entry entry: entries) {
                        if(entry.getRigidBody().isEnabled()) {
                            Collisions.detectAndApplySingleCorrection(player, entry);
                        }
                    }
                }
            }
            // hostile
            for(Hostile enemy: enemies) {
                if(enemy.getRigidBody().isEnabled()) {
                    // other enemies
                    for(Hostile other: enemies) {
                        if(!enemy.equals(other) && other.getRigidBody().isEnabled()) {
                            Collisions.detectAndApplyEqualRadialCorrection(enemy, other);
                        }
                    }
                    // projectiles
                    for(Projectile projectile: projectiles) {
                        if(projectile.getRigidBody().isEnabled() && projectile.dangerousTo(enemy) &&
                                projectile.ownedByPlayer() && Collisions.detectCollision(enemy, projectile)) {
                            projectile.collide(enemy);
                        }
                    }
                    // items
                    for(Item item: items) {
                        if(item.getRigidBody().isEnabled()) {
                            Collisions.detectAndApplySingleRadialCorrection(item, enemy);
                        }
                    }
                    // obstacles
                    for(Obstacle obstacle: obstacles) {
                        if(obstacle.getRigidBody().isEnabled() && (obstacle.isDangerous() || obstacle.isBlocking()) && Collisions.detectCollision(enemy, obstacle)) {
                            if(obstacle.isDangerous()) {
                                obstacle.collide(enemy);
                            }
                            if(obstacle.isBlocking()) {
                                Collisions.applySingleCorrection(enemy, obstacle);
                            }
                        }
                    }
                    // entries
                    for(Entry entry: entries) {
                        Collisions.detectAndApplySingleCorrection(enemy, entry);
                    }
                }
            }
            // obstacles
            for(Obstacle obstacle: obstacles) {
                if(obstacle.getRigidBody().isEnabled() && obstacle.isBlocking()) {
                    // projectiles
                    for(Projectile projectile: projectiles) {
                        if(projectile.getRigidBody().isEnabled() && Collisions.detectCollision(obstacle, projectile)) {
                            projectile.collide(obstacle);
                        }
                    }
                    // neutrals
                    for(Neutral neutral: neutrals) {
                        if(neutral.getRigidBody().isEnabled()) {
                            Collisions.detectAndApplySingleCorrection(neutral, obstacle);
                        }
                    }
                    // items
                    for(Item item: items) {
                        if(item.getRigidBody().isEnabled()) {
                            Collisions.detectAndApplySingleCorrection(item, obstacle);
                        }
                    }
                }
            }
            // neutrals
            for(Neutral neutral: neutrals) {
                if(neutral.getRigidBody().isEnabled()) {
                    // items
                    for(Item item: items) {
                        if(item.getRigidBody().isEnabled()) {
                            Collisions.detectAndApplySingleRadialCorrection(item, neutral);
                        }
                    }
                    // entries
                    for(Entry entry: entries) {
                        Collisions.detectAndApplySingleCorrection(neutral, entry);
                    }
                }
            }
            // items
            for(Item item: items) {
                if(item.getRigidBody().isEnabled()) {
                    // entries
                    for(Entry entry: entries) {
                        Collisions.detectAndApplySingleCorrection(item, entry);
                    }
                }
            }
            // COLLISIONS WITH ENVIRONMENT
            // players
            for(Player player: players) {
                if(player.getRigidBody().isEnabled()) {
                    Collisions.applyEnvironmentCorrections(player, foreground);
                }
            }
            // hostile
            for(Hostile enemy: enemies) {
                if(enemy.getRigidBody().isEnabled()) {
                    Collisions.applyEnvironmentCorrections(enemy, foreground);
                }
            }
            // projectiles
            for(Projectile projectile: projectiles) {
                if(projectile.getRigidBody().isEnabled()) {
                    for(Tile tile: foreground) {
                        if(tile.getRigidBody().isEnabled() && Collisions.detectCollision(projectile, tile)) {
                            projectile.collide();
                        }
                    }
                }
            }
            // neutrals
            for(Neutral neutral: neutrals) {
                if(neutral.getRigidBody().isEnabled()) {
                    Collisions.applyEnvironmentCorrections(neutral, foreground);
                }
            }
            // items
            for(Item item: items) {
                if(item.getRigidBody().isEnabled()) {
                    Collisions.applyEnvironmentCorrections(item, foreground);
                }
            }
        }
    }
    
    @Override
    public void serialize() {
        if(hasPlayers()) {
            for(Player player: players) {
                List<SerializedObject> updates = MazeGameServer.updates.get(player.getPlayerID());
                // player
                updates.add(SerializeFactory.serialize(player));
                // room
                updates.add(SerializeFactory.serialize(this));
                // other players
                for(Player other: players) {
                    if(!other.equals(player) && other.isEnabled()) {
                        updates.add(other.serialize());
                    }
                }
                // hostile
                for(Hostile enemy: enemies) {
                    if(enemy.isEnabled()) {
                        updates.add(enemy.serialize());
                    }
                }
                // obstacles
                for(Obstacle obstacle: obstacles) {
                    if(obstacle.isEnabled()) {
                        updates.add(obstacle.serialize());
                    }
                }
                // projectiles
                for(Projectile projectile: projectiles) {
                    if(projectile.isEnabled()) {
                        updates.add(projectile.serialize());
                    }
                }
                // neutrals
                for(Neutral neutral: neutrals) {
                    if(neutral.isEnabled()) {
                        updates.add(neutral.serialize());
                    }
                }
                // items
                for(Item item: items) {
                    if(item.isEnabled()) {
                        updates.add(item.serialize());
                    }
                }
                // entry
                for(Entry entry: entries) {
                    if(entry.isEnabled()) {
                        updates.add(entry.serialize());
                    }
                }
            }
        }
    }
    
    public Vector2i getLocation() {
        return location;
    }
    
    public Vector2i getCenter() {
        return center;
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
    
    public void addEnemy(Hostile enemy) {
        enemies.add(enemy);
    }
    
    public void removeEnemy(Hostile enemy) {
        enemies.remove(enemy);
    }
    
    public ArrayList<Hostile> getEnemies() {
        return enemies;
    }
}
