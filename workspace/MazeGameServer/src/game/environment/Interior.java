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
import game.entities.effects.Effect;
import game.entities.environment.Entry;
import game.entities.environment.Explosion;
import game.entities.environment.Obstacle;
import game.entities.environment.Tile;
import game.entities.items.*;
import game.entities.npcs.Hostile;
import game.entities.npcs.Neutral;
import game.entities.npcs.Player;
import game.entities.projectiles.Projectile;
import game.enums.GameState;
import game.enums.ItemType;
import game.enums.ObstacleType;
import game.enums.Sound;

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
    private ArrayList<Item> items = new ArrayList<Item>();
    
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
                        MazeGameServer.level.getExterior().addPlayer(player);
                        player.reset();
                        player.removeLife();
                        player.update(elapsedTime);
                        playerItr.remove();
                        continue;
                    }
                    else {
                        MazeGameServer.states.set(player.getPlayerID(), GameState.LOSE);
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
                        double rand = Math.random();
                        if(rand <= 0.6){
                            addItem(EntityFactory.createItem(new Vector2f(center), ItemType.randomItem()));
                        } else if(rand <= 0.9) {
                            System.out.println("spawn chest");
                            addObstacle(EntityFactory.createObstacle(new Vector2f(center), ObstacleType.CHEST, this));
                        } else {
                            addObstacle(EntityFactory.createBomb(new Vector2f(center), this));
                        }
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
                if(obstacle.isEnabled()) {
                    obstacle.update(elapsedTime);
                } else {
                    obstacleItr.remove();
                    continue;
                }
            }
            
            // explosions
            Iterator<Explosion> explosionItr = explosions.iterator();
            while(explosionItr.hasNext()) {
                Explosion explosion = explosionItr.next();
                if(explosion.isEnabled()) {
                    explosion.update(elapsedTime);
                } else {
                    explosionItr.remove();
                    continue;
                }
            }
            
            // effects
            Iterator<Effect> effectItr = effects.iterator();
            while(effectItr.hasNext()) {
                Effect effect = effectItr.next();
                if(effect.isEnabled()) {
                    effect.update(elapsedTime);
                } else {
                    effectItr.remove();
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
                        }
                    }
                    // obstacles
                    for(Obstacle obstacle: obstacles) {
                        // TODO: SOLVE THIS ISSUE OF CONCURRENCY MODIFICATION... NOT SURE WHAT IS CAUSING IT
                        // THE PLAYER PUSHED THE OBSTACLE INTO A ROCK
                        if(obstacle.getRigidBody().isEnabled() &&
                                (obstacle.isDangerous() || obstacle.isBlocking() || obstacle.isMoveable()) &&
                                Collisions.detectCollision(player, obstacle)) {
                            if(obstacle.isDangerous()) {
                                obstacle.collide(player);
                            } else if(obstacle.isOpenable()) {
                                obstacle.interact(player);
                            }
                            if(obstacle.isMoveable()) {
                                if(obstacle.isHeavy()) {
                                    Collisions.applyEqualRadialCorrection(player, obstacle);
                                } else {
                                    Collisions.applySingleRadialCorrection(obstacle, player);
                                }
                            } else if(obstacle.isBlocking()) {
                                Collisions.applySingleCorrection(player, obstacle);
                            }
                        }
                    }
                    // entries
                    for(Entry entry: entries) {
                        if(entry.getRigidBody().isEnabled()) { // if this is true, it is either a locked/disguised door, or a deactivated portal
                            if(Collisions.detectAndApplySingleCorrection(player, entry)) {
                                entry.interact(player);
                            }
                        }
                    }
                    // explosions
                    for(Explosion explosion: explosions) {
                        if(explosion.getRigidBody().isEnabled()) {
                            if(Collisions.detectAndApplySingleRadialCorrection(player, explosion)) {
                                player.takeDamage(explosion.getSource(), explosion.getExplosionDamage(player));
                            }
                        }
                    }
                }
            }
            // hostile
            for(Hostile enemy: enemies) {
                if(enemy.getRigidBody().isEnabled()) {
                    // projectiles
                    for(Projectile projectile: projectiles) {
                        if(projectile.getRigidBody().isEnabled() && projectile.dangerousTo(enemy) &&
                                projectile.ownedByPlayer() && Collisions.detectCollision(enemy, projectile)) {
                            projectile.collide(enemy);
                        }
                    }
                    // enemy can't fly
                    if(!enemy.isFlying()) {
                        // other enemies
                        for(Hostile other: enemies) {
                            if(!enemy.equals(other) && !other.isFlying() && other.getRigidBody().isEnabled()) {
                                Collisions.detectAndApplyEqualRadialCorrection(enemy, other);
                            }
                        }
                        // neutrals
                        for(Neutral neutral: neutrals) {
                            if(neutral.getRigidBody().isEnabled() && Collisions.detectCollision(enemy, neutral)) {
                                Collisions.applyEqualRadialCorrection(enemy, neutral);
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
                            if(obstacle.getRigidBody().isEnabled() &&
                                    (obstacle.isDangerous() || obstacle.isBlocking() || obstacle.isMoveable()) &&
                                    Collisions.detectCollision(enemy, obstacle)) {
                                if(obstacle.isDangerous()) {
                                    obstacle.collide(enemy);
                                }
                                if(obstacle.isMoveable()) {
                                    if(obstacle.isHeavy()) {
                                        Collisions.applyEqualRadialCorrection(enemy, obstacle);
                                    } else {
                                        Collisions.applySingleRadialCorrection(obstacle, enemy);
                                    }
                                } else if(obstacle.isBlocking()) {
                                    Collisions.applySingleCorrection(enemy, obstacle);
                                }
                            }
                        }
                    }
                    // enemy can fly
                    else {
                        // other enemies
                        for(Hostile other: enemies) {
                            if(!enemy.equals(other) && other.isFlying() && other.getRigidBody().isEnabled()) {
                                Collisions.detectAndApplyEqualRadialCorrection(enemy, other);
                            }
                        }
                    }
                    // entries
                    for(Entry entry: entries) {
                        Collisions.detectAndApplySingleCorrection(enemy, entry);
                    }
                    // explosions
                    for(Explosion explosion: explosions) {
                        if(explosion.getRigidBody().isEnabled()) {
                            if(Collisions.detectAndApplySingleCorrection(enemy, explosion)) {
                                enemy.takeDamage(explosion, explosion.getExplosionDamage(enemy));   
                            }
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
                    // obstacles
                    for(Obstacle obstacle: obstacles) {
                        if(obstacle.getRigidBody().isEnabled() &&
                                (obstacle.isBlocking() || obstacle.isMoveable()) &&
                                Collisions.detectCollision(neutral, obstacle)) {
                            if(obstacle.isMoveable()) {
                                if(obstacle.isHeavy()) {
                                    Collisions.applyEqualRadialCorrection(neutral, obstacle);
                                } else {
                                    Collisions.applySingleRadialCorrection(obstacle, neutral);
                                }
                            } else if(obstacle.isBlocking()) {
                                Collisions.applySingleCorrection(neutral, obstacle);
                            }
                        }
                    }
                    // explosions
                    for(Explosion explosion: explosions) {
                        if(explosion.getRigidBody().isEnabled()) {
                            Collisions.detectAndApplySingleCorrection(neutral, explosion);
                        }
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
                    // other items
                    for(Item other: items) {
                        if(!item.equals(other) && other.getRigidBody().isEnabled()) {
                            Collisions.detectAndApplyEqualRadialCorrection(item, other);
                        }
                    }
                    // obstacles
                    for(Obstacle obstacle: obstacles) {
                        if(obstacle.getRigidBody().isEnabled() &&
                                (obstacle.isBlocking() || obstacle.isMoveable()) &&
                                Collisions.detectCollision(item, obstacle)) {
                            if(obstacle.isMoveable()) {
                                if(!obstacle.isHeavy()) {
                                    Collisions.applyEqualRadialCorrection(item, obstacle);
                                } else {
                                    Collisions.applySingleRadialCorrection(item, obstacle);
                                }
                            } else if(obstacle.isBlocking()) {
                                Collisions.applySingleCorrection(item, obstacle);
                            }
                        }
                    }
                    // explosions
                    for(Explosion explosion: explosions) {
                        if(explosion.getRigidBody().isEnabled()) {
                            Collisions.detectAndApplySingleCorrection(item, explosion);
                        }
                    }
                }
            }
            // obstacles
            for(Obstacle obstacle: obstacles) {
                if(obstacle.getRigidBody().isEnabled() && obstacle.isMoveable()) {
                    // other obstacles
                    for(Obstacle other: obstacles) {
                        if(other.getRigidBody().isEnabled() &&
                                (other.isBlocking() || other.isMoveable()) && 
                                Collisions.detectCollision(other, obstacle)) {
                            if(other.isMoveable()) {
                                if(obstacle.isHeavy() == other.isHeavy()) {
                                    Collisions.applyEqualRadialCorrection(other, obstacle);
                                } else if(obstacle.isHeavy()) {
                                    Collisions.applySingleCorrection(other, obstacle);
                                } else {
                                    Collisions.applySingleCorrection(obstacle, other);
                                }
                            } else if(other.isBlocking()){
                                Collisions.applySingleCorrection(obstacle, other);
                            }
                        }
                    }
                    // entries
                    for(Entry entry: entries) {
                        Collisions.detectAndApplySingleCorrection(obstacle, entry);
                    }
                }
                // TODO: Change to else if, if we prefer for chests to block not projectiles too
                if(obstacle.getRigidBody().isEnabled() && obstacle.isBlocking()) {
                    // projectiles
                    for(Projectile projectile: projectiles) {
                        if(projectile.getRigidBody().isEnabled() && Collisions.detectCollision(obstacle, projectile)) {
                            projectile.collide(obstacle);
                            // TODO: Use the known velocity of the object (projectile or otherwise)
                            // And apply that to the hit object. We can start transferring force.
                        }
                    }
                }
                if (obstacle.getRigidBody().isEnabled() && obstacle.isDestructable()) {
                    // explosions
                    for(Explosion explosion: explosions) { // TODO: solve concurrent modification on this line
                        // TODO: The issue is because if a bomb sets off another bomb, it effectively adds to
                        // this explosion array that we're currently iterating through. therefore we need to
                        // find a different way, and/or make sure that if an explosion sets off an obstacle
                        // it gets delayed for a tick and added later
                        if(explosion.getRigidBody().isEnabled() && explosion.inRange(obstacle)) {
                            obstacle.destroy();
                        }
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
                    // environment
                    for(Tile tile: foreground) {
                        if(tile.getRigidBody().isEnabled() && Collisions.detectCollision(projectile, tile)) {
                            projectile.collide();
                        }
                    }
                    // entries
                    for(Entry entry: entries) {
                        if(entry.getRigidBody().isEnabled() && Collisions.detectCollision(projectile, entry)) {
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
            // obstacles
            for(Obstacle obstacle: obstacles) {
                if(obstacle.getRigidBody().isEnabled() && obstacle.isMoveable()) {
                    Collisions.applyEnvironmentCorrections(obstacle, foreground);
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
                // effects
                for(Effect effect: effects) {
                    if(effect.isEnabled()) {
                        updates.add(effect.serialize());
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
                // entries
                for(Entry entry: entries) {
                    if(entry.isEnabled()) {
                        updates.add(entry.serialize());
                    }
                }
                // sounds
                for(Sound sound: sounds) {
                    updates.add(sound.serialize());
                }
                sounds.clear();
            }
        }
    }
    
    public Vector2i getLocation() {
        return location;
    }
    
    public Vector2i getCenter() {
        return center;
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
