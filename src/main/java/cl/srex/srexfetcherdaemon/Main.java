/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexfetcherdaemon;

import cl.srex.srexfetcherdaemon.server.Server;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rccursach
 */
public class Main {
    
    public static void main(String[] args) {
        
        Server s = new Server(3030);
        try {
            s.start();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
