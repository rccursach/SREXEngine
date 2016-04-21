package cl.srex.srexfetcherdaemon.server;


import cl.srex.srexfetcherdaemon.fetcher.engines.EngineFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.apache.log4j.Logger;
import cl.srex.srexindexer.IndexManager;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rccursach
 */
public class ClientManager implements Runnable{
    
    private Socket cli = null;

    public ClientManager(Socket sock) {
        this.cli = sock;
    }

    @Override
    public void run() {

        SearchManager sm = new SearchManager(EngineFactory.engines.BING, String.valueOf(cli.getPort()));
        Logger logger = org.apache.log4j.Logger.getLogger(ClientManager.class);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cli.getInputStream()));
            PrintWriter out = new PrintWriter(cli.getOutputStream(), true);
            //DataOutputStream out = new DataOutputStream(new BufferedOutputStream(cli.getOutputStream()));
            String inputLine, outputLine;
            
            System.out.println("Client connected on " + String.valueOf(cli.getPort()));

            //while( (inputLine = in.readLine()) != null && cli.isConnected()){
            while( (inputLine = in.readLine()) != null ) {
                //outputLine = String.format("server got: %s", inputLine);
                //System.out.println(outputLine);
                
                if(inputLine.equals("close_sock")){
                    break;
                }
                
                logger.info("Mensaje recibido..");
                
                //out.println(outputLine);
                sm.search(inputLine);
                logger.info("done searching");
                IndexManager idxMan = new IndexManager(String.valueOf(cli.getPort()));
                idxMan.createIndex();
                logger.info("done indexing");
                //out.println("done");
                //idxMan.getTerms();
                out.println(idxMan.getTerms()); // Replaced with DataOutputStream's .writeUTF8()
                //out.writeUTF(idxMan.getTerms());
                //out.flush();
                
                logger.info("done building terms vector");
                
                if ( Thread.interrupted()){
                    System.out.println("Closing connections...");
                    try {
                        in.close();
                        out.close();
                        cli.close();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Could not close socket");
                        System.exit(-1);
                    }
                }
            }
            System.out.println("Client disconnected from " + String.valueOf(cli.getPort()));
            cli.close();
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    
}
