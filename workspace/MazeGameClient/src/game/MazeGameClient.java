package game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

import engine.Vertex2;
import engine.Vertex2f;
import engine.inputhandler.Button;
import engine.inputhandler.Input;
import engine.inputhandler.PhysicalInput;
import engine.render.JLWGLDisplay;
import engine.serializable.SerializedEntity;
import engine.serializable.SerializedObject;
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
public class MazeGameClient extends Game {
    ArrayList<Input> inputs = null;
    private int theWidth = 1024;
    private int theHeight = 768;
    private Entity winScreen = null;
    private Entity loseScreen = null;
    private Entity startScreen = null;
    /*private ArrayList<Entity> background = null;
    private ArrayList<Entity> foreground = null;
    private ArrayList<Entity> traps = null;*/
    public RenderableLevel level;
    public Map<String, Vertex2f> destinations;
    private Camera cam = null;
    private String theAssetsPath = "assets/";
    private boolean win = false;
    private boolean isStart = false;
    private boolean lose = false;
    public Button right = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_RIGHT, PhysicalInput.KEYBOARD_D });
    public Button left = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_LEFT, PhysicalInput.KEYBOARD_A });
    public Button up = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_UP, PhysicalInput.KEYBOARD_W });
    public Button down = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_DOWN, PhysicalInput.KEYBOARD_S });
    public Button fire = new Button(
            new PhysicalInput[] { PhysicalInput.MOUSE_LEFT });
    public Button escape = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_ESCAPE });
    public Button pause = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_P });
    public Button cameraMode = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_F2 });
    public Button startGame = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_RETURN });
    
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
    
    public int sound_hit;
    public int sound_shot;
    public int sound_deflect;
    public int sound_spawn;
    public int sound_dead;
    public int BGM_quickman;
    private long timeBGM = 0;
    
    /**
     * Constructor
     * 
     * @param e
     */
    public MazeGameClient(GameEngine e) throws Exception {
        super(e);
        setDisplay(new JLWGLDisplay("MazeGame", theWidth, theHeight));
        destinations = new HashMap<String, Vertex2f>();
        cam = new Camera(this);
    }
    
    @Override
    public ArrayList<Entity> getEntities() {
        ArrayList<Entity> tmp = new ArrayList<Entity>();
        if(isStart){
            if(!win && !lose){
                /*tmp.addAll(background);
                tmp.addAll(foreground);
                tmp.addAll(traps);*/
                //for(RenderableRoom r: level.getRooms()) {
                RenderableRoom r = level.getCurrentRoom();
                for(Entity bg: r.getBackground()) {
                    if(bg != null) tmp.add(bg);
                }
                //}
                //for(RenderableRoom r: level.getRooms()) {
                for(Entity fg: r.getForeground()) {
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
    
    @Override
    public ArrayList<Input> initInputs(List<SerializedRoom> levelLayout) {
        level = new RenderableLevel(this);
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
        startScreen = new EnvironmentTile(this, "UI/startScreen.gif", 0, 0);
        winScreen = new EnvironmentTile(this, "UI/winScreen.gif", 0, 0);
        loseScreen = new EnvironmentTile(this, "UI/game_over.gif", 0, 0);
        
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
    
    public ArrayList<Input> getInputs() {
        return inputs;
    }

    /*
     * (non-Javadoc)
     * @see IGame#update(long)
     */
    @Override
    public void update(long time, List<SerializedObject> updateObjects) {
        if((GameEngine.getTime()-timeBGM) > 38000) {
            //timeBGM = GameEngine.getTime();
            //GameEngine.playMusic(BGM_quickman);
        }
        if(updateObjects != null) {
            boolean first = true;
            for(SerializedObject so: updateObjects) {
                if(so instanceof SerializedRoom) {
                    SerializedRoom sr = (SerializedRoom) so;
                    System.out.println(sr.getIndex());
                    level.setCurrentRoom(sr.getIndex());
                    if(sr.getIndex() != 0) {
                        cam.setFocusObject(null);
                        cam.setOrientation(sr.getPosition().getX(), sr.getPosition().getY()-48, 0, 1);
                    }
                }
                else if(so instanceof SerializedEntity) {
                    SerializedEntity se = (SerializedEntity) so;
                    if(se.needsDelete()) {
                        //entities.remove(so.getID());
                        destinations.remove(so.getID());
                    } else {
                        if(first) {
                            if(level.getCurrentIndex() == 0) {
                                cam.setFocusObject(se);
                            }
                            first = false;
                        }
                        destinations.put(se.getID(), se.getPosition());
                        //entities.put(so.getID(), so);
                    }
                }  
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
        
        camera();
        
        if(escape.isDown()) {
            isDone = true;
        }     
        if(!isStart){
            cameraMode.setDown(true);
            cam.mode = false;
            
            if(this.startGame.isDown()){
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
    
    private void camera() {
        cam.update();
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}

