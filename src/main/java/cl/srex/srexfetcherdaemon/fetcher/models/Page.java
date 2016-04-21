/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexfetcherdaemon.fetcher.models;

import java.io.Serializable;

/**
 *
 * Clase que maneja la estructura de las p&aacute;ginas almacenadas en el cache.
 *
 * @author javier
 * @version 1.0
 * @since 1.0
 */
public class Page implements Serializable {

    /**
     *
     * Clase que maneja el modelo de datos para una p&aacute;gina,
     *
     * @author Javier Fuentes (j.fuentes06@ufromail.cl)
     * @version 1.0
     *
     */
    private static final long serialVersionUID = 1L;

    //private int rankingPosition;
    private String url;
    private String title;
    private String snippet;
    private String content = null;

    public Page(String url, String title, String snippet) {
        this.url = url;
        this.title = title;
        this.snippet = snippet;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public int getContentLength() {
        return this.content == null ? 0 : this.content.length();
    }
    
    @Override
    public String toString() {
        return "Page [url=" + url + ", title=" + title + ", snippet=" + snippet + "]";
    }

}
