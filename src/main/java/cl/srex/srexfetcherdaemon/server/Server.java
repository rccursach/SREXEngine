/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexfetcherdaemon.server;

import java.io.IOException;
import java.net.ServerSocket;
//import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author rccursach
 */
public class Server {

    private int port;
    private LinkedList<Thread> threads;

    /**
     * Server
     * @param port The port number to listen on
     */
    public Server(int port) {
        this.port = port;
    }
    

    /**
     * start
     * @throws IOException 
     */
    public void start() throws IOException {
        
        try {
            ServerSocket srv = new ServerSocket(port);
            System.out.println(String.format("Server listening on port %d", port));
            
            
            /**
             * Main loop
             */
            while(true){
                Thread clm = new Thread(new ClientManager(srv.accept()));
                //threads.add(clm);
                clm.start();
                //System.out.println("New Threaded connection!");
                
                /**
                 * Check for dead threads
                 */
                /*
                for (Iterator<Thread> iterator = threads.iterator(); iterator.hasNext();) {
                    Thread t = iterator.next();
                    if(!t.isAlive()){
                        System.out.println(String.format("Removing thread %d",t.getId()) );
                        threads.remove(t);
                    }
                }
                System.out.println( String.format("\n%d clients connected.", threads.size()) );
                */
            }
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
        
    }
    
}
