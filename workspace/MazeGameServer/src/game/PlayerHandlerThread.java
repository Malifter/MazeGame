package game;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import engine.serializable.SerializedObject;
import engine.serializable.SerializedRoom;
import game.levelloader.LevelLoader;

/*
* Classname:            PlayerHandlerThread.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/
 
/**
* Skeleton game server thread
*
* @version 1.0 1 Nov 2013
* @author Garrett Benoit
*/
public class PlayerHandlerThread implements Runnable {

    protected final static int READ_TIMEOUT = 1000;

    /** Socket used to represent client socket within thread. */
    private final Socket socket;
    private final int playerID;
    private final GameEngine engine;
    
    private String clientPort;
    private String clientAddress;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
 
    /**
     * Initializer Constructor that initializes the thread with the
     * client for with the thread was created.
     * 
     * @param socket            The socket connected to the client.
     * @throws IOException      If an input or output exception occurs.
     */
    public PlayerHandlerThread(Socket socket, int playerID, GameEngine engine) throws IOException {
        this.socket = socket;
        this.playerID = playerID;
        this.engine = engine;
    }
 
    /**
     * The run function is used to centralize most of the error handling
     * that is required for the client communication and threads. It also
     * calls a series of functions in order to follow the specific
     * procedures required when communicating with a client.
     */
    public void run() {
        try {
            
            // Setup initial socket properties.
            setUp();
            sendLevelToClient(LevelLoader.getLevelLayout());
            while(engine.playingGame) {                
                List<GameEngine.Pressed> inputs = checkForClientInputs();
                if(inputs != null) {
                    engine.setInputs(inputs, playerID); // lock and apply inputs
                }
                
                List<SerializedObject> updates = engine.getUpdates(); // lock and get updates
                if(updates.size() > 0) {
                    sendUpdatesToClient(updates);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        protectedClose();
    }
 
    /**
     * Provides an all-purpose, full-scale setup in order to allow the
     * thread to be prepared for client communication. Is similar in
     * function to a constructor.
     * 
     * @throws IOException                  If an input or output exception
     *                                      occurs.
     * @throws DNSTerminateClientException  If the client socket reports
     *                                      an invalid IP Address or Port
     *                                      Number.
     */
    protected void setUp() throws IOException {
            clientAddress = socket.getInetAddress().getHostAddress();
            clientPort = Integer.toString(socket.getPort());
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());
            socket.setSoTimeout(READ_TIMEOUT);
            
        System.out.println("Handling player " + playerID + " on client "+ clientAddress
                + "-" + clientPort + " with thread id "
                    + Thread.currentThread().getId());
        }

    /**
     * Checks if any client inputs have been sent
     * @throws ClassNotFoundException 
     */
    @SuppressWarnings("unchecked")
    protected List<GameEngine.Pressed> checkForClientInputs() throws IOException, ClassNotFoundException {
        try {
            return (ArrayList<GameEngine.Pressed>) ois.readObject();
        } catch(SocketTimeoutException ignore) {}
        catch(ClassNotFoundException ignore) {}
        catch(ClassCastException ignore) {}
        return null;
    }
    
    /**
     * Sends all updates for a room to the client
     */
    protected void sendUpdatesToClient(List<SerializedObject> updates) throws IOException {
        oos.writeObject(updates);
    }
    
    protected void sendLevelToClient(ArrayList<SerializedRoom> levelLayout) throws IOException {
        oos.writeObject(levelLayout);
    }

    /**
     * Exception free socket close
     */
    protected void protectedClose() {
        try {
            socket.close();
        }
        catch(Exception ignore) {}
    }
}