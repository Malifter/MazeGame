package game.entities;

import engine.Vector2i;
import engine.Vector2f;
import engine.physics.RigidBody;
import game.entities.effects.Effect;
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
import game.entities.projectiles.Directed;
import game.entities.projectiles.Projectile;
import game.entities.projectiles.Straight;
import game.enums.*;
import game.environment.Interior;
import game.environment.Room;
import game.levelloader.LevelLoader;

public class EntityFactory {
    private static final int TILESIZE = LevelLoader.TILESIZE;
    
    public static Player createPlayer(Face direction, Vector2f location, int playerID, Room room) {
        //RigidBody rb = new RigidBody(location, 12, 30);
        //RigidBody.useLowerBoundingBox(rb, 1.0f/3.0f);
        //rb.setOffset(rb.getOffset().x, rb.getOffset().y - 4);
        // TODO: Optimize the way we set partial bounding boxes (aka that don't encompass the entire image)
        RigidBody rb = new RigidBody(location, 10, 12); // XXX: make the bouding box much smaller to make it easier to dodge and avoid spikes
        rb.setOffset(rb.getOffset().x, rb.getOffset().y + 5);
        Player player = new Player(rb, playerID, room, Face.DOWN);
        return player;
    }
    
    public static Hostile createEnemy(Face direction, Vector2f location, Room room, EnemyType type) {
        Hostile enemy = null;
        RigidBody rb = null;
        switch(type) {
            case SPIDER_BOSS:
                rb = new RigidBody(location, 32, 26);
               // rb.setOffset(rb.getOffset().x, rb.getOffset().y);
                enemy = new SpiderBoss(rb, room, Face.DOWN);
                break;
            case FLYBAG:
                rb = new RigidBody(location, 10, 8);
                rb.setOffset(rb.getOffset().x, rb.getOffset().y + 2);
                enemy = new FlyingBag(rb, room, Face.DOWN);
                break;
            case SPIDER:
                rb = new RigidBody(location, 10, 10);
                rb.setOffset(rb.getOffset().x, rb.getOffset().y + 1);
                enemy = new Spider(rb, room, Face.DOWN);
                break;
            case CHASER:
                rb = new RigidBody(location, 10, 8);
                rb.setOffset(rb.getOffset().x, rb.getOffset().y + 1);
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
                rb = new RigidBody(location, 10, 8);
                rb.setOffset(rb.getOffset().x, rb.getOffset().y + 4);
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
                rb = new RigidBody(offset, 10, 8);
                rb.setOffset(rb.getOffset().x, rb.getOffset().y + 2);
                System.out.println(side + " " + direction);
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
                rb = new RigidBody(location, 8, 8); // was 12 12
                obstacle = new Spikes(rb);
                break;
            case PIT:
                rb = new RigidBody(location, 12, 12);
                obstacle = new Pit(rb);
                break;
            case ROCK:
                rb = new RigidBody(location, 12, 12);
                obstacle = new Rock(rb);
                break;
            case CELLDOOR:
                rb = new RigidBody(location, 14, 14);
                obstacle = new CellDoor(rb);
                break;
            case CHEST:
                rb = new RigidBody(location, 10, 10); // was 12 12
                obstacle = new Chest(rb, room);
                break;
            case ACTIVE_BOMB:
                rb = new RigidBody(location, 6, 6);
                obstacle = new ActiveBomb(rb, room, null); // was 10 10
                break;
            case CORPSE:
                rb = new RigidBody(location, 8, 6);
                obstacle = new Corpse(AnimationPath.CORPSE, rb);
                break;
        }
        return obstacle;
    }
    
    public static Obstacle createCorpse(Hostile source) {
        if(source instanceof SpiderBoss) {
            return new Corpse(source.getAnimationPath(), new RigidBody(source.getRigidBody()));
        } else {
            return new Corpse(source.getAnimationPath(), new RigidBody(source.getRigidBody(), 8, 6));
        }
    }
    
    public static Obstacle createBomb(Hostile source, Room room) {
        return new ActiveBomb(new RigidBody(source.getRigidBody(), 10, 10), room, source);
    }
    
    public static Obstacle createBomb(Vector2f location, Room room) {
        return new ActiveBomb(new RigidBody(location, 10, 10), room, null);
    }
    
    public static Projectile createProjectile(Vector2f location, Vector2f offset, Vector2f target, Face face, Hostile source, ProjectileType type) {
        Projectile projectile = null;
        RigidBody rb = null;
        Vector2f newLocation = offset == null ? new Vector2f(location) : offset.add(location);
        switch(type) {
            case STRAIGHT:
                rb = new RigidBody(newLocation, 6, 6);
                projectile = new Straight(rb, face, source);
                break;
            case DIAGONAL:
                rb = new RigidBody(newLocation, 6, 6);
                projectile = new Diagonal(rb, face, source);
                break;
            case ARC:
                break;
            case HOMING:
                break;
            case DIRECTED:
                rb = new RigidBody(newLocation, 6, 6);
                projectile = new Directed(rb, face, source);
                break;
            default:
                break;
        }
        return projectile;
    }
    
    public static Item createItem(Vector2f location, ItemType type) {
        Item item = null;
        RigidBody rb = new RigidBody(new Vector2f(location), 6, 6); // was 10 10
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
            case ACTIVE_BOMB:
                System.out.println("THIS SHOULD NEVER BE CALLED - ITEM CREATION IN ENTITY FACTORY");
                break;
        }
        return item;
    }
    
    public static Entry createEntry(Vector2f location, Room room, Side side, EntryType type, Door linkedDoor) {
        Entry entry = null;
        RigidBody rb = null;
        RigidBody zone = null;
        switch(type) {
            case DOOR:
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
                rb = new RigidBody(location, 16, 16);
                zone = new RigidBody(location, 24, 24);
                entry = new Door(rb, zone, exit, room, linkedDoor, side);
                break;
            case PORTAL:
                rb = new RigidBody(location, 16, 16);
                zone = new RigidBody(location, 24, 24);
                entry = new Portal(rb, zone, room, side);
                break;
            case NONE:
                break;
        }
        return entry;
    }
    
    public static Explosion createExplosion(Entity source, Hostile owner) {
        RigidBody rb = new RigidBody(source.getRigidBody().getLocation(), TILESIZE*2, TILESIZE*2);
        return new Explosion(rb, owner);
    }
    
    public static Effect createEffect(EffectType type, Entity source) {
        RigidBody rb = new RigidBody(source.getRigidBody().getLocation(), 0, 0);
        return new Effect(type, rb);
    }
    
    public static Tile createTile(Vector2f location) {
        RigidBody rb = new RigidBody(location, TILESIZE, TILESIZE);
        return new Tile(rb);
    }
}
