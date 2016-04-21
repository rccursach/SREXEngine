/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexindexer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 *
 * @author rccursach
 */
public class ContentTable {
    private Jedis db;
    private String id;
    //private static final Logger logger = Logger.getLogger(ContentTable.class);
    
    public ContentTable(String id){
        //@Refactor use config file for URL
        String db_url = "localhost";
        this.db = new Jedis(db_url);
        this.id = id;
    }
    
    public Map<String, String>[] getSet() {
        
        Map res = db.hgetAll(this.id);
        Map<String, String>[] out = new Map[res.size()];
        
        Gson gson = new Gson();
        Type strHashmapType = new TypeToken<Map<String, String>>(){}.getType();
        
        //logger.info(String.format("Reading %d records", res.size()));
        
        for(Object key : res.keySet()) {
            String rank = (String) key;
            String json = (String)res.get(rank);
            
            Map result = gson.fromJson(json, strHashmapType);
            result.put("ranking", key);
            
            out[Integer.parseInt(rank)] = result;
        }
        //logger.info("Done reading");
        
        return out;
    }
    
}
