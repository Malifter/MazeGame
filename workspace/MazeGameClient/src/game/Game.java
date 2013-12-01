package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import engine.Vector2i;
import engine.Vector2f;
import engine.inputhandler.Button;
import engine.inputhandler.Input;
import engine.inputhandler.PhysicalInput;
import engine.render.IDisplay;
import engine.render.JLWGLDisplay;
import engine.serializable.SerializedEntity;
import engine.serializable.SerializedObject;
import engine.serializable.SerializedPlayer;
import engine.serializable.SerializedRoom;

/*
* Classname:            MazeGameClient.java
*
* Version information:  1.0
*
* Date:                 11/3/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * MazeGameClient: This is our MazeGame game
 */
public class Game {
    private static boolean isDone = false;
    private static transient IDisplay display;
    private static ArrayList<Input> inputs = null;
    private static int theWidth = 1024;
    private static int theHeight = 768;
    private static RenderableEntity winScreen = null;
    private static RenderableEntity loseScreen = null;
    private static RenderableEntity startScreen = null;
    public static RenderableLevel level;
    public static Map<String, Vector2f> destinations;
    private static Camera cam = null;
    private static boolean win = false;
    private static boolean isStart = false;
    private static boolean lose = false;
    public static Button right = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_RIGHT, PhysicalInput.KEYBOARD_D });
    public static Button left = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_LEFT, PhysicalInput.KEYBOARD_A });
    public static Button up = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_UP, PhysicalInput.KEYBOARD_W });
    public static Button down = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_DOWN, PhysicalInput.KEYBOARD_S });
    public static Button fire = new Button(
            new PhysicalInput[] { PhysicalInput.MOUSE_LEFT });
    public static Button escape = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_ESCAPE });
    public static Button pause = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_P });
    public static Button cameraMode = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_F2 });
    public static Button startGame = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_RETURN });
    public static Button selectForward = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_E});
    public static Button selectBackward = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_Q});
    
    public static int sound_hit;
    public static int sound_shot;
    public static int sound_deflect;
    public static int sound_spawn;
    public static int sound_dead;
    public static int BGM_quickman;
    private static long timeBGM = 0;
    
    /**
     * Constructor - private to prevent instance creation
     */
    private Game(){}
    
    public static void init() {
        initDisplay();
        destinations = new HashMap<String, Vector2f>();
        cam = new Camera();
    }
    
    public static void initDisplay() {
        display = new JLWGLDisplay("MazeGame", theWidth, theHeight);
    }
    
    public static IDisplay getDisplay() {
        return display;
    }
    
    public static ArrayList<Input> initInputs(List<SerializedRoom> levelLayout) {
        level = new RenderableLevel();
        level.addRoom(RenderableLevel.createOuter(levelLayout.get(0).getIndex()));
        for(int i = 1; i < levelLayout.size(); i++) {
            level.addRoom(RenderableLevel.createRoom(levelLayout.get(i).getIndex(), levelLayout.get(i).getPosition()));
        }
        cam.setOrientation(512,0,0,1);
        inputs = new ArrayList<Input>();
        inputs.add(left);
        inputs.add(right);
        inputs.add(up);
        inputs.add(down);
        inputs.add(fire);
        inputs.add(escape);
        inputs.add(pause);
        inputs.add(cameraMode);
        inputs.add(startGame);
        
        initSounds();
        
        timeBGM = GameEngine.getTime();
        //GameEngine.playMusic(BGM_quickman);
        GameEngine.playSound(sound_spawn);
        
        //GameEngine.setMouseHidden(true);
        startScreen = new RenderableEntity("UI/startScreen.gif", new Vector2i());
        winScreen = new RenderableEntity("UI/winScreen.gif", new Vector2i());
        loseScreen = new RenderableEntity("UI/game_over.gif", new Vector2i());
        
        return inputs;
    }
    
    public static ArrayList<RenderableEntity> getEntities() {
        ArrayList<RenderableEntity> tmp = new ArrayList<RenderableEntity>();
        if(isStart){
            if(!win && !lose){
                /*tmp.addAll(background);
                tmp.addAll(foreground);
                tmp.addAll(traps);*/
                //for(RenderableRoom r: level.getRooms()) {
                RenderableRoom r = level.getCurrentRoom();
                for(RenderableEntity bg: r.getBackground()) {
                    if(bg != null) tmp.add(bg);
                }
                //}
                //for(RenderableRoom r: level.getRooms()) {
                for(RenderableEntity fg: r.getForeground()) {
                    if(fg != null) tmp.add(fg);
                }
                //}
                /*for(RenderableRoom r: renderableRooms) {
                    for(Entity t: r.getTraps()) {
                        if(t != null) tmp.add(t);
                    }
                }*/
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
        }
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
    
    public ArrayList<Input> getInputs() {
        return inputs;
    }

    /*
     * (non-Javadoc)
     * @see IGame#update(long)
     */
    public static void update(long time, List<SerializedObject> updateObjects) {
        //if((GameEngine.getTime()-timeBGM) > 38000) {
            //timeBGM = GameEngine.getTime();
            //GameEngine.playMusic(BGM_quickman);
        ///=}
        if(updateObjects != null) {
            for(SerializedObject so: updateObjects) {
                if(so instanceof SerializedRoom) {
                    SerializedRoom sr = (SerializedRoom) so;
                    level.setCurrentRoom(sr.getIndex());
                    if(sr.getIndex() != 0) {
                        cam.setFocusObject(null);
                        cam.setOrientation(sr.getPosition().x, sr.getPosition().y-36, 0, 1);
                    }
                }
                else if(so instanceof SerializedPlayer) {
                    SerializedPlayer sp = (SerializedPlayer) so;
                    if(level.getCurrentIndex() == 0) {
                        cam.setFocusObject(sp);
                    }
                }  
                /*
                else if(so instanceof SerializedEntity) {
                    if(sp.needsDelete()) {
                        entities.remove(so.getID());
                        destinations.remove(so.getID());
                    } else {
                        destinations.put(se.getID(), se.getPosition());
                        entities.put(so.getID(), so);
                    }
                }
                 */
            }
        }
        /*
        if(player.needsDelete()) {
            lose = true;
            cameraMode.setDown(true);
            cam.mode = false;
        }
        //cam.setFocusEntity(player);
        player.update(time);
        *//*
        for(Map.Entry<String, SerializedObject> entry: entities.entrySet()) {
            if(entry.getValue().getPosition() != destinations.get(entry.getKey())) {
                entry.getValue().setPosition(destinations.get(entry.getKey()));
            }
        }*/
        
        cam.update();
        
        if(escape.isDown()) {
            isDone = true;
        }     
        if(!isStart){
            cameraMode.setDown(true);
            cam.mode = false;
            
            if(startGame.isDown()){
                isStart = true;
                cameraMode.setDown(false);
                cam.mode = true;
            }
       }
        if(win){
            cameraMode.setDown(true);
            cam.mode = false;
        }
    }

    public void setWin(boolean w) {
        win = w;
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
}

