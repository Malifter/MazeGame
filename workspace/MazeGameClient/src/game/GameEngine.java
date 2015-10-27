package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import net.java.games.input.*;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import engine.inputhandler.Axis;
import engine.inputhandler.Button;
import engine.inputhandler.Input;
import engine.inputhandler.PhysicalInput;
import engine.render.Animator;
import engine.render.Animator.AnimationInfo;
import engine.render.IDisplay;
import engine.render.Sprite;
import engine.serializable.SerializedEffect;
import engine.serializable.SerializedEntity;
import engine.serializable.SerializedInputs;
import engine.serializable.SerializedObject;
import engine.serializable.SerializedObstacle;
import engine.serializable.SerializedPlayer;
import engine.serializable.SerializedRoom;
import engine.soundmanager.SoundManager;
import game.enums.GameState;
import game.enums.Pressed;

/*
* Classname:            GameEngine.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * GameEngine: Implementation of a game loop. Runs a game implementing the
 * IGame interface.
 */
public class GameEngine {
    /** the number of frames per second that we want to run the game at */
    private static final int FPS = 60;
    
    /** flag indicating that we're playing the game */
    private static boolean playingGame = true;
    
    /** a map of physical inputs to names */
    private static HashMap<PhysicalInput, String> inputMap;
    
    /** a list of inputs to listen for */
    private static ArrayList<Input> inputs;
    
    /** the number of timer ticks per second */
    private static long timerTicksPerSecond = Sys.getTimerResolution();
    
    /**
     * The time at which the last rendering looped started from the point
     * of view of the game logic
     */
    private static long lastLoopTime = getTime();
    
    /** SoundManager to make sound with */
    private static SoundManager soundManager;
    
    /** the display object to use */
    private static IDisplay display = null;

    /** The time since the last record of fps */
    private static long lastFpsTime;

    /** The recorded fps */
    private static int fps;
    
    protected final static int READ_TIMEOUT = 1000;
    protected final static int SERVER_PORT = 10500;
    protected final static String IP_ADDRESS = "localhost";

    protected static Socket socket;
    protected static ObjectInputStream ois;
    protected static ObjectOutputStream oos;
    
    /**
     * Constructor: Private to prevent instantiation.
     */
    private GameEngine(){}
    
    public static void init() {
        initInput();
        initSound();
    }
    
    private static void initInput() {
     // initialize input map
        inputMap = new HashMap<PhysicalInput, String>();
        
        // populate input map
        inputMap.put(PhysicalInput.KEYBOARD_BACK, "Keyboard:Back");
        inputMap.put(PhysicalInput.KEYBOARD_TAB, "Keyboard:Tab");
        inputMap.put(PhysicalInput.KEYBOARD_RETURN, "Keyboard:Return");
        inputMap.put(PhysicalInput.KEYBOARD_SPACE, "Keyboard: ");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT_SHIFT,
                "Keyboard:Left Shift");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT_CONTROL,
                "Keyboard:Left Control");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT_ALT, "Keyboard:Left Alt");
        inputMap.put(PhysicalInput.KEYBOARD_PAUSE, "Keyboard:Pause");
        inputMap.put(PhysicalInput.KEYBOARD_CAPS_LOCK,
                "Keyboard:Caps Lock");
        inputMap.put(PhysicalInput.KEYBOARD_ESCAPE, "Keyboard:Escape");
        inputMap.put(PhysicalInput.KEYBOARD_PG_UP, "Keyboard:Pg Up");
        inputMap.put(PhysicalInput.KEYBOARD_PG_DOWN, "Keyboard:Pg Down");
        inputMap.put(PhysicalInput.KEYBOARD_END, "Keyboard:End");
        inputMap.put(PhysicalInput.KEYBOARD_HOME, "Keyboard:Home");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT, "Keyboard:Left");
        inputMap.put(PhysicalInput.KEYBOARD_UP, "Keyboard:Up");
        inputMap.put(PhysicalInput.KEYBOARD_RIGHT, "Keyboard:Right");
        inputMap.put(PhysicalInput.KEYBOARD_DOWN, "Keyboard:Down");
        inputMap.put(PhysicalInput.KEYBOARD_SYSRQ, "Keyboard:SysRq");
        inputMap.put(PhysicalInput.KEYBOARD_INSERT, "Keyboard:Insert");
        inputMap.put(PhysicalInput.KEYBOARD_DELETE, "Keyboard:Delete");
        inputMap.put(PhysicalInput.KEYBOARD_0, "Keyboard:0");
        inputMap.put(PhysicalInput.KEYBOARD_1, "Keyboard:1");
        inputMap.put(PhysicalInput.KEYBOARD_2, "Keyboard:2");
        inputMap.put(PhysicalInput.KEYBOARD_3, "Keyboard:3");
        inputMap.put(PhysicalInput.KEYBOARD_4, "Keyboard:4");
        inputMap.put(PhysicalInput.KEYBOARD_5, "Keyboard:5");
        inputMap.put(PhysicalInput.KEYBOARD_6, "Keyboard:6");
        inputMap.put(PhysicalInput.KEYBOARD_7, "Keyboard:7");
        inputMap.put(PhysicalInput.KEYBOARD_8, "Keyboard:8");
        inputMap.put(PhysicalInput.KEYBOARD_9, "Keyboard:9");
        inputMap.put(PhysicalInput.KEYBOARD_A, "Keyboard:A");
        inputMap.put(PhysicalInput.KEYBOARD_B, "Keyboard:B");
        inputMap.put(PhysicalInput.KEYBOARD_C, "Keyboard:C");
        inputMap.put(PhysicalInput.KEYBOARD_D, "Keyboard:D");
        inputMap.put(PhysicalInput.KEYBOARD_E, "Keyboard:E");
        inputMap.put(PhysicalInput.KEYBOARD_F, "Keyboard:F");
        inputMap.put(PhysicalInput.KEYBOARD_G, "Keyboard:G");
        inputMap.put(PhysicalInput.KEYBOARD_H, "Keyboard:H");
        inputMap.put(PhysicalInput.KEYBOARD_I, "Keyboard:I");
        inputMap.put(PhysicalInput.KEYBOARD_J, "Keyboard:J");
        inputMap.put(PhysicalInput.KEYBOARD_K, "Keyboard:K");
        inputMap.put(PhysicalInput.KEYBOARD_L, "Keyboard:L");
        inputMap.put(PhysicalInput.KEYBOARD_M, "Keyboard:M");
        inputMap.put(PhysicalInput.KEYBOARD_N, "Keyboard:N");
        inputMap.put(PhysicalInput.KEYBOARD_O, "Keyboard:O");
        inputMap.put(PhysicalInput.KEYBOARD_P, "Keyboard:P");
        inputMap.put(PhysicalInput.KEYBOARD_Q, "Keyboard:Q");
        inputMap.put(PhysicalInput.KEYBOARD_R, "Keyboard:R");
        inputMap.put(PhysicalInput.KEYBOARD_S, "Keyboard:S");
        inputMap.put(PhysicalInput.KEYBOARD_T, "Keyboard:T");
        inputMap.put(PhysicalInput.KEYBOARD_U, "Keyboard:U");
        inputMap.put(PhysicalInput.KEYBOARD_V, "Keyboard:V");
        inputMap.put(PhysicalInput.KEYBOARD_W, "Keyboard:W");
        inputMap.put(PhysicalInput.KEYBOARD_X, "Keyboard:X");
        inputMap.put(PhysicalInput.KEYBOARD_Y, "Keyboard:Y");
        inputMap.put(PhysicalInput.KEYBOARD_Z, "Keyboard:Z");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT_WINDOWS,
                "Keyboard:Left Windows");
        inputMap.put(PhysicalInput.KEYBOARD_RIGHT_WINDOWS,
                "Keyboard:Right Windows");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_0, "Keyboard:Num 0");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_1, "Keyboard:Num 1");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_2, "Keyboard:Num 2");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_3, "Keyboard:Num 3");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_4, "Keyboard:Num 4");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_5, "Keyboard:Num 5");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_6, "Keyboard:Num 6");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_7, "Keyboard:Num 7");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_8, "Keyboard:Num 8");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_9, "Keyboard:Num 9");
        inputMap.put(PhysicalInput.KEYBOARD_MULTIPLY, "Keyboard:Multiply");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_PLUS, "Keyboard:Num +");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_MINUS, "Keyboard:Num -");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_DOT, "Keyboard:Num .");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_DIVIDE, "Keyboard:Num /");
        inputMap.put(PhysicalInput.KEYBOARD_F1, "Keyboard:F1");
        inputMap.put(PhysicalInput.KEYBOARD_F2, "Keyboard:F2");
        inputMap.put(PhysicalInput.KEYBOARD_F3, "Keyboard:F3");
        inputMap.put(PhysicalInput.KEYBOARD_F4, "Keyboard:F4");
        inputMap.put(PhysicalInput.KEYBOARD_F5, "Keyboard:F5");
        inputMap.put(PhysicalInput.KEYBOARD_F6, "Keyboard:F6");
        inputMap.put(PhysicalInput.KEYBOARD_F7, "Keyboard:F7");
        inputMap.put(PhysicalInput.KEYBOARD_F8, "Keyboard:F8");
        inputMap.put(PhysicalInput.KEYBOARD_F9, "Keyboard:F9");
        inputMap.put(PhysicalInput.KEYBOARD_F10, "Keyboard:F10");
        inputMap.put(PhysicalInput.KEYBOARD_F11, "Keyboard:F11");
        inputMap.put(PhysicalInput.KEYBOARD_F12, "Keyboard:F12");
        inputMap.put(PhysicalInput.KEYBOARD_F13, "Keyboard:F13");
        inputMap.put(PhysicalInput.KEYBOARD_F14, "Keyboard:F14");
        inputMap.put(PhysicalInput.KEYBOARD_F15, "Keyboard:F15");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_LOCK, "Keyboard:Num Lock");
        inputMap.put(PhysicalInput.KEYBOARD_SCROLL_LOCK,
                "Keyboard:Scroll Lock");
        inputMap.put(PhysicalInput.KEYBOARD_COMMA, "Keyboard:,");
        inputMap.put(PhysicalInput.KEYBOARD_DOT, "Keyboard:.");
        inputMap.put(PhysicalInput.KEYBOARD_TILDE, "Keyboard:~");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT_BRACKET, "Keyboard:[");
        inputMap.put(PhysicalInput.KEYBOARD_RIGHT_BRACKET, "Keyboard:]");
        inputMap.put(PhysicalInput.MOUSE_X, "Mouse:x");
        inputMap.put(PhysicalInput.MOUSE_Y, "Mouse:y");
        inputMap.put(PhysicalInput.MOUSE_Z, "Mouse:z");
        inputMap.put(PhysicalInput.MOUSE_LEFT, "Mouse:Left");
        inputMap.put(PhysicalInput.MOUSE_RIGHT, "Mouse:Right");
        inputMap.put(PhysicalInput.MOUSE_MIDDLE, "Mouse:Middle");
    }
    
    private static void initSound() {
        soundManager = new SoundManager();
        soundManager.initialize(8);
    }
    
    /**
     * setDisplay: Sets the display
     * 
     * @param aDisplay
     *            the display to use, if not null
     * @throws Exception
     *             if the display is null
     */
    private static void setDisplay(IDisplay iDisplay) throws Exception {
        if (iDisplay == null) {
            throw new Exception("Null display");
        }
        display = iDisplay;
    }
    
    /**
     * run: runs the game loop on a game implementing the IGame interface.
     * 
     * @param aGame
     *            the game to run.
     * @throws Exception
     */
    public static void run() throws Exception {
        Game.init();
        setDisplay(Game.getDisplay());
        display.init();
        createSocket();
        inputs = Game.initInputs(checkForServerLevel());
        gameLoop();
        //Game.shutdown();
    }
    
    /**
     * gameLoop: a timer-based game loop to run the game
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    private static void gameLoop() throws IOException, ClassNotFoundException {        
        // Game loop runs while the player is playing
        lastLoopTime = getTime();
        while (playingGame) {
            // clear screen
            display.reset();
            
            // Get the input
            getInputs();
            
            long elapsedTime = getTime() - lastLoopTime;
            lastLoopTime = getTime();
            lastFpsTime += elapsedTime;
            fps++;
            
            if (lastFpsTime > 1000) {
                Display.setTitle("(FPS: " + fps + ")");
                lastFpsTime = 0;
                fps = 0;
            }
            
            List<SerializedObject> updatedObjects = null;
            if(Game.state.equals(GameState.PLAYING)) {
                List<Pressed> inputs = new ArrayList<Pressed>();
                if(Game.right.isDown()) inputs.add(Pressed.RIGHT);
                if(Game.left.isDown()) inputs.add(Pressed.LEFT);
                if(Game.up.isDown()) inputs.add(Pressed.UP);
                if(Game.down.isDown()) inputs.add(Pressed.DOWN);
                if(Game.fire.isDown()) inputs.add(Pressed.FIRE);
                if(Game.escape.isDown()) inputs.add(Pressed.ESCAPE);
                if(Game.pause.isDown()) inputs.add(Pressed.PAUSE);
                //if(Game.cameraMode.isDown()) inputs.add("cameraMode");
                if(Game.startGame.isDown()) inputs.add(Pressed.START_GAME);
                if(Game.selectForward.isDown()) inputs.add(Pressed.SELECT_FORWARD);
                if(Game.selectBackward.isDown()) inputs.add(Pressed.SELECT_BACKWARD);
                if(Game.useItem.isDown()) inputs.add(Pressed.USE_ITEM);
                SerializedInputs sInputs = new SerializedInputs(inputs, display.getMouseCoordinates());
                
                // send inputs to server
                sendInputsToServer(sInputs);
                
                // Update the world
                updatedObjects = checkForServerUpdates();
            }
            Game.update(elapsedTime, updatedObjects);

            // render the graphics
            Animator.update(elapsedTime, updatedObjects);
            render();
            // draw GUI here or add last to render function
            
            // update window contents
            display.update();
            if (Game.isDone()) {
                playingGame = false;
            }
        }
        
        // clean up
        soundManager.destroy();
        
        // Close Game window
        display.quit();
    }
    
    /**
     * Creates the socket
     */
    protected static void createSocket()throws
            UnknownHostException, IOException {

        socket = new Socket(IP_ADDRESS, SERVER_PORT);
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        ois = new ObjectInputStream(socket.getInputStream());
        socket.setSoTimeout(READ_TIMEOUT);
    }
    
    /**
     * send inputs to server
     */
    protected static void sendInputsToServer(SerializedInputs sInputs) throws IOException {
        if(sInputs != null) oos.writeObject(sInputs);
    }
 
    /**
     * Wait for server to provide list of updates
     */
    @SuppressWarnings("unchecked")
    protected static List<SerializedObject> checkForServerUpdates() throws IOException, ClassNotFoundException {
        try {
            return (List<SerializedObject>) ois.readObject();
        } catch(Exception ignore) {}
        return null;
    }
    
    @SuppressWarnings("unchecked")
    protected static List<SerializedRoom> checkForServerLevel() {
        try {
            return (List<SerializedRoom>) ois.readObject();
        } catch(Exception ignore) {}
        return null;
    }
    
    /**
     * getInput: Get a list of the input components to track
     */
    private static void getInputs() {
        for (Input i : inputs) {
            boolean hasDown = false;
            float pollValue = 0.0f;
            String deviceType = "";
            for (PhysicalInput p : i.getPhysicalInputs()) {
                String physInputName = inputMap.get(p);
                String inputName = physInputName.substring(physInputName.indexOf(':') + 1);
                String inputType = physInputName.substring(0, physInputName.indexOf(':'));
                ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
                // Check each controller for input
                for (Controller controller : ce.getControllers()) {
                    deviceType = controller.getType().toString();
                    if (controller != null && hasDown == false && deviceType.equalsIgnoreCase(inputType)) {
                        for(Component com: controller.getComponents()) {
                            if (com.getName().equalsIgnoreCase(inputName)) {
                                controller.poll();
                                pollValue = com.getPollData();
                                if (i instanceof Button && pollValue == 1.0f) {
                                        hasDown = true;
                                }
                                break;
                            }
                        }
                    }
                    if(hasDown == true || pollValue > 0) { // Added pollValue > 0 --- might be wrong implementation for joystick
                        break;
                    }
                }
                
            }
            if (i instanceof Button) {
                if (hasDown) {
                    ((Button) i).setDown(true);
                } else {
                    ((Button) i).setDown(false);
                }
            } else if (i instanceof Axis) {
                ((Axis) i).setValue(pollValue);
            }
        }
    }
    
    /**
     * Get the high resolution time in milliseconds
     * 
     * @return The high resolution time in milliseconds
     */
    public static long getTime() {
        // we get the "timer ticks" from the high resolution timer
        // multiply by 1000 so our end result is in milliseconds
        // then divide by the number of ticks in a second giving
        // us a nice clear time in milliseconds
        return (Sys.getTime() * 1000) / timerTicksPerSecond;
    }
    
    /**
     * render: Syncs the display to FPS
     */
    public static void render() {
        display.sync(FPS);
        drawEntities();
        if(Animator.getSize() > 0 && Game.state.equals(GameState.PLAYING)) {
            HashMap<AnimationInfo, Float> renderMap = new HashMap<AnimationInfo, Float>();
            ValueComparator bvc = new ValueComparator(renderMap);
            TreeMap<AnimationInfo, Float> sortedMap = new TreeMap<AnimationInfo, Float>(bvc);
            ArrayList<AnimationInfo> renderLast = new ArrayList<AnimationInfo>();
            
            // Render Static Environment
            for(Entry<String, AnimationInfo> entry: Animator.getAnimations()) {
                SerializedObject so = entry.getValue().getEntity();
                if(so instanceof SerializedEffect) {
                    SerializedEffect s = (SerializedEffect) so;
                    if(!s.drawOnTop()) {
                        String path = Animator.getPath(entry.getValue());
                        if(path != null) {
                            Sprite sprite = Game.getDisplay().getSprite(path);
                            sprite.draw(s.getPosition().x.intValue(), s.getPosition().y.intValue());
                        } else {
                            //System.out.println("effect null");
                        }
                    } else {
                        // render later
                        renderLast.add(entry.getValue());
                    }
                }
                else if(so instanceof SerializedObstacle) {
                    SerializedObstacle s = (SerializedObstacle) so;
                    if(s.isMoveable()) {
                        renderMap.put(entry.getValue(), s.getPosition().y);
                    } else {
                        String path = Animator.getPath(entry.getValue());
                        if(path != null) {
                            Sprite sprite = Game.getDisplay().getSprite(path);
                            sprite.draw(s.getPosition().x.intValue(), s.getPosition().y.intValue());
                        } else {
                            //System.out.println("obstacle null");
                        }
                    }
                } else if(so instanceof SerializedPlayer) {
                    SerializedPlayer s = (SerializedPlayer) so;
                    renderMap.put(entry.getValue(), s.getPosition().y);
                } else if(so instanceof SerializedEntity) {
                    SerializedEntity s = (SerializedEntity) so;
                    renderMap.put(entry.getValue(), s.getPosition().y);
                }
            }
            
            // Render In y order
            sortedMap.putAll(renderMap);
            Iterator<AnimationInfo> mapItr = sortedMap.navigableKeySet().iterator();
            while(mapItr.hasNext()) {
                AnimationInfo animInfo = (AnimationInfo) mapItr.next();
                String path = Animator.getPath(animInfo);
                if(path != null) {
                    Sprite sprite = Game.getDisplay().getSprite(path);
                    SerializedEntity entity = animInfo.getEntity();
                    sprite.draw(entity.getPosition().x.intValue(), entity.getPosition().y.intValue());
                } else {
                    //System.out.println("entity null");
                }
            }
            
            // Render Last
            for(AnimationInfo animInfo: renderLast) {
                String path = Animator.getPath(animInfo);
                if(path != null) {
                    Sprite sprite = Game.getDisplay().getSprite(path);
                    SerializedEntity entity = animInfo.getEntity();
                    sprite.draw(entity.getPosition().x.intValue(), entity.getPosition().y.intValue());
                    System.out.println("path");
                } else {
                    //System.out.println("entity null");
                }
            }
        }
    }
    
    /**
     * drawEntities: calls draw on all the entities in the Game
     */
    public static void drawEntities() {
        for (RenderableEntity ent : Game.getEntities()) {
            if(ent != null) {
                ent.draw();
            }
        }
    }

    public static int addSound(String fileName) {
        return soundManager.addSound(fileName);
    }
    
    public static void playSound(int soundNum) {
        soundManager.playEffect(soundNum);
    }
    
    public static void playMusic(int soundNum) {
        soundManager.playSound(soundNum);
    }
    
    public static void setMouseHidden(boolean mouseHidden) {
        Mouse.setGrabbed(mouseHidden);
    }
    
}
