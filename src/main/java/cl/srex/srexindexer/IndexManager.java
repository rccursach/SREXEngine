/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexindexer;

import cl.srex.srexindexer.index.Index;
import java.io.IOException;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author rccursach
 */
public class IndexManager {
    private final String id;
    private Map[] results;
    private static final Logger logger = Logger.getLogger(IndexManager.class);
    private final Index idx;
    private final String[] fields;
    
    public IndexManager(String id) throws IOException {
        this.id = id;
        this.idx = new Index(id);
        this.fields = new String[]{"url", "ranking", "content", "snippet"};
    }
    
    public void createIndex() throws IOException {
        
        
        /* get results from db*/ logger.info("Retrieveing content");
        
        ContentTable content = new ContentTable(this.id);
        this.results = content.getSet();
        
        
        /* add each map as a document */ logger.info("Indexing content");
        
        for (Map<String, String> result : this.results) {
            this.idx.addDocument(result, this.fields);
        }
        
        logger.info("Done indexing content");
    }
    
    public String getTerms(){
        try {
            return this.idx.getVector();
        } catch (IOException ex) {
            logger.error("error while asking for terms vector");
            logger.error(ex.getMessage());
        }
        return "{ 'error': 'Ocurrio un error extrayendo el vector de terminos de los documentos'}";
    }
}
