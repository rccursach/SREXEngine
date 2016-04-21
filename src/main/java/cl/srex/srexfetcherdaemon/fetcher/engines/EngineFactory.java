/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexfetcherdaemon.fetcher.engines;

import cl.srex.srexfetcherdaemon.fetcher.engines.implementations.BingSearchAPI;
import cl.srex.srexfetcherdaemon.fetcher.engines.implementations.GoogleSearchAPI;
import cl.srex.srexfetcherdaemon.fetcher.config.PropReader;

import java.io.IOException;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 * Created by rccursach on 1/29/16.
 */
public class EngineFactory {
    private static EngineFactory _instance;
    private static final Logger logger = Logger.getLogger(EngineFactory.class);
    
    public enum engines { BING, GOOGLE };

    private EngineFactory() {
    }

    public static EngineFactory getInstance() {
        if( _instance == null ) {
            _instance = new EngineFactory();
        }
        return _instance;
    }

    public SearchEngine createEngine (engines en, String client_id) {
        SearchEngine eng = null;
        PropReader p = null;

        try {
            logger.info(String.format("Creating engine %s", en.name()));
            switch (en) {
                case BING:
                    p = new PropReader("bing.properties");
                    eng = new BingSearchAPI(p.getValueFor("API_KEY"), client_id);
                    break;
                case GOOGLE:
                    p = new PropReader("google.properties");
                    eng = new GoogleSearchAPI(p.getValueFor("API_KEY"), p.getValueFor("API_CX"), client_id);
                    break;
            }
            logger.info("engine created");
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
        return eng;
    }
}
