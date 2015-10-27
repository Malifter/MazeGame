package game.environment;

/*
* Classname:            Exterior.java
*
* Version information:  1.0
*
* Date:                 11/12/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import engine.Vector2f;
import engine.Vector2i;
import engine.physics.Collisions;
import engine.serializable.SerializeFactory;
import engine.serializable.SerializedObject;
import game.MazeGameServer;
import game.entities.effects.Effect;
import game.entities.environment.Entry;
import game.entities.environment.Explosion;
import game.entities.environment.Obstacle;
import game.entities.environment.Tile;
import game.entities.npcs.Neutral;
import game.entities.npcs.Player;
import game.entities.projectiles.Projectile;
import game.enums.GameState;
import game.enums.Sound;

public class Exterior extends Room{
    public static final int HEIGHT = 528;
    public static final int WIDTH = 720;
    public static final int SPAWN_RADIUS = 50;
    private ArrayList<Vector2i> playerSpawns = new ArrayList<Vector2i>(); // for now make these the safe zones too possibly
    
    public Exterior(int layout) {
        super(layout);
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
                
                if(player.hasFollower()) {
                    if(Collisions.findDistance(new Vector2f(playerSpawns.get(player.getPlayerID())), player.getFollower().getRigidBody().getLocation()) < SPAWN_RADIUS ) {
                        MazeGameServer.hostageSaved(player.getPlayerID());
                        //removeHostage(player.getHostage()); For now only 1 hostage
                    }
                    // In the game update if there's no more hostages to save then player with highest score is winner!
                    // If in single player mode, then level is cleared when all hostages are saved (still get score) same thing
                    // If you lose all lives before game is over then you get -score for that but still +score for each hostage saved
                    // person with highest score wins regardless if they went out
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
            // players
            for(Player player: players) {
                if(player.getRigidBody().isEnabled()) {
                    // other players
                    for(Player other: players) {
                        if(!player.equals(other) && other.getRigidBody().isEnabled()) {
                            Collisions.detectAndApplyEqualRadialCorrection(player, other);
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
                    // entries
                    for(Entry entry: entries) {
                        if(entry.getRigidBody().isEnabled()) { // if this is true, it is either a locked/disguised door, or a deactivated portal
                            if(Collisions.detectAndApplySingleCorrection(player, entry)) {
                                entry.interact(player);
                            }
                        }
                    }
                    // obstacles
                    for(Obstacle obstacle: obstacles) {
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
            // neutrals
            for(Neutral neutral: neutrals) {
                if(neutral.getRigidBody().isEnabled()) {
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
                    for(Explosion explosion: explosions) {
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
                // projectiles
                for(Projectile projectile: projectiles) {
                    //if(projectile.isEnabled()) {
                        updates.add(projectile.serialize());
                    //}
                }
                // neutrals
                for(Neutral neutral: neutrals) {
                    if(neutral.isEnabled()) {
                        updates.add(neutral.serialize());
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
    
    public void addPlayerSpawn(Vector2i location) {
        playerSpawns.add(location);
    }
    
    public ArrayList<Vector2i> getPlayerSpawns() {
        return playerSpawns;
    }
}
