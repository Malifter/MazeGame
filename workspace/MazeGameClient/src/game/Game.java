package game;

import java.util.ArrayList;
import java.util.List;
import engine.Vector2i;
import engine.inputhandler.Button;
import engine.inputhandler.Input;
import engine.inputhandler.PhysicalInput;
import engine.render.Animator;
import engine.render.IDisplay;
import engine.render.JLWGLDisplay;
import engine.serializable.SerializedEntity;
import engine.serializable.SerializedGameState;
import engine.serializable.SerializedObject;
import engine.serializable.SerializedPlayer;
import engine.serializable.SerializedRoom;
import engine.serializable.SerializedSound;
import game.enums.GameState;
import game.enums.Sound;

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
    private static boolean done = false;
    private static transient IDisplay display;
    private static ArrayList<Input> inputs = null;
    private static int theWidth = 1024;
    private static int theHeight = 768;
    private static RenderableEntity winScreen = null;
    private static RenderableEntity loseScreen = null;
    public static RenderableLevel level;
    private static Camera cam = null;
    public static GameState state = GameState.PLAYING;
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
    public static Button useItem = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_SPACE});
    
    public static int sound_hit;
    public static int sound_shot;
    public static int sound_deflect;
    public static int sound_spawn;
    public static int sound_dead;
    public static int BGM_quickman;
    //private static long timeBGM = 0;
    
    /**
     * Constructor - private to prevent instance creation
     */
    private Game(){}
    
    public static void init() {
        initDisplay();
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
        cameraMode.setDown(false);
        cam.mode = true;
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
        inputs.add(selectForward);
        inputs.add(selectBackward);
        inputs.add(useItem);
        
        initSounds();
        
        //timeBGM = GameEngine.getTime();
        //GameEngine.playMusic(BGM_quickman);
        GameEngine.playSound(sound_spawn);
        
        //GameEngine.setMouseHidden(true);
        winScreen = new RenderableEntity("UI/winScreen.gif", new Vector2i(theWidth/2, theHeight/2));
        loseScreen = new RenderableEntity("UI/loseScreen.gif", new Vector2i(theWidth/2, theHeight/2));

        return inputs;
    }
    
    public static ArrayList<RenderableEntity> getEntities() {
        ArrayList<RenderableEntity> tmp = new ArrayList<RenderableEntity>();
        if(state.equals(GameState.PLAYING)){
            RenderableRoom r = level.getCurrentRoom();
            for(RenderableEntity bg: r.getBackground()) {
                if(bg != null) tmp.add(bg);
            }
            for(RenderableEntity fg: r.getForeground()) {
                if(fg != null) tmp.add(fg);
            }
        }
        else if(state.equals(GameState.WIN)){
            tmp.add(winScreen);
        }
        else {
            tmp.add(loseScreen);
        }
        return tmp;
    }
    
    private static void initSounds() {
        for(Sound sound: Sound.values()) {
            GameEngine.addSound(sound.getFilePath());
        }
        //sound_hit = GameEngine.addSound("hit.wav");
        //sound_shot = GameEngine.addSound("shot.wav");
        /*sound_spawn = GameEngine.addSound("spawn.wav");
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
            ArrayList<SerializedEntity> entities = new ArrayList<SerializedEntity>();
            for(SerializedObject object: updateObjects) {
                if(object instanceof SerializedRoom) {
                    SerializedRoom room = (SerializedRoom) object;
                    if(level.getCurrentIndex() != room.getIndex()) {
                        level.setCurrentRoom(room.getIndex());
                        if(room.getIndex() != 0) {
                            cam.setFocusObject(null);
                            cam.setOrientation(room.getPosition().x, room.getPosition().y-36, 0, 1);
                        }
                        Animator.clear();
                    }
                } else if(object instanceof SerializedGameState) {
                    SerializedGameState gamestate = (SerializedGameState) object;
                    state = gamestate.getGameState();
                } else if(object instanceof SerializedPlayer) {
                    SerializedEntity player = (SerializedEntity) object;
//                    GUI.populate(sp.getItems());
//                    GUI.setPlayerHealth(sp.getHealth());
//                    GUI.setPlayerLives(sp.getLives());
                    if(level.getCurrentIndex() == 0) {
                        cam.setFocusObject(player);
                    }
                    entities.add(player);
                } else if(object instanceof SerializedEntity) {
                    SerializedEntity entity = (SerializedEntity) object;
                    entities.add(entity);
                } else if(object instanceof SerializedSound) {
                    SerializedSound sound = (SerializedSound) object;
                    // TODO: Not all sounds are recovered from the server because the server only attempts to send once
                    // And it is spamming the sends so it is lost sometimes.
                    // Perhaps instead of spamming information we should lower it to like 60 times per second or maybe
                    // 30 updates per second. Something less severe. This might allow all updates to make it
                    // That or we should have sounds only on the client side, and the client should be smart enough to play
                    // the current sounds based off of what is received.
                    // If get new projectile, then sound is produced on the first reception.
                    // If it's an enemy maybe sounds are played based off of animation state
                    // or possibly off of a sound state. (walking/running/attacking/idle/ambient)
                    // TODO: An addition solution can be simply to make the sound an Entity
                    // and give it an update function on the server and a live time equal to 175
                    // like the effects. Then you simply just have it send it to the client over
                    // and over with it's unique id, and the client knows to just play it once
                    // sort of like the animator does with the effects
                    GameEngine.playSound(sound.getSound());
                }
            }
            Animator.setEntities(entities);
        }
        cam.update();
        
        if(escape.isDown()) {
            done = true;
        }     
        if(!state.equals(GameState.PLAYING)){
            cameraMode.setDown(true);
            cam.mode = false;
            if(startGame.isDown()) {
                done = true;
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
        return done;
    }
}

