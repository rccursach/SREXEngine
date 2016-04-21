/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexindexer;

import java.io.IOException;

/**
 *
 * @author rccursach
 */
public class Main {
    
    public static void main(String[] args) {
        
        try {
            IndexManager idxMan = new IndexManager("result");
            idxMan.createIndex();
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
}
