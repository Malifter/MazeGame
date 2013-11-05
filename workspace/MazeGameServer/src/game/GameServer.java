package game;
/*
* Classname:            GameServer.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Skeleton Game Server
*/
public class GameServer implements Runnable{
    protected final static int KEEP_ALIVE_TIME = 60; // in seconds
    protected final static int THREADPOOL_SIZE = 4;
    protected final static int CONNECTION_QUEUE_SIZE = 4;
    protected final static int SOCKET_TIMEOUT = 1000;
    protected final static int SERVER_PORT = 10500;

    protected static BlockingQueue<Runnable> blockingQueue;
    protected static ThreadPoolExecutor threadPoolExecutor;

    protected static ServerSocket serverSocket;
    protected static Map<SocketAddress, Integer> playerMap;
    protected static GameEngine engine;
    
    public GameServer(GameEngine engine) throws IOException {
        this.engine = engine;
    }
    
    /**
     * main
     */
    public void run() {
        try {
            playerMap = new HashMap<SocketAddress, Integer>();
            setupThreading();
            createServerSocket();
 
            boolean inLobby = true; // need to implement fine details later
            while(inLobby) {
                acceptClients(); // handles
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        protectedServerClose();
    }
    
    /**
     * Accepts a game client and then passes 
     */
    protected static void acceptClients() {
        Socket clientSocket = null;
        try {
            // We only need to do this if it's a brand new connection (up to 4 players)
            clientSocket = serverSocket.accept();
            Integer playerID = playerMap.get(clientSocket.getRemoteSocketAddress());
            if(playerID == null) {
                playerID = playerMap.size();
                playerMap.put(clientSocket.getRemoteSocketAddress(), playerID);
            }
            if(playerID != 0) { // tmp since don't know spawn location
                MazeGameServer g = ((MazeGameServer) engine.theGame);
                g.rooms.get(0).addPlayer(new Player(engine.theGame, "spawn1.gif", g.rooms.get(0).getCenter().getX(),
                        g.rooms.get(0).getCenter().getY(), g.rooms.get(0).getCenter().getX()+11, g.rooms.get(0).getCenter().getY()+19, 12, 11, 3, playerID));
                //engine.clientInputs.add(new ArrayList<Integer>());
                engine.inputLocks.add(new ReentrantLock());
            }
            System.out.println(playerID);
            threadPoolExecutor.execute(new PlayerHandlerThread(clientSocket, playerID, engine));
        }
        catch(IOException ignore) {}
    }
 
    /**
     * Set up the threadpool
     */
    protected static void setupThreading() {
        blockingQueue = new ArrayBlockingQueue<Runnable>(CONNECTION_QUEUE_SIZE);
        threadPoolExecutor = new ThreadPoolExecutor(THREADPOOL_SIZE,
                THREADPOOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                blockingQueue); 
    }

    /**
     * Set up the server socket
     */
    protected static void createServerSocket() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.setSoTimeout(SOCKET_TIMEOUT);

        SocketAddress socketAddress = new InetSocketAddress(SERVER_PORT);
        serverSocket.bind(socketAddress);
    }
    
    /**
     * Exception free closing of server socket
     */
    protected static void protectedServerClose() {
        try {
            serverSocket.close();
        }
        catch(Exception ignore) {}
    }
}
