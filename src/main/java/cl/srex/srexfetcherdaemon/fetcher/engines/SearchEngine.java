/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexfetcherdaemon.fetcher.engines;

/**
 *
 * @author rccursach
 */

/**
 * Created by rccursach on 1/29/16.
 */

import org.apache.log4j.Logger;
import cl.srex.srexfetcherdaemon.fetcher.config.Constants;
import cl.srex.srexfetcherdaemon.fetcher.data.DBManager;
import cl.srex.srexfetcherdaemon.fetcher.data.PageRetriever;
import cl.srex.srexfetcherdaemon.fetcher.models.Page;
import cl.srex.srexfetcherdaemon.fetcher.models.Query;

import java.io.IOException;

public abstract class SearchEngine {

    private static final Logger logger = Logger.getLogger(SearchEngine.class);
    private String client_id;
    
    public SearchEngine(String client_id){
        this.client_id = client_id;
    }

    /**
     *
     * Permite obtener las paginas, sin contenido, desde el buscador.
     * @param query, Consulta a ser buscada
     * @param size, Cantidad de resultados
     * @throws IOException
     * @return lista con las paginas obtenidas.
     */
    public abstract Page[] getPages(Query query, int size) throws IOException;


    /**
     * Metodo que realiza la busqueda, indexa y retorna el resultado de la busqueda
     */
    public void initSearch(final Query query)  {

        logger.debug("Buscando por: " + query.getQuery());


        /**
         * Download and index the pages retrieved from the search engine
         * in different threads
         */
        try {

            logger.info("Buscando páginas");

            /**
             * Execute the query in the search engine.
             */
            //Page[] pages = this.getPages(query, Constants.PAGES / 10);
            Page[] pages = this.getPages(query, 10);


            logger.info("Se descargarán los contenidos de "+pages.length+" páginas");


            /** Define one thread for each page download  */
            Thread[] threads = new Thread[pages.length];

            /**  Starts the Indexing Manager (Lucene)*/
            //final IndexManager idxManager = new IndexManager();
            //idxManager.openIndex();

            // Mal!! debe ser una conexion por hilo!! movido al ciclo for!
            //final DBManager db = new DBManager ();



            /**
             * Start the retrieve/indexing proccesses in different threads
             * */
            for (int i = 0; i < pages.length; i++) {
                DBManager db = new DBManager(this.client_id);
                final int ranking = i;
                threads[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PageRetriever pr = new PageRetriever();
                        //logger.info("Recuperando página  "+pages[ranking]);

                        /**
                         * retrieve the page content
                         * */
                        //PageRetrieval.retrieveContentPage(pages[ranking], true);
                        pr.retrievePageContent(pages[ranking]);

                        /**
                         * Index the retrieved page
                         * */
                        //idxManager.indexPage(pages[ranking], ranking);

                        db.addPage (pages[ranking],ranking);


                    }
                });
                threads[i].start();
            }


            /**
             * Wait until all indexing threads are completed
             * and then close the indexing process  */
            for(int j = 0; j < threads.length; j++)
                threads[j].join();

            //idxManager.closeIndex();

            logger.info("Todos los download threads fueron cerrados exitosamente");

        } catch (IOException e) {
            logger.error("Error I/O en initSearch");
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.error("Error Threads en initSearch");
            e.printStackTrace();
        }


    }
}

