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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    
    public GameServer() throws IOException {
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
            GameEngine.newPlayerConnected(playerID);
            threadPoolExecutor.execute(new PlayerHandlerThread(clientSocket, playerID));
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
