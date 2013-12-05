package game.entities;

import engine.Vector2i;
import engine.Vector2f;
import engine.physics.RigidBody;
import game.entities.environment.*;
import game.entities.items.ABomb;
import game.entities.items.Bomb;
import game.entities.items.CellKey;
import game.entities.items.DisguiseTool;
import game.entities.items.DoorKey;
import game.entities.items.ExtraLife;
import game.entities.items.Gold;
import game.entities.items.HealthBooster;
import game.entities.items.Item;
import game.entities.items.Shield;
import game.entities.npcs.Chaser;
import game.entities.npcs.FlyingBag;
import game.entities.npcs.GateKeeper;
import game.entities.npcs.Hostage;
import game.entities.npcs.Hostile;
import game.entities.npcs.Neutral;
import game.entities.npcs.Player;
import game.entities.npcs.Spider;
import game.entities.npcs.SpiderBoss;
import game.entities.projectiles.Diagonal;
import game.entities.projectiles.Projectile;
import game.entities.projectiles.Straight;
import game.enums.*;
import game.environment.Interior;
import game.environment.Room;
import game.levelloader.LevelLoader;

public class EntityFactory {
    private static final int TILESIZE = LevelLoader.TILESIZE;
    
    public static Player createPlayer(Face direction, Vector2f location, int playerID, Room room) {
        RigidBody rb = new RigidBody(location, 12, 30);
        RigidBody.useLowerBoundingBox(rb, 1.0f/2.0f);
        rb.setOffset(rb.getOffset().x, rb.getOffset().y - 4);
        Player player = new Player(rb, playerID, room, Face.DOWN);
        return player;
    }
    
    public static Hostile createEnemy(Face direction, Vector2f location, Room room, EnemyType type) {
        Hostile enemy = null;
        RigidBody rb = null;
        switch(type) {
            case SPIDER_BOSS:
                rb = new RigidBody(location, TILESIZE*2, TILESIZE*2);
                enemy = new SpiderBoss(rb, room, Face.DOWN);
                break;
            case FLYBAG:
                rb = new RigidBody(location, TILESIZE, TILESIZE);
                enemy = new FlyingBag(rb, room, Face.DOWN);
                break;
            case SPIDER:
                rb = new RigidBody(location, TILESIZE, TILESIZE);
                enemy = new Spider(rb, room, Face.DOWN);
                break;
            case CHASER:
                rb = new RigidBody(location, TILESIZE, TILESIZE);
                enemy = new Chaser(rb, room, Face.DOWN);
                break;
        }
        return enemy;
    }
    
    public static Neutral createNeutral(Vector2f location, NeutralType type, Portal portal) {
        Neutral neutral = null;
        RigidBody rb = null;
        switch(type) {
            case HOSTAGE:
                rb = new RigidBody(location, 14, 14);
                neutral = new Hostage(rb);
                break;
            case GATEKEEPER:
                Face direction = Face.NONE;
                Side side = portal.getSide();
                Vector2f offset = new Vector2f(TILESIZE, TILESIZE);
                switch(side) {
                    case TOP:
                        offset = location.add(offset);
                        direction = Face.DOWN;
                        break;
                    case LEFT:
                        offset = location.add(offset);
                        direction = Face.RIGHT;
                        break;
                    case RIGHT:
                        offset = location.sub(offset);
                        direction = Face.LEFT;
                        break;
                    case BOTTOM:
                        offset = location.sub(offset);
                        direction = Face.UP;
                        break;
                }
                rb = new RigidBody(offset, 14, 14);
                neutral = new GateKeeper(rb, portal, direction);
                break;
        }
        return neutral;
    }
    
    public static Obstacle createObstacle(Vector2f location, ObstacleType type, Interior room) {
        Obstacle obstacle = null;
        RigidBody rb = null;
        switch(type) {
            case SPIKES:
                rb = new RigidBody(location, 12, 12);
                obstacle = new Spikes(rb);
                break;
            case PIT:
                rb = new RigidBody(location, 15, 15);
                obstacle = new Pit(rb);
                break;
            case ROCK:
                rb = new RigidBody(location, 15, 15);
                obstacle = new Rock(rb);
                break;
            case CELLDOOR:
                rb = new RigidBody(location, TILESIZE, TILESIZE);
                obstacle = new CellDoor(rb);
                break;
            case CHEST:
                rb = new RigidBody(location, TILESIZE, TILESIZE);
                obstacle = new Chest(rb, room);
        }
        return obstacle;
    }
    
    public static Projectile createProjectile(Vector2f location, Vector2f target, Face face, Hostile hostile, ProjectileType type) {
        Projectile projectile = null;
        RigidBody rb = null;
        Vector2f newLocation = new Vector2f(location);
        switch(type) {
            case STRAIGHT:
                newLocation.y += 2;
                rb = new RigidBody(newLocation, 6, 6);
                projectile = new Straight(rb, face, hostile);
                break;
            case DIAGONAL:
                newLocation.y += 2;
                rb = new RigidBody(newLocation, 6, 6);
                projectile = new Diagonal(rb, face, hostile);
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
        RigidBody rb = new RigidBody(new Vector2f(location), 10, 10);
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
            case A_BOMB:
                item = new ABomb(rb);
                break;
        }
        return item;
    }
    
    public static Entry createEntry(Vector2f location, Room room, Side side, EntryType type, Door linkedDoor) {
        Entry entry = null;
        RigidBody rb = null;
        switch(type) {
            case DOOR:
                //boolean locked = true;
                Vector2i exit;
                if(side.equals(Side.TOP)) {
                    exit = new Vector2i(location.x.intValue(), TILESIZE + location.y.intValue());
                } else if(side.equals(Side.LEFT)) {
                    exit = new Vector2i(TILESIZE + location.x.intValue(), location.y.intValue());
                } else if(side.equals(Side.RIGHT)) {
                    exit = new Vector2i(location.x.intValue() - TILESIZE, location.y.intValue());
                } else {
                    exit = new Vector2i(location.x.intValue(), location.y.intValue() - TILESIZE);
                }
                rb = new RigidBody(location, 24, 24);
                entry = new Door(rb, exit, room, linkedDoor, side, locked);
                break;
            case PORTAL:
                rb = new RigidBody(location, 24, 24);
                entry = new Portal(rb, room, side);
                break;
            case NONE:
                break;
        }
        return entry;
    }
    
    public static Tile createTile(Vector2f location) {
        RigidBody rb = new RigidBody(location, TILESIZE, TILESIZE);
        return new Tile(rb);
    }
}
