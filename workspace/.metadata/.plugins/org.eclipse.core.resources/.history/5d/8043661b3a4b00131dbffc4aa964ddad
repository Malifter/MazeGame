package game;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import engine.Position;
import engine.physics.*;
import engine.serializable.SerializedObject;


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
public class MazeGameServer extends Game {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3118971393018891785L;
    ArrayList<ArrayList<Boolean>> inputs = null;
    //private int theWidth = 1024; // could be used if gotten from client to resize sprites
    //private int theHeight = 768;
    private Entity winScreen = null;
    private Entity loseScreen = null;
    private Entity startScreen = null;
    /*public ArrayList<Entity> players = null;
    private ArrayList<Entity> background = null;
    private ArrayList<Entity> enemies = null;
    private ArrayList<Entity> traps = null;
    private ArrayList<Entity> foreground = null;*/
    private ArrayList<Entity> healthbar = null;
    public ArrayList<Room> rooms = null;
    private String theAssetsPath = "assets/";
    private String roomLayouts[] = {theAssetsPath + "RoomLayout0.oel", theAssetsPath + "RoomLayout1.oel"};
    private boolean win = false;
    private boolean isStart = false;
    private boolean lose = false;
    
    public static enum Sound {
        HIT(0), SHOT(1), DEFLECT(2), SPAWN(3), DEAD(4), MUSIC(5);
        private final int value;
        private Sound(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    };
    
    /**
     * Constructor
     * 
     * @param e
     */
    public MazeGameServer(GameEngine e) throws Exception {
        super(e);
        /*background = new ArrayList<Entity>();
        foreground = new ArrayList<Entity>();
        players = new ArrayList<Entity>();
        enemies = new ArrayList<Entity>();
        traps = new ArrayList<Entity>();*/
        healthbar = new ArrayList<Entity>();
        rooms = new ArrayList<Room>();
    }
    
    @Override
    public ArrayList<Entity> getEntities() {
        ArrayList<Entity> tmp = new ArrayList<Entity>();
        /*if(isStart){
            if(!win && !lose){
                tmp.addAll(background);        
                tmp.addAll(foreground);
                tmp.addAll(traps);
                for (Entity e : enemies) {
                    tmp.addAll(e.getShots());
                }
                for(Entity e: players) {
                    tmp.addAll(e.getShots());
                }
                tmp.addAll(enemies);
                for(Entity p: players) {
                    tmp.addAll(p.getShots());
                }
                tmp.addAll(players);
                for(Entity p: players) {
                    tmp.add(healthbar.get(p.getHealthPoints()));
                }
            }
            else if(win){
                tmp.add(winScreen);
            }
            else {
                tmp.add(loseScreen);
            }
        }
        else{
            tmp.add(startScreen);
        }*/
        return tmp;
    }
    
    /**
     * loadLevel: Loads a level from a .oel (XML file)
     * 
     * @param filename
     */
    public Room createRoom(int layout, Position<Integer, Integer> position, Door door, Boolean[] addDoor, Boolean[] addPortal) {
        try {
            Room room = new Room(position);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(roomLayouts[layout])));
            String line = null;
            int doorNum = 0;
            int portalNum = 0;
            while((line = bufferedReader.readLine()) != null) {
                line = line.toLowerCase();
                if(line.contains("background")) {
                    int x = position.getX(), y = position.getY();
                    String tileset = line.split("\"")[1];
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("background")) {
                        String[] parts = line.toLowerCase().split(",");
                        for(int p = 0; p < parts.length; p++) {
                            if(Integer.parseInt(parts[p]) != -1) {
                                room.addToBackground(new EnvironmentTile(this, tileset + parts[p] + ".gif", x, y));
                            }
                            x += EnvironmentTile.TILESIZE;
                        }
                        x = position.getX();
                        y += EnvironmentTile.TILESIZE;
                    }
                }
                
                //objects
                if(line.contains("objects")) {
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("objects")) {
                        String[] parts = line.toLowerCase().split("\\s+");
                        int x = Integer.parseInt(parts[3].split("\"")[1]) + position.getX();
                        int y = Integer.parseInt(parts[4].split("\"")[1]) + position.getY();
                        // NOTE WHEN SPAWNING NEW ENEMIES CHANGE CONSTRUCTOR TO TAKE IN ENUM FACE.RIGHT/LEFT/UP/DOWN
                        if(parts[1].contains("playerspawn")) {
                            room.addPlayer(new Player(this, "spawn1.gif", x, y, x+11, y+19, 12, 11, 3, 0));
                        }
                        else if (parts[1].contains("ShieldEnemy")) {
                            room.addEnemy(new ShieldGuyEntity(this, "ShieldGuy1.gif", x, y, x+1, y+2, 24, 22, room));
                        }
                        else if(parts[1].contains("Spike")) {
                            room.addTrap(new SpikeEntity(this, "spikeFloor.gif", x, y, x+7, y+2, 10, 15));
                        }
                        else if(parts[1].contains("cSpike")) {
                            room.addTrap(new SpikeEntity(this, "spikeCeiling.gif", x, y, x+7, y+2, 10, 15));
                        }
                        else if(parts[1].contains("WoodMan")) {
                            room.addEnemy(new WoodManEntity(this, "\\enemies\\woodman1.gif", x, y, x+6, y+6, 30, 26, room));
                        }
                        else if(parts[1].contains("Cannon")) {
                            room.addEnemy(new CannonEntity(this, "\\enemies\\cannon1floor.gif", x, y, x, y, 35, 25, room));
                        }
                        else if(parts[1].contains("door")) {
                            if(addDoor[doorNum]) {
                                Door.Side side;
                                Position<Integer, Integer> exitLoc;
                                if(doorNum == 0) {
                                    side = Door.Side.TOP;
                                    exitLoc = new Position<Integer, Integer>(x, Door.TILESIZE + y);
                                } else if(doorNum == 1) {
                                    side = Door.Side.LEFT;
                                    exitLoc = new Position<Integer, Integer>(Door.TILESIZE + x, y);
                                } else if(doorNum == 2) {
                                    side = Door.Side.RIGHT;
                                    exitLoc = new Position<Integer, Integer>(x - Door.TILESIZE, y);
                                } else {
                                    side = Door.Side.BOTTOM;
                                    exitLoc = new Position<Integer, Integer>(y - Door.TILESIZE, x);
                                }
                                
                                if(door != null) {
                                    if(door.getSide().compareTo(Door.Side.RIGHT) == 0 && side.compareTo(Door.Side.LEFT) == 0) {
                                        room.addDoor(new Door(this, "tiles_mm1_elec38.gif", x, y, exitLoc, room, door, side));
                                    } else if(door.getSide().compareTo(Door.Side.LEFT) == 0 && side.compareTo(Door.Side.RIGHT) == 0) {
                                        room.addDoor(new Door(this, "tiles_mm1_elec38.gif", x, y, exitLoc, room, door, side));
                                    } else if(door.getSide().compareTo(Door.Side.TOP) == 0 && side.compareTo(Door.Side.BOTTOM) == 0) {
                                        room.addDoor(new Door(this, "tiles_mm1_elec38.gif", x, y, exitLoc, room, door, side));
                                    } else if(door.getSide().compareTo(Door.Side.BOTTOM) == 0 && side.compareTo(Door.Side.TOP) == 0) {
                                        room.addDoor(new Door(this, "tiles_mm1_elec38.gif", x, y, exitLoc, room, door, side));
                                    } else {
                                        room.addDoor(new Door(this, "tiles_mm1_elec38.gif", x, y, exitLoc, room, null, side));
                                    }
                                } else {
                                    room.addDoor(new Door(this, "tiles_mm1_elec38.gif", x, y, exitLoc, room, null, side));   
                                }
                            } else {
                                //room.addToForeground(new EnvironmentTile(this, "invisible.gif", x, y));
                            }
                            doorNum++;
                        }
                        else if(parts[1].contains("portal")) {
                            if(addPortal[portalNum]) {
                                Portal portal = new Portal(this, "tiles_mm1_elec6.gif", x, y, room, rooms);
                                room.addPortals(portal);
                                room.addGateKeeper(new GateKeeper(this, "tiles_mm1_elec6.gif", x, y-20, 10,10,10,10, portal));
                            }
                            portalNum++;
                        }
                        // ADD OTHER OBJECTS HERE
                    }
                }
                
                //check foreground and tileset
                if(line.contains("foreground")) {
                    int x = position.getX(), y = position.getY();
                    String tileset = line.split("\"")[1];
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("foreground")) {
                        String[] parts = line.toLowerCase().split(",");
                        for(int p = 0; p < parts.length; p++) {
                            if(Integer.parseInt(parts[p]) != -1) {
                                room.addToForeground(new EnvironmentTile(this, tileset + parts[p] + ".gif", x, y));
                            }
                            x += EnvironmentTile.TILESIZE;
                        }
                        x = position.getX();
                        y += EnvironmentTile.TILESIZE;
                    }
                }
            }
            return room;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    public ArrayList<ArrayList<Boolean>> initInputs() {
        rooms.add(createRoom(0, new Position<Integer, Integer>(0, 0), null, new Boolean[]{false, false, true, false}, new Boolean[]{false, false, false, true}));
        rooms.add(createRoom(1, new Position<Integer, Integer>(Room.WIDTH, 0), rooms.get(0).getDoors().get(0), new Boolean[]{false, true, false, false}, new Boolean[]{false, false, false, false}));
        inputs = new ArrayList<ArrayList<Boolean>>();
        for(int i = 0; i < 4; i++) { // tmp to allow multiplayer
            ArrayList<Boolean> tmpInputs = new ArrayList<Boolean>();
            tmpInputs.add(false); //inputs.add(right);
            tmpInputs.add(false); //inputs.add(left);
            tmpInputs.add(false); //inputs.add(up);
            tmpInputs.add(false); //inputs.add(down);
            tmpInputs.add(false); //inputs.add(fire);
            tmpInputs.add(false); //inputs.add(escape);
            tmpInputs.add(false); //inputs.add(pause);
            tmpInputs.add(false); //inputs.add(startGame);
            inputs.add(tmpInputs);
        }
        /*for(int i = 0; i < 29; i++) {
            healthbar.add(new HealthBar(this, "\\healthbar\\health"+i+".gif", players.get(0)));
        }*/
        startScreen = new EnvironmentTile(this, "startScreen.gif", 0, 0);
        winScreen = new EnvironmentTile(this, "winScreen.gif", 0, 0);
        loseScreen = new EnvironmentTile(this, "game_over.gif", 0, 0);
        //initSounds();
        return inputs;
    }
    
    private void initSounds() {
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
    @Override
    public List<SerializedObject> update(long time) {
        List<SerializedObject> generatedUpdates = new ArrayList<SerializedObject>();
        spawnPlayer(time);
        
        /*if((GameEngine.getTime()-timeBGM) > 38000) {
            timeBGM = GameEngine.getTime();
        }*/
        for(Room r: rooms) {
            if(r.numPlayers() > 0) {
                for(Entity p: r.getPlayers()) {
                    if(p.needsDelete()) {
                        if(p.getLives() > 0) {
                            ((Player) p).reset();
                            p.calculateBounds();
                            p.setLives(p.getLives()-1);
                        }
                        else {
                            lose = true;
                        }
                    }
                    p.update(time);
                    generatedUpdates.add(p.serialize());
                    for(Entity shot: p.getShots()) {
                        generatedUpdates.add(shot.serialize());
                    }
                }
                
                for(Door d: r.getDoors()) {
                    int i = 0;
                    while(i < r.getPlayers().size()) {
                        if(d.contains(r.getPlayers().get(i))) {
                            d.transport(r.getPlayers().get(i));
                            r.removePlayer(r.getPlayers().get(i));
                        } else i++;
                    }
                    generatedUpdates.add(d.serialize());
                }
                
                for(Portal p: r.getPortals()) {
                    int i = 0;
                    while(i < r.getPlayers().size()) {
                        if(p.contains(r.getPlayers().get(i))) {
                            if (p.isActivated()){
                                p.transport(r.getPlayers().get(i));
                                r.removePlayer(r.getPlayers().get(i));
                            }
                            //if portal is not opened, the player is bounced back to the center
                            //of the room
                            else
                            {
                                r.getPlayers().get(i).setMinX(r.getCenter().getX());
                                r.getPlayers().get(i).setMinY(r.getCenter().getY());
                            }  
                        } else i++;
                    }
                    generatedUpdates.add(p.serialize());
                }
                
                for(GateKeeper g: r.getGateKeepers()){ 
                    int i = 0;
                    while(i < r.getPlayers().size()) {
                        if(g.contains(r.getPlayers().get(i))) {
                                g.negotiate(r.getPlayers().get(i));
                        } else i++;
                    }
                    generatedUpdates.add(g.serialize());
                  
                }
                //healthbar.get(players.get(0).getHealthPoints()).update(time);
                //generatedUpdates.add(healthbar.get(players.get(0).getHealthPoints()).serialize());
                
                int i = 0;
                while(i < r.getEnemies().size()) {
                    if(r.getEnemies().get(i).needsDelete()) {
                        r.getEnemies().remove(i);
                    }
                    else {
                        r.getEnemies().get(i).update(time);
                        generatedUpdates.add(r.getEnemies().get(i).serialize());
                        for(Entity shot: r.getEnemies().get(i).getShots()) {
                            generatedUpdates.add(shot.serialize());
                        }
                        i++;
                    }
                }
        
                checkCollisions(generatedUpdates, r);
                checkTileCollision(r);
            
            /*if(inputs.get(GameEngine.Pressed.ESCAPE.getValue())) {
                isDone = true;
            }     
            if(!isStart){
                if(inputs.get(GameEngine.Pressed.START_GAME.getValue())){
                    isStart = true;
                }
            }
            if(win){
                //cam stuff
            }*/
            }
        }
        return generatedUpdates;
    }

    private void spawnPlayer(long time) {
        for(Room r: rooms) {
            if(r.numPlayers() > 0) {
                for(Entity p: r.getPlayers()) {
                    ((Player) p).nextAnimation("spawnArray",6);
                }
            }
        }
    }
  

    private void checkCollisions(List<SerializedObject> generatedUpdates, Room room) {
        for (Entity player: room.getPlayers()) {
            for(Entity enemy: room.getEnemies()) {
                if(Collisions.detectCollision(enemy, player)) {
                    player.takeDamage(enemy.getDamage());
                }
                for(Entity shot: player.getShots()) {
                    if(Collisions.detectCollision(enemy, shot)) {
                        ((ShotEntity) shot).bulletHit(enemy);
                    }
                }
                for(Entity shot: enemy.getShots()) {
                    if(Collisions.detectCollision(shot, player)) {
                        ((ShotEntity) shot).bulletHit(player);
                    }
                }
            }
        }
        for(Entity trap: room.getTraps()) {
            for(Entity player: room.getPlayers()) {
                if(Collisions.detectCollision(trap, player)) {
                    player.takeDamage(trap.getDamage());
                }
            }
        }
    }

    // checks gravity and collision of all entities vs the world environment
    private void checkTileCollision(Room room) {
        for(Entity player: room.getPlayers()) {
            Collisions.applyEnvironmentCollision(player, room.getForeground());
        }
        
        for(Entity enemy: room.getEnemies()) {
            Collisions.applyEnvironmentCollision(enemy, room.getForeground());
        }
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}

