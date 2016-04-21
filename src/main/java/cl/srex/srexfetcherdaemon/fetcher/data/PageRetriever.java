/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexfetcherdaemon.fetcher.data;

/**
 *
 * @author rccursach
 */

import cl.srex.srexfetcherdaemon.fetcher.models.Page;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

/**
 *
 * @author rccursach
 */
public class PageRetriever {

    private static final Logger logger = Logger.getLogger(PageRetriever.class);
    
    public void retrievePageContent(Page page) {
        
        String data = "";
        
        try {
            data = downloadWithTika(page.getUrl());
        } catch (IOException | TikaException ex) {
            logger.error(ex.getMessage());
        }
        
        page.setContent(data);
    }
    
    private String downloadWithTika(String str_url) throws MalformedURLException, IOException, TikaException {
        
        if (str_url.startsWith("https")){
            str_url = str_url.replaceFirst("https", "http");
        }
        
        URL url = new URL(str_url);
        String out = "";
        
        Tika tika = new Tika();
        logger.info(tika.detect(url));
        out = tika.parseToString(url);
        
        // cleaning abuse of blank spaces newlines and other characters for formatting plain text
        //return out.replaceAll("^\\s|\t|\n\\s|\\s$|\\||\\s\\s+", "").replaceAll("\n", " ");
        return out.replaceAll("\n|\t|\\|", " ").replaceAll("\\s\\s+", " ");
    }
}

