package game.entities;

import engine.Vector2i;
import engine.Vector2f;
import engine.physics.RigidBody;
import game.entities.environment.*;
import game.entities.items.Bomb;
import game.entities.items.CellKey;
import game.entities.items.DisguiseTool;
import game.entities.items.DoorKey;
import game.entities.items.ExtraLife;
import game.entities.items.Gold;
import game.entities.items.HealthBooster;
import game.entities.items.Item;
import game.entities.items.Shield;
import game.entities.npcs.Cannon;
import game.entities.npcs.GateKeeper;
import game.entities.npcs.Hostile;
import game.entities.npcs.Neutral;
import game.entities.npcs.Player;
import game.entities.npcs.ShieldGuy;
import game.entities.npcs.Woodman;
import game.entities.projectiles.Projectile;
import game.enums.*;
import game.environment.Room;
import game.levelloader.LevelLoader;

public class EntityFactory {
    private static final int TILESIZE = LevelLoader.TILESIZE;
    
    public static Player createPlayer(Face direction, Vector2f location, int playerID, Room room) {
        RigidBody rb = new RigidBody(location, 12, 30);
        RigidBody.useLowerBoundingBox(rb, 1.0f/3.0f);
        Player player = new Player("spawn1.gif", rb, playerID, room);
        return player;
    }
    
    public static Hostile createEnemy(Face direction, Vector2f location, Room room, EnemyType type) {
        Hostile enemy = null;
        RigidBody rb = null;
        switch(type) {
            case WOODMAN:
                rb = new RigidBody(location, 26, 32);
                RigidBody.useLowerBoundingBox(rb, 1.0f/2.0f);
                enemy = new Woodman(type.getPath()+"woodman1.gif", rb, room);
                break;
            case SHIELD:
                rb = new RigidBody(location, 24, 24);
                RigidBody.useLowerBoundingBox(rb, 1.0f/2.0f);
                enemy = new ShieldGuy(type.getPath()+"ShieldGuy1.gif", rb, room);
                break;
            case CANNON:
                rb = new RigidBody(location, 33, 30);
                RigidBody.useLowerBoundingBox(rb, 2.0f/3.0f);
                enemy = new Cannon(type.getPath()+"cannon1floor.gif", rb, room);
                break;
        }
        return enemy;
    }
    
    public static Neutral createNeutral(Face direction, Vector2i location, NeutralType type, Portal portal) {
        Neutral neutral = null;
        RigidBody rb = null;
        switch(type) {
            case HOSTAGE:
                break;
            case GATEKEEPER:
                rb = new RigidBody(location, 16, 16);
                neutral = new GateKeeper(type.getPath()+"alien4.gif", rb, portal);
                break;
        }
        return neutral;
    }
    
    public static Obstacle createObstacle(Vector2f location, ObstacleType type) {
        Obstacle obstacle = null;
        RigidBody rb = null;
        switch(type) {
            case SPIKES:
                rb = new RigidBody(location, 12, 12);
                obstacle = new Spikes(type.getPath()+"spikeFloor.gif", rb);
                break;
            case PIT:
                //rb = new RigidBody(location, TILESIZE, TILESIZE);
                break;
            case ROCK:
                //rb = new RigidBody(location, TILESIZE, TILESIZE);
                break;
        }
        return obstacle;
    }
    
    public static Projectile createProjectile(Vector2f location, Vector2f target, Hostile hostile, ProjectileType type) {
        Projectile projectile = null;
        RigidBody rb = null;
        switch(type) {
            case STRAIGHT:
                Vector2f dir = target.sub(location);
                Face direction;
                if(Math.abs(dir.y) > Math.abs(dir.x)) {
                    // Up or Down
                    if(dir.y > 0) {
                        direction = Face.DOWN;
                    } else {
                        direction = Face.UP;
                    }
                } else {
                    // Right or Left
                    if(dir.x > 0) {
                        direction = Face.RIGHT;
                    } else {
                        direction = Face.LEFT;
                    }
                }
                rb = new RigidBody(new Vector2f(location), 6, 6);
                projectile = new Projectile("shot.gif", rb, direction, hostile);
                break;
            case DIAGONAL:
                break;
            case ARC:
                break;
            case HOMING:
                break;
        }
        return projectile;
    }
    
    public static Item createItem(Vector2f location, ItemType type) {
        Item item = null;
        RigidBody rb = new RigidBody(location, TILESIZE, TILESIZE);
        switch(type) {
            case BOMB:
                item = new Bomb(rb);
                break;
            case GOLD:
                item = new Gold(rb);
                break;
            case CKEY:
                item = new CellKey(rb);
                break;
            case DKEY:
                item = new DoorKey(rb);
                break;
            case TOOL:
                item = new DisguiseTool(rb);
                break;
            case LIFE:
                item = new ExtraLife(rb);
                break;
            case BOOSTER:
                item = new HealthBooster(rb);
                break;
            case SHIELD:
                item = new Shield(rb);
                break;
        }
        return item;
    }
    
    public static Entry createEntry(Vector2i location, Room room, Side side, EntryType type, Door linkedDoor) {
        Entry entry = null;
        RigidBody rb = null;
        String entryPath = "tilesets/";
        // Have to shift the location from the top-left to center of door
        location.addEq(new Vector2i(TILESIZE/2, TILESIZE/2));
        switch(type) {
            case DOOR:
                boolean locked = false;
                Vector2i exit;
                if(side.equals(Side.TOP)) {
                    entryPath += "tiles_mm1_elec/29.gif";
                    exit = new Vector2i(location.x.intValue(), TILESIZE + location.y.intValue());
                } else if(side.equals(Side.LEFT)) {
                    entryPath += "tiles_mm1_elec/38.gif";
                    exit = new Vector2i(TILESIZE + location.x.intValue(), location.y.intValue());
                } else if(side.equals(Side.RIGHT)) {
                    entryPath += "tiles_mm1_elec/38.gif";
                    exit = new Vector2i(location.x.intValue() - TILESIZE, location.y.intValue());
                } else {
                    entryPath += "tiles_mm1_elec/29.gif";
                    exit = new Vector2i(location.x.intValue(), location.y.intValue() - TILESIZE);
                }
                rb = new RigidBody(location, 24, 24);
                entry = new Door(entryPath, rb, exit, room, linkedDoor, side, locked);
                break;
            case CELLDOOR:
                break;
            case PORTAL:
                entryPath += "tiles_mm1_elec/6.gif";
                rb = new RigidBody(location, 24, 24);
                entry = new Portal(entryPath, rb, room, side);
                break;
        }
        return entry;
    }
    
    public static Tile createTile(Vector2i location, String tileset) {
        location.addEq(new Vector2i(TILESIZE/2, TILESIZE/2));
        RigidBody rb = new RigidBody(location, TILESIZE, TILESIZE);
        return new Tile(tileset, rb);
    }
}
