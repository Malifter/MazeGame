package game;

import java.util.ArrayList;
import java.util.List;
import engine.Vertex2f;
import engine.physics.*;
import engine.serializable.SerializedObject;
import engine.serializable.SerializedRoom;
import game.entities.Entity;
import game.entities.EntityFactory;
import game.entities.environment.Entry;
import game.entities.environment.Obstacle;
import game.entities.npcs.GateKeeper;
import game.entities.npcs.Hostile;
import game.entities.npcs.Player;
import game.entities.projectiles.Projectile;
import game.enums.Face;
import game.enums.ItemType;
import game.enums.Pressed;
import game.environment.Exterior;
import game.environment.Interior;
import game.environment.Room;
import game.levelloader.Level;
import game.levelloader.LevelLoader;

/*
* Classname:            MazeGameServer.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * MazeGameServer: This is our Mega Man game
 */
public class MazeGameServer {
    private static boolean isDone;
    public static final int NUM_PLAYERS = 4;
    public static int numPlayers = 0;
    public static ArrayList<ArrayList<Boolean>> inputs = new ArrayList<ArrayList<Boolean>>();
    public final static Level level = LevelLoader.generateRandomLevel(LevelLoader.LevelSize.SMALL);
    
    /**
     * Constructor: Private to prevent instantiation.
     * 
     * @param e
     */
    private MazeGameServer(){}
    
    public static void init() {
        isDone = false;
    }
    
    public static ArrayList<Entity> getEntities() {
        ArrayList<Entity> tmp = new ArrayList<Entity>();
        return tmp;
    }
    
    private static void initSounds() {
        /*sound_hit = GameEngine.addSound("hit.wav");
        sound_shot = GameEngine.addSound("shot.wav");
        sound_spawn = GameEngine.addSound("spawn.wav");
        sound_deflect = GameEngine.addSound("deflect.wav");
        sound_dead = GameEngine.addSound("dead.wav");
        BGM_quickman = GameEngine.addSound("music/quickmanBGM.wav");*/
    }
    
    /*
     * (non-Javadoc)
     * @see IGame#update(long)
     */
    public static List<SerializedObject> update(long time) {
        List<SerializedObject> generatedUpdates = new ArrayList<SerializedObject>();
        spawnPlayer(time);
        
        /*if((GameEngine.getTime()-timeBGM) > 38000) {
            timeBGM = GameEngine.getTime();
        }*/
        
        Exterior ext = level.getExterior();
        if(ext.numPlayers() > 0) {
            for(Player p: ext.getPlayers()) {
                if(!p.isEnabled()) {
                    if(p.getLives() > 0) {
                        p.reset();
                        p.removeLife();
                    }
                    else {
                        // LOSE
                    }
                }
                p.update(inputs.get(p.getPlayerID()), time);
                generatedUpdates.add(p.serialize());
                for(Entity shot: p.getShots()) {
                    generatedUpdates.add(shot.serialize());
                }
            }
            
            for(Entry e: ext.getEntries()) {
                int i = 0;
                while(i < ext.getPlayers().size()) {
                    if(e.transport(ext.getPlayers().get(i))) {
                        //ext.removePlayer(ext.getPlayers().get(i));
                    } else i++;
                }
                generatedUpdates.add(e.serialize());
            }
            generatedUpdates.add(new SerializedRoom(null, 0));
            checkTileCollision(ext);
        }
        
        for(int room = 0; room < level.getRooms().size(); room++) {
            Interior r = level.getRooms().get(room);
            if(r.numPlayers() > 0) {
                generatedUpdates.add(new SerializedRoom(r.getLocation(), room+1));
                for(Player p: r.getPlayers()) {
                    if(!p.isEnabled()) {
                        if(p.getLives() > 0) {
                            p.reset();
                            p.removeLife();
                        }
                        else {
                            // LOSE
                        }
                    }
                    p.update(inputs.get(p.getPlayerID()), time);
                    generatedUpdates.add(p.serialize());
                    for(Entity shot: p.getShots()) {
                        generatedUpdates.add(shot.serialize());
                    }
                }
                
                for(Entry e: r.getEntries()) {
                    int i = 0;
                    while(i < r.getPlayers().size()) {
                        if(e.transport(r.getPlayers().get(i))) {
                            //r.removePlayer(r.getPlayers().get(i));
                        } else i++;
                    }
                    generatedUpdates.add(e.serialize());
                }
                
                int i = 0;
                while(i < r.getEnemies().size()) {
                    if(r.getEnemies().get(i).isEnabled()) {
                        r.getEnemies().get(i).update(time);
                        generatedUpdates.add(r.getEnemies().get(i).serialize());
                        for(Entity shot: r.getEnemies().get(i).getShots()) {
                            generatedUpdates.add(shot.serialize());
                        }
                        i++;
                    } else {
                        r.addItem(EntityFactory.createItem(new Vertex2f(r.getEnemies().get(i).getRigidBody().getLocation()), ItemType.randomItem()));
                        r.getEnemies().remove(i);
					}
				}

                int j = 0;
                while(j < r.getItems().size()) {
                    if(r.getItems().get(j).isEnabled()) {
                        //System.out.print("item:"+r.getEnemies().size());
                        r.getItems().get(j).update(time);
                        generatedUpdates.add(r.getItems().get(j).serialize());
                        j++;
                    } else {
                        r.getItems().remove(j);
                    }
                }
        
                checkCollisions(generatedUpdates, r);
                checkTileCollision(r);
            }
        }
        return generatedUpdates;
    }

    // THIS FUNCTION WILL BE DELETED, SHOULDNT EXIST
    private static void spawnPlayer(long time) {
        Room r = level.getExterior();
        //for(Interior r: level.getRooms()) {
            if(r.numPlayers() > 0) {
                for(Entity p: r.getPlayers()) {
                    ((Player) p).nextAnimation("spawnArray",6);
                }
            }
        //}
    }
  

    private static void checkCollisions(List<SerializedObject> generatedUpdates, Interior room) {
        for (Player player: room.getPlayers()) {
            for(Hostile enemy: room.getEnemies()) {
                if(Collisions.detectCollision(enemy, player)) {
                    player.takeDamage(enemy.getDamage());
                }
                for(Entity shot: player.getShots()) {
                    if(Collisions.detectCollision(enemy, shot)) {
                        ((Projectile) shot).bulletHit(enemy);
                    }
                }
                for(Entity shot: enemy.getShots()) {
                    if(Collisions.detectCollision(shot, player)) {
                        ((Projectile) shot).bulletHit(player);
                    }
                }
            }
            for(GateKeeper gateKeeper: room.getGateKeepers()){
                if(Collisions.detectCollision(player, gateKeeper)){
                    gateKeeper.negotiate(player);
                    Collisions.applySingleCorrection(player, gateKeeper);
                }
                generatedUpdates.add(gateKeeper.serialize());
            }
            for(Entry entry: room.getEntries()){
                if(entry.getRigidBody().isEnabled()){
                    Collisions.detectAndApplySingleCorrection(player, entry);
                }
            }
            
            int i = 0;
            while(i < room.getItems().size()) {
                if(Collisions.detectCollision(player, room.getItems().get(i))){
                    Player p = (Player) player;
                    p.pickItem(room.getItems().get(i));
                    room.removeItem(room.getItems().get(i));
                } else i++;
            }
        }
        
        for(Obstacle trap: room.getTraps()) {
            for(Player player: room.getPlayers()) {
                if(trap.isDangerous()) {
                    if(Collisions.detectCollision(trap, player)) {
                        player.takeDamage(Obstacle.COLLISION_DAMAGE);
                    }
                }
            }
        }
    }

    // checks gravity and collision of all entities vs the world environment
    private static void checkTileCollision(Room room) {
        for(Entity player: room.getPlayers()) {
            Collisions.applyEnvironmentCorrections(player, (ArrayList<Entity>)(ArrayList<?>)room.getForeground());
        }
        
        if(room instanceof Interior) {
            for(Entity enemy: ((Interior) room).getEnemies()) {
                Collisions.applyEnvironmentCorrections(enemy, (ArrayList<Entity>)(ArrayList<?>)room.getForeground());
            }
            
        }
    }
    
    /**
     * isDone: Return is done; true if the game is done (time to exit);
     * false otherwise.
     * 
     * @return isDone
     */
    public static boolean isDone() {
        return isDone;
    }
    
    /**
     * Notification that the player has died.
     */
    public static void notifyDeath() {
        isDone = true;
    }
    
    public static void joinNewPlayer(int playerID) {
        Vertex2f spawnLocation = new Vertex2f(level.getExterior().getPlayerSpawns().get(playerID));
        if(numPlayers < NUM_PLAYERS) {
            level.getExterior().addPlayer(EntityFactory.createPlayer(Face.DOWN, spawnLocation, playerID));
            initNewInputs();
            numPlayers++;
        }
    }
    
    public static void initNewInputs() {
        ArrayList<Boolean> newInputs = new ArrayList<Boolean>();
        for(int j = 0; j < Pressed.SIZE; j++) {
            newInputs.add(false);
        }
        inputs.add(newInputs);
    }
}

