/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexfetcherdaemon.server;

import cl.srex.srexfetcherdaemon.fetcher.engines.EngineFactory;
import cl.srex.srexfetcherdaemon.fetcher.engines.SearchEngine;
import cl.srex.srexfetcherdaemon.fetcher.models.Query;

/**
 *
 * @author rccursach
 */
public class SearchManager {
    
    private SearchEngine engine;
    
    public SearchManager(EngineFactory.engines e, String client_id) {
        EngineFactory ef = EngineFactory.getInstance();
        engine = ef.createEngine(e, client_id);
    }

    public void search(String query) {
        Query q = new Query(query);
        engine.initSearch(q);
    }
    
}
