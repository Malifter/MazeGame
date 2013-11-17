package game;

/*
* Classname:            RenderableLevel
*
* Version information:  1.0
*
* Date:                 11/17/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import engine.Vertex2;
import game.RenderableRoom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class RenderableLevel {
    public static MazeGameClient game; //temporary
    private final static String GIF = ".gif";
    private final static String layoutPath = "assets/layouts/";
    private final static String exteriorLayout = layoutPath+"exterior/OutsideLayout.oel";
    private final static String roomLayouts[] = {layoutPath+"rooms/RoomLayout0.oel", layoutPath+"rooms/RoomLayout1.oel"};
    private final static String hostageLayout = layoutPath+"rooms/RoomLayout0.oel";//layoutPath+"hostage/HostageRoomLayout.oel";
    private final static String tilesetPath = "tilesets/";
    
    private ArrayList<RenderableRoom> rooms = new ArrayList<RenderableRoom>();
    private int currentRoom = 0;
    
    public RenderableLevel(MazeGameClient game) {
        this.game = game;
    }
    
    public void addRoom(RenderableRoom room) {
        rooms.add(room);
    }
    
    public ArrayList<RenderableRoom> getRooms() {
        return rooms;
    }
    
    public void setCurrentRoom(int currentRoom) {
        this.currentRoom = currentRoom;
    }
    
    public RenderableRoom getCurrentRoom() {
        return rooms.get(currentRoom);
    }
    
    public int getCurrentIndex() {
        return currentRoom;
    }
    
    /**
     * loadLevel: Loads a level from a .oel (XML file)
     * 
     * @param filename
     */
    public static RenderableRoom createRoom(int layout, Vertex2 position) {
        try {
            RenderableRoom renderableRoom = new RenderableRoom();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(roomLayouts[layout])));
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                line = line.toLowerCase();

                //check foreground and tileset
                if(line.contains("foreground")) {
                    int x = position.getX(), y = position.getY();
                    String tileset = line.split("\"")[1];
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("foreground")) {
                        String[] parts = line.toLowerCase().split(",");
                        for(int p = 0; p < parts.length; p++) {
                            if(Integer.parseInt(parts[p]) != -1) {
                                renderableRoom.addToForeground(new EnvironmentTile(game, tilesetPath+tileset+"/"+parts[p]+GIF, x, y/*new Vertex2(x, y)*/));
                            }
                            x += EnvironmentTile.TILESIZE;
                        }
                        x = position.getX();
                        y += EnvironmentTile.TILESIZE;
                    }
                }
                
                if(line.contains("background")) {
                    int x = position.getX(), y = position.getY();
                    String tileset = line.split("\"")[1];
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("background")) {
                        String[] parts = line.toLowerCase().split(",");
                        for(int p = 0; p < parts.length; p++) {
                            if(Integer.parseInt(parts[p]) != -1) {
                                renderableRoom.addToBackground(new EnvironmentTile(game, tilesetPath+tileset+"/"+parts[p]+GIF, x, y));
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
                        /*if(parts[1].contains("Spike")) {
                            renderableRoom.addTrap(new EnvironmentTile(this, "animations/spikes/spikeFloor.gif", x, y));
                        }
                        else if(parts[1].contains("cSpike")) {
                            renderableRoom.addTrap(new EnvironmentTile(this, "animations/spikes/spikeCeiling.gif", x, y));
                        }*/
                        // ADD OTHER OBJECTS HERE
                    }
                }
            }
            return renderableRoom;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static RenderableRoom createOuter(int layout) {
        try {
            RenderableRoom renderableRoom = new RenderableRoom();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(exteriorLayout)));
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                line = line.toLowerCase();

                //check foreground and tileset
                if(line.contains("foreground")) {
                    int x = 0, y = 0;
                    String tileset = line.split("\"")[1];
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("foreground")) {
                        String[] parts = line.toLowerCase().split(",");
                        for(int p = 0; p < parts.length; p++) {
                            if(Integer.parseInt(parts[p]) != -1) {
                                renderableRoom.addToForeground(new EnvironmentTile(game, tilesetPath+tileset+"/"+parts[p]+GIF, x, y/*new Vertex2(x, y)*/));
                            }
                            x += EnvironmentTile.TILESIZE;
                        }
                        x = 0;
                        y += EnvironmentTile.TILESIZE;
                    }
                }
                
                if(line.contains("background")) {
                    int x = 0, y = 0;
                    String tileset = line.split("\"")[1];
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("background")) {
                        String[] parts = line.toLowerCase().split(",");
                        for(int p = 0; p < parts.length; p++) {
                            if(Integer.parseInt(parts[p]) != -1) {
                                renderableRoom.addToBackground(new EnvironmentTile(game, tilesetPath+tileset+"/"+parts[p]+GIF, x, y));
                            }
                            x += EnvironmentTile.TILESIZE;
                        }
                        x = 0;
                        y += EnvironmentTile.TILESIZE;
                    }
                }
                
                //objects
                if(line.contains("objects")) {
                    while((line = bufferedReader.readLine()) != null && !line.toLowerCase().contains("objects")) {
                        String[] parts = line.toLowerCase().split("\\s+");
                        int x = Integer.parseInt(parts[3].split("\"")[1]);
                        int y = Integer.parseInt(parts[4].split("\"")[1]);
                        // NOTE WHEN SPAWNING NEW ENEMIES CHANGE CONSTRUCTOR TO TAKE IN ENUM FACE.RIGHT/LEFT/UP/DOWN
                        /*In this case it will be the safe zones perhaps*/
                        // ADD OTHER OBJECTS HERE
                    }
                }
            }
            return renderableRoom;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}