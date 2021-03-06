package game.levelloader;

import engine.Vector2i;
import engine.Vector2f;
import engine.serializable.SerializedRoom;
import game.entities.Entity;
import game.entities.EntityFactory;
import game.entities.environment.Door;
import game.entities.environment.Entry;
import game.entities.environment.Portal;
import game.enums.*;
import game.environment.Exterior;
import game.environment.Interior;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class LevelLoader {
    private final static String layoutPath = "assets/layouts/";
    private final static String exteriorLayout = layoutPath+"exterior/ExteriorLayout.oel";
    private final static String roomLayouts[] = {layoutPath+"rooms/RoomLayout0.oel",
        layoutPath+"rooms/RoomLayout1.oel",
        layoutPath+"rooms/RoomLayout2.oel",
        layoutPath+"rooms/RoomLayout3.oel",
        layoutPath+"rooms/RoomLayout4.oel",
        layoutPath+"rooms/RoomLayout5.oel",
        layoutPath+"rooms/RoomLayout6.oel",
        layoutPath+"rooms/RoomLayout7.oel",
        layoutPath+"rooms/RoomLayout8.oel",
        layoutPath+"rooms/RoomLayout9.oel",
        layoutPath+"rooms/RoomLayout10.oel",
        layoutPath+"rooms/RoomLayout11.oel",
        layoutPath+"rooms/RoomLayout12.oel",
        layoutPath+"rooms/RoomLayout13.oel",
        layoutPath+"rooms/RoomLayout14.oel",
        layoutPath+"rooms/RoomLayout15.oel",
        layoutPath+"rooms/RoomLayout16.oel",
        layoutPath+"rooms/RoomLayout17.oel"};
    private final static String hostageLayout = layoutPath+"hostage/HostageRoom0.oel";
    public final static int TILESIZE = 16;
    private final static Vector2f OFFSET = new Vector2f(TILESIZE/2, TILESIZE/2);
    
    private static Level level;
    
    private static ArrayList<SerializedRoom> levelLayout = new ArrayList<SerializedRoom>();
    
    public static enum LevelSize {
        MINI(3), SMALL(5), MEDIUM(7), LARGE(9);
        private final int value;
        private LevelSize(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    };
    
    public static ArrayList<SerializedRoom> getLevelLayout() {
        return levelLayout;
    }
    
    public static Level generateRandomLevel(LevelSize breadth) {
        // Create initial variables for level and it's contents
        level = new Level();
        int size = breadth.getValue();
        ArrayList<Interior> rooms = new ArrayList<Interior>();
        for(int i = 0; i < size*size; i++) {
            rooms.add(null);
        }
        EntryType[] generateEntry = new EntryType[Interior.MAX_ENTRIES];
        Vector2i position = null;
        
        // Create an initial hostage room at center of maze with 4 doors
        for(int i = 0; i < generateEntry.length; i++) {
            generateEntry[i] = EntryType.DOOR;
        }
        position = new Vector2i((size/2)*Interior.WIDTH, (size/2)*Interior.HEIGHT);
        rooms.set((size/2)+((size/2)*size), createRoom(-1, position, null, generateEntry)); // for now center will be Hostage room
        // Generate Rooms branching outwards from center (only add a room if have a door with no link)
        boolean newLinks = true;
        while(newLinks) {
            newLinks = false;
            for(int room = 0; room < rooms.size(); room++) {
                if(rooms.get(room) != null) {
                    for(Entry entry: rooms.get(room).getEntries()) {
                        Door door = null;
                        if(entry instanceof Door) {
                            door = (Door)entry;
                        }
                        if(door != null && door.getLink() == null) {
                            //boolean test = false;
                            int newRoom = -1;
                            if(door.getSide().equals(Side.TOP) && (newRoom = room-size) >= 0 && rooms.get(newRoom) == null) {
                                position = new Vector2i((newRoom%size)*Interior.WIDTH, (newRoom/size)*Interior.HEIGHT);
                                randomEntry(generateEntry, newRoom, size, rooms);
                                generateEntry[door.getSide().opposite().getIndex()] = EntryType.DOOR;
                                rooms.set(newRoom, createRoom(randomLayout(), position, door, generateEntry));
                                linkAdjacentDoors(newRoom, size, rooms);
                                newLinks = true;
                                //test = true;
                            } else if(door.getSide().equals(Side.BOTTOM) && (newRoom = room+size) < size*size && rooms.get(newRoom) == null) {
                                position = new Vector2i((newRoom%size)*Interior.WIDTH, (newRoom/size)*Interior.HEIGHT);
                                randomEntry(generateEntry, newRoom, size, rooms);
                                generateEntry[door.getSide().opposite().getIndex()] = EntryType.DOOR;
                                rooms.set(newRoom, createRoom(randomLayout(), position, door, generateEntry));
                                linkAdjacentDoors(newRoom, size, rooms);
                                newLinks = true;
                                //test = true;
                            } else if(door.getSide().equals(Side.LEFT) && (newRoom = room-1) >= 0 && rooms.get(newRoom) == null) {
                                position = new Vector2i((newRoom%size)*Interior.WIDTH, (newRoom/size)*Interior.HEIGHT);
                                randomEntry(generateEntry, newRoom, size, rooms);
                                generateEntry[door.getSide().opposite().getIndex()] = EntryType.DOOR;
                                rooms.set(newRoom, createRoom(randomLayout(), position, door, generateEntry));
                                linkAdjacentDoors(newRoom, size, rooms);
                                newLinks = true;
                                //test = true;
                            } else if(door.getSide().equals(Side.RIGHT) && (newRoom = room+1) < size*size && rooms.get(newRoom) == null) {
                                position = new Vector2i((newRoom%size)*Interior.WIDTH, (newRoom/size)*Interior.HEIGHT);
                                randomEntry(generateEntry, newRoom, size, rooms);
                                generateEntry[door.getSide().opposite().getIndex()] = EntryType.DOOR;
                                rooms.set(newRoom, createRoom(randomLayout(), position, door, generateEntry));
                                linkAdjacentDoors(newRoom, size, rooms);
                                newLinks = true;
                                //test = true;
                            }
                            /*if(test) {
                                System.out.println("Room: " + room + " newRoom: " + newRoom);
                                System.out.println("TOP: " + generateEntry[Side.TOP.getIndex()]);
                                System.out.println("BOTTOM: " + generateEntry[Side.BOTTOM.getIndex()]);
                                System.out.println("LEFT: " + generateEntry[Side.LEFT.getIndex()]);
                                System.out.println("RIGHT: " + generateEntry[Side.RIGHT.getIndex()]);
                                break;
                            }*/
                        }
                    }
                }
            }
        }
        
        for(int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i) != null) {
                linkAdjacentDoors(i, size, rooms);
            }
        }
        
        // double check all links are made
        /*for(int r = 0; r < size; r++) {
            for(int c = 0; c < size; c++) {
                if(rooms.get(c + (r*size)) != null) {
                    boolean top = false;
                    boolean bot = false;
                    boolean right = false;
                    boolean left = false;
                    for(Entry entry: rooms.get(c + (r*size)).getEntries()) {
                        Door door = null;
                        if(entry instanceof Door) {
                            door = (Door)entry;
                        }
                        if(door != null) {
                            if(door.getSide().equals(Side.TOP)) {
                                top = true;
                            } else if(door.getSide().equals(Side.BOTTOM)) {
                                bot = true;
                            } else if(door.getSide().equals(Side.RIGHT)) {
                                right = true;
                            } else {
                                left = true;
                            }
                        }
                    }
                    if(left) System.out.print("}");
                    else System.out.print("|");
                    if(top && bot) System.out.print(" ");
                    else if(top) System.out.print("_");
                    else if(bot) System.out.print("-");
                    else System.out.print("=");
                    if(right) System.out.print("{");
                    else System.out.print("|");
                    /*System.out.println("Room: " + (c + (r*size)));
                    if(top) System.out.println("TOP: DOOR");
                    else System.out.println("TOP: NONE");
                    if(bot) System.out.println("BOTTOM: DOOR");
                    else System.out.println("BOTTOM: NONE");
                    if(left) System.out.println("LEFT: DOOR");
                    else System.out.println("LEFT: NONE");
                    if(right) System.out.println("RIGHT: DOOR");
                    else System.out.println("RIGHT: NONE");*/
                    //linkAdjacentDoors(room, size, rooms);
                /*} else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }*/
        
        // Find edge rooms and link them to generated exterior
        Exterior outer = createOuter(exteriorLayout, size, rooms);
        
        if(outer.getEntries().isEmpty()) {
            generateOuterDoors(outer, size, rooms);            
        }
        
        // Add attributes to level
        level.setExterior(outer);
        levelLayout.add(new SerializedRoom(null, level.getExterior().layout));
        for(int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i) != null) {
                levelLayout.add(new SerializedRoom(rooms.get(i).getLocation(), rooms.get(i).layout));
                level.addRoom(rooms.get(i));
            }
        }
        return level;
    }
    
    private static void generateOuterDoors(Exterior outer, int size, ArrayList<Interior> rooms) {
        int minTop = (size/2)+((size/2)*size);
        int maxBot = minTop;
        int minLeft = minTop;
        int maxRight = minTop;
        // find four edge rooms
        for(int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i) != null) {
                int row = i/size;
                int col = i%size;
                if(row < minTop/size) {
                    minTop = i;
                }
                if(row > maxBot/size) {
                    maxBot = i;
                }
                if(col < minLeft%size) {
                    minLeft = i;
                }
                if(col > maxRight%size) {
                    maxRight = i;
                }
            }
        }
        // convert outer wall to door for TOP
        Interior room = rooms.get(minTop);
        for(Entity fgr: room.getForeground()) {
            if(fgr.getRigidBody().getMin().y.intValue() == room.getCenter().y-(Interior.HEIGHT/2)
                    && fgr.getRigidBody().getMin().x.intValue() == room.getCenter().x-(TILESIZE/2)) {
                Door door = forceAddDoorToRoom(room, fgr.getRigidBody().getMin().x.intValue(), fgr.getRigidBody().getMin().y.intValue(), Side.TOP);
                room.getForeground().remove(fgr);
                for(Entity fgo: outer.getForeground()) {
                    if(fgo.getRigidBody().getMin().y.intValue() == 240 && fgo.getRigidBody().getMin().x.intValue() == 640) {
                        forceAddDoorToOuter(outer, fgo.getRigidBody().getMin().x.intValue(), fgo.getRigidBody().getMin().y.intValue(), door, Side.BOTTOM);
                        outer.getForeground().remove(fgo);
                        break;
                    }
                }
                break;
            }
        }
        // convert outer wall to door for BOTTOM
        room = rooms.get(maxBot);
        for(Entity fgr: room.getForeground()) {
            if(fgr.getRigidBody().getMin().y.intValue() == room.getCenter().y+(Interior.HEIGHT/2)-TILESIZE
                    && fgr.getRigidBody().getMin().x.intValue() == room.getCenter().x-(TILESIZE/2)) {
                Door door = forceAddDoorToRoom(room, fgr.getRigidBody().getMin().x.intValue(), fgr.getRigidBody().getMin().y.intValue(), Side.BOTTOM);
                room.getForeground().remove(fgr);
                for(Entity fgo: outer.getForeground()) {
                    if(fgo.getRigidBody().getMin().y.intValue() == 688 && fgo.getRigidBody().getMin().x.intValue() == 640) {
                        forceAddDoorToOuter(outer, fgo.getRigidBody().getMin().x.intValue(), fgo.getRigidBody().getMin().y.intValue(), door, Side.TOP);
                        outer.getForeground().remove(fgo);
                        break;
                    }
                }
                break;
            }
        }
        // convert outer wall to door for LEFT
        room = rooms.get(minLeft);
        for(Entity fgr: room.getForeground()) {
            if(fgr.getRigidBody().getMin().y.intValue() == room.getCenter().y-(TILESIZE/2)
                    && fgr.getRigidBody().getMin().x.intValue() == room.getCenter().x-(Interior.WIDTH/2)) {
                Door door = forceAddDoorToRoom(room, fgr.getRigidBody().getMin().x.intValue(), fgr.getRigidBody().getMin().y.intValue(), Side.LEFT);
                room.getForeground().remove(fgr);
                for(Entity fgo: outer.getForeground()) {
                    if(fgo.getRigidBody().getMin().y.intValue() == 464 && fgo.getRigidBody().getMin().x.intValue() == 320) {
                        forceAddDoorToOuter(outer, fgo.getRigidBody().getMin().x.intValue(), fgo.getRigidBody().getMin().y.intValue(), door, Side.RIGHT);
                        outer.getForeground().remove(fgo);
                        break;
                    }
                }
                break;
            }
        }
        // convert outer wall to door for RIGHT
        room = rooms.get(maxRight);
        for(Entity fgr: room.getForeground()) {
            if(fgr.getRigidBody().getMin().y.intValue() == room.getCenter().y-(TILESIZE/2)
                    && fgr.getRigidBody().getMin().x.intValue() == room.getCenter().x+(Interior.WIDTH/2)-TILESIZE) {
                Door door = forceAddDoorToRoom(room, fgr.getRigidBody().getMin().x.intValue(), fgr.getRigidBody().getMin().y.intValue(), Side.RIGHT);
                room.getForeground().remove(fgr);
                for(Entity fgo: outer.getForeground()) {
                    if(fgo.getRigidBody().getMin().y.intValue() == 464 && fgo.getRigidBody().getMin().x.intValue() == 960) {
                        forceAddDoorToOuter(outer, fgo.getRigidBody().getMin().x.intValue(), fgo.getRigidBody().getMin().y.intValue(), door, Side.LEFT);
                        outer.getForeground().remove(fgo);
                        break;
                    }
                }
                break;
            }
        }
    }
    
    private static void forceAddDoorToOuter(Exterior outer, int x, int y, Door linkedDoor, Side side) {
        outer.addEntry(EntityFactory.createEntry((new Vector2f(x, y)).add(OFFSET), outer, side, EntryType.DOOR, linkedDoor));
    }
    
    private static Door forceAddDoorToRoom(Interior room, int x, int y, Side side) {
        Door door = (Door) EntityFactory.createEntry((new Vector2f(x, y)).add(OFFSET), room, side, EntryType.DOOR, null);
        room.addEntry(door);
        return door;
    }
    
    private static void linkAdjacentDoors(int index, int size, ArrayList<Interior> rooms) {
        Interior room = rooms.get(index);
        for(Entry entry: room.getEntries()) {
            Door door = null;
            if(entry instanceof Door){
                door = (Door)entry;
            }
            if(door != null && door.getLink() == null) {
                int adjRoom = -1;
                if(door.getSide().equals(Side.TOP) && (adjRoom = index-size) >= 0) {
                    if(rooms.get(adjRoom) != null) {
                        boolean doorLinked = false;
                        for(Entry adjEntry: rooms.get(adjRoom).getEntries()) {
                            Door adjDoor = null;
                            if(adjEntry instanceof Door) {
                                adjDoor = (Door)adjEntry;
                            }
                            if(adjDoor != null && adjDoor.getSide().opposite().equals(door.getSide())) {
                                door.setLink(adjDoor);
                                doorLinked = true;
                            }
                        }
                        if(!doorLinked) {
                            Vector2i position = new Vector2i((adjRoom%size)*Interior.WIDTH, (adjRoom/size)*Interior.HEIGHT);
                            // ADJUST TO BOTTOM DOOR
                            position.addEq(new Vector2i((Interior.WIDTH/2) - (TILESIZE/2), Interior.HEIGHT - TILESIZE));
                            Door adjDoor = forceAddDoorToRoom(rooms.get(adjRoom), position.x, position.y, door.getSide().opposite());
                            door.setLink(adjDoor);
                            doorLinked = true;
                            // REMOVE FOREGROUND FROM ADJACENT ROOM
                            for(Entity fgr: rooms.get(adjRoom).getForeground()) {
                                if(fgr.getRigidBody().getLocation().equals(adjDoor.getRigidBody().getLocation())) {
                                    rooms.get(adjRoom).getForeground().remove(fgr);
                                    break;
                                }
                            }
                        }
                    }
                } else if(door.getSide().equals(Side.BOTTOM) && (adjRoom = index+size) < size*size) {
                    if(rooms.get(adjRoom) != null) {
                        boolean doorLinked = false;
                        for(Entry adjEntry: rooms.get(adjRoom).getEntries()) {
                            Door adjDoor = null;
                            if(adjEntry instanceof Door) {
                                adjDoor = (Door)adjEntry;
                            }
                            if(adjDoor != null && adjDoor.getSide().opposite().equals(door.getSide())) {
                                door.setLink(adjDoor);
                                doorLinked = true;
                            }
                        }
                        if(!doorLinked) {
                            Vector2i position = new Vector2i((adjRoom%size)*Interior.WIDTH, (adjRoom/size)*Interior.HEIGHT);
                            // ADJUST TO TOP DOOR
                            position.addEq(new Vector2i((Interior.WIDTH/2) - (TILESIZE/2), 0));
                            Door adjDoor = forceAddDoorToRoom(rooms.get(adjRoom), position.x, position.y, door.getSide().opposite());
                            door.setLink(adjDoor);
                            doorLinked = true;
                            // REMOVE FOREGROUND FROM ADJACENT ROOM
                            for(Entity fgr: rooms.get(adjRoom).getForeground()) {
                                if(fgr.getRigidBody().getLocation().equals(adjDoor.getRigidBody().getLocation())) {
                                    rooms.get(adjRoom).getForeground().remove(fgr);
                                    break;
                                }
                            }
                        }
                    }
                } else if(door.getSide().equals(Side.LEFT) && (adjRoom = index-1) >= 0) {
                    if(rooms.get(adjRoom) != null) {
                        boolean doorLinked = false;
                        for(Entry adjEntry: rooms.get(adjRoom).getEntries()) {
                            Door adjDoor = null;
                            if(adjEntry instanceof Door) {
                                adjDoor = (Door)adjEntry;
                            }
                            if(adjDoor != null && adjDoor.getSide().opposite().equals(door.getSide())) {
                                door.setLink(adjDoor);
                                doorLinked = true;
                            }
                        }
                        if(!doorLinked) {
                            Vector2i position = new Vector2i((adjRoom%size)*Interior.WIDTH, (adjRoom/size)*Interior.HEIGHT);
                            // ADJUST TO RIGHT DOOR
                            position.addEq(new Vector2i(Interior.WIDTH - TILESIZE, (Interior.HEIGHT/2) - (TILESIZE/2)));
                            Door adjDoor = forceAddDoorToRoom(rooms.get(adjRoom), position.x, position.y, door.getSide().opposite());
                            door.setLink(adjDoor);
                            doorLinked = true;
                            // REMOVE FOREGROUND FROM ADJACENT ROOM
                            for(Entity fgr: rooms.get(adjRoom).getForeground()) {
                                if(fgr.getRigidBody().getLocation().equals(adjDoor.getRigidBody().getLocation())) {
                                    rooms.get(adjRoom).getForeground().remove(fgr);
                                    break;
                                }
                            }
                        }
                    }
                } else if(door.getSide().equals(Side.RIGHT) && (adjRoom = index+1) < size*size) {
                    if(rooms.get(adjRoom) != null) {
                        boolean doorLinked = false;
                        for(Entry adjEntry: rooms.get(adjRoom).getEntries()) {
                            Door adjDoor = null;
                            if(adjEntry instanceof Door) {
                                adjDoor = (Door)adjEntry;
                            }
                            if(adjDoor != null && adjDoor.getSide().opposite().equals(door.getSide())) {
                                door.setLink(adjDoor);
                                doorLinked = true;
                            }
                        }
                        if(!doorLinked) {
                            Vector2i position = new Vector2i((adjRoom%size)*Interior.WIDTH, (adjRoom/size)*Interior.HEIGHT);
                            // ADJUST TO LEFT DOOR
                            position.addEq(new Vector2i(0, (Interior.HEIGHT/2) - (TILESIZE/2)));
                            Door adjDoor = forceAddDoorToRoom(rooms.get(adjRoom), position.x, position.y, door.getSide().opposite());
                            door.setLink(adjDoor);
                            doorLinked = true;
                            // REMOVE FOREGROUND FROM ADJACENT ROOM
                            for(Entity fgr: rooms.get(adjRoom).getForeground()) {
                                if(fgr.getRigidBody().getLocation().equals(adjDoor.getRigidBody().getLocation())) {
                                    rooms.get(adjRoom).getForeground().remove(fgr);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static void randomEntry(EntryType[] generateEntry, int index, int size, ArrayList<Interior> rooms) {
        boolean onePortalPerRoom = false;
        for(int i = 0; i < generateEntry.length; i++) {
            double prob = Math.random();
            if(prob <= EntryType.NONE.getProbability()) {
                generateEntry[i] = EntryType.NONE;
            } else {
                prob -= EntryType.NONE.getProbability();
                if(prob <= EntryType.DOOR.getProbability()) {
                    generateEntry[i] = EntryType.DOOR;
                } else {
                    prob -= EntryType.DOOR.getProbability();
                    if(prob <= EntryType.PORTAL.getProbability()) {
                        if(!onePortalPerRoom) {
                            generateEntry[i] = EntryType.PORTAL;
                            onePortalPerRoom = true;
                        } else {
                            generateEntry[i] = EntryType.DOOR;
                        }
                    }
                }
            }
        }
        int adjRoom = -1;
        if((adjRoom = index-size) >= 0 && rooms.get(adjRoom) != null) {
            if(generateEntry[Side.TOP.getIndex()].equals(EntryType.DOOR)) {
                generateEntry[Side.TOP.getIndex()] = EntryType.NONE;
            } else if(generateEntry[Side.TOP.getIndex()].equals(EntryType.PORTAL)) {
                for(Entry entry: rooms.get(adjRoom).getEntries()) {
                    if(entry.getSide().opposite().equals(Side.TOP)) {
                        generateEntry[Side.TOP.getIndex()] = EntryType.NONE;
                    }
                }
            }
        }
        if((adjRoom = index+size) < size*size && rooms.get(adjRoom) != null) {
            if(generateEntry[Side.BOTTOM.getIndex()].equals(EntryType.DOOR)) {
                generateEntry[Side.BOTTOM.getIndex()] = EntryType.NONE;
            } else if(generateEntry[Side.BOTTOM.getIndex()].equals(EntryType.PORTAL)) {
                for(Entry entry: rooms.get(adjRoom).getEntries()) {
                    if(entry.getSide().opposite().equals(Side.BOTTOM)) {
                        generateEntry[Side.BOTTOM.getIndex()] = EntryType.NONE;
                    }
                }
            }
        }
        if((adjRoom = index+1) < size*size && rooms.get(adjRoom) != null) {
            if(generateEntry[Side.RIGHT.getIndex()].equals(EntryType.DOOR)) {
                generateEntry[Side.RIGHT.getIndex()] = EntryType.NONE;
            } else if(generateEntry[Side.RIGHT.getIndex()].equals(EntryType.PORTAL)) {
                for(Entry entry: rooms.get(adjRoom).getEntries()) {
                    if(entry.getSide().opposite().equals(Side.RIGHT)) {
                        generateEntry[Side.RIGHT.getIndex()] = EntryType.NONE;
                    }
                }
            }
        }
        if((adjRoom = index-1) >= 0 && rooms.get(adjRoom) != null) {
            if(generateEntry[Side.LEFT.getIndex()].equals(EntryType.DOOR)) {
                generateEntry[Side.LEFT.getIndex()] = EntryType.NONE;
            } else if(generateEntry[Side.LEFT.getIndex()].equals(EntryType.PORTAL)) {
                for(Entry entry: rooms.get(adjRoom).getEntries()) {
                    if(entry.getSide().opposite().equals(Side.LEFT)) {
                        generateEntry[Side.LEFT.getIndex()] = EntryType.NONE;
                    }
                }
            }
        }
    }
    
    private static int randomLayout() {
        return (int) (Math.random() * roomLayouts.length);
    }
    
    /**
     * loadLevel: Loads a room from a .oel (XML file)
     * 
     * @param filename
     */
    private static Interior createRoom(int layout, Vector2i position, Door door, EntryType[] generateEntry) {
        try {
            BufferedReader bufferedReader;
            if(layout == -1) {
                bufferedReader = new BufferedReader(new FileReader(new File(hostageLayout)));
            } else {
                bufferedReader = new BufferedReader(new FileReader(new File(roomLayouts[layout])));
            }
            Interior room = new Interior(position, layout);
            EnemyType enemyType1 = EnemyType.randomWeakEnemy();
            EnemyType enemyType2 = EnemyType.randomWeakEnemy();
            ObstacleType obstacleType1 = ObstacleType.randomObstacle();
            ObstacleType obstacleType2 = ObstacleType.randomObstacle();
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                line = line.toLowerCase();
                
                //check foreground and tileset
                if(line.contains("foreground")) {
                    int x = position.x, y = position.y;
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("foreground")) {
                        String[] parts = line.toLowerCase().split(",");
                        for(int p = 0; p < parts.length; p++) {
                            if(Integer.parseInt(parts[p]) != -1) {
                                room.addToForeground(EntityFactory.createTile((new Vector2f(x, y)).add(OFFSET)));
                            }
                            x += TILESIZE;
                        }
                        x = position.x;
                        y += TILESIZE;
                    }
                }
                
                //objects
                if(line.contains("objects")) {
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("objects")) {
                        String[] parts = line.toLowerCase().split("\\s+");
                        int x = Integer.parseInt(parts[3].split("\"")[1]) + position.x;
                        int y = Integer.parseInt(parts[4].split("\"")[1]) + position.y;
                        Vector2f location = new Vector2f(x, y);
                        if(parts[1].contains("enemyspawn")) {
                            room.addEnemy(EntityFactory.createEnemy(Face.randomFace(), location.add(OFFSET), room, enemyType1));
                        } else if(parts[1].contains("enemyspawn2")) {
                            room.addEnemy(EntityFactory.createEnemy(Face.randomFace(), location.add(OFFSET), room, enemyType2));
                        } else if(parts[1].contains("itemspawn")) {
                            double rand = Math.random();
                            if(rand <= 0.50) {
                                room.addItem(EntityFactory.createItem(location.add(OFFSET), ItemType.randomItem()));
                            } else if(rand <= 0.6) {
                                room.addObstacle(EntityFactory.createObstacle(location.add(OFFSET), ObstacleType.CHEST, room));
                            } else if(rand <= 0.66) {
                                room.addObstacle(EntityFactory.createObstacle(location.add(OFFSET), ObstacleType.ACTIVE_BOMB, room));
                            }
                          
                        } else if(parts[1].contains("bossspawn")) {
                            room.addEnemy(EntityFactory.createEnemy(Face.randomFace(), location.add(OFFSET), room, EnemyType.SPIDER_BOSS));
                        } else if(parts[1].contains("obstacle1")) {
                            room.addObstacle(EntityFactory.createObstacle(location.add(OFFSET), obstacleType1, room));
                        } else if(parts[1].contains("obstacle2")) {
                            room.addObstacle(EntityFactory.createObstacle(location.add(OFFSET), obstacleType2, room));
                        } else if(parts[1].contains("hostagespawn")) {
                            room.addNeutral(EntityFactory.createNeutral(location.add(OFFSET), NeutralType.HOSTAGE, null));
                        } else if(parts[1].contains("celldoor")) {
                            room.addObstacle(EntityFactory.createObstacle(location.add(OFFSET), ObstacleType.CELLDOOR, room));
                        } else if(parts[1].contains("door")) {
                            Side side = Side.findByValue(parts[5].split("\"")[1]);
                            if(generateEntry[side.getIndex()].equals(EntryType.DOOR)) {
                                if(door != null && door.getSide().opposite().equals(side)) {
                                    room.addEntry(EntityFactory.createEntry(location.add(OFFSET), room, side, EntryType.DOOR, door));
                                } else {
                                    room.addEntry(EntityFactory.createEntry(location.add(OFFSET), room, side, EntryType.DOOR, null));
                                }
                            } else if (generateEntry[side.getIndex()].equals(EntryType.PORTAL)) {
                                Portal portal = (Portal) EntityFactory.createEntry(location.add(OFFSET), room, side, EntryType.PORTAL, null);
                                room.addEntry(portal);
                                room.addNeutral(EntityFactory.createNeutral(portal.getRigidBody().getLocation(), NeutralType.GATEKEEPER, portal));
                            } else {
                                room.addToForeground(EntityFactory.createTile(location.add(OFFSET)));
                            }
                        }
                        // ADD OTHER OBJECTS HERE
                    }
                }
            }
            return room;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } 
    }
    
    private static Exterior createOuter(String layout, int size, ArrayList<Interior> rooms) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(layout)));
            Exterior outer = new Exterior(0);
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                line = line.toLowerCase();
                
                //check foreground and tileset
                if(line.contains("foreground")) {
                    int x = 0, y = 0;
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("foreground")) {
                        String[] parts = line.toLowerCase().split(",");
                        for(int p = 0; p < parts.length; p++) {
                            if(Integer.parseInt(parts[p]) != -1) {
                                outer.addToForeground(EntityFactory.createTile((new Vector2f(x, y)).add(OFFSET)));
                            }
                            x += TILESIZE;
                        }
                        x = 0;
                        y += TILESIZE;
                    }
                }
                
                //objects
                if(line.contains("objects")) {
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("objects")) {
                        String[] parts = line.toLowerCase().split("\\s+");
                        int x = Integer.parseInt(parts[3].split("\"")[1]);
                        int y = Integer.parseInt(parts[4].split("\"")[1]);
                        Vector2f location = new Vector2f(x, y);
                        if(parts[1].contains("playerspawn")) {
                            outer.addPlayerSpawn(new Vector2i(x, y));
                        }
                        else if(parts[1].contains("door")) {
                            Side side = Side.findByValue(parts[5].split("\"")[1]);
                            int row = Integer.parseInt(parts[6].split("\"")[1]);
                            int col = Integer.parseInt(parts[7].split("\"")[1]); 

                            boolean doorLinked = false;
                            if(rooms.get(col+(row*size)) != null) {
                                for(Entry entry: rooms.get(col+(row*size)).getEntries()) {
                                    Door door = null;
                                    if(entry instanceof Door) {
                                        door = (Door)entry;
                                    }
                                    if(door != null && door.getSide().opposite().equals(side)) {
                                        outer.addEntry(EntityFactory.createEntry(location.add(OFFSET), outer, side, EntryType.DOOR, door));
                                        doorLinked = true;
                                        break;
                                    }
                                }
                            }
                            if(!doorLinked) {
                                outer.addToForeground(EntityFactory.createTile(location.add(OFFSET)));
                            }
                        }
                        // ADD OTHER OBJECTS HERE
                    }
                }
            }
            return outer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } 
    }
}
