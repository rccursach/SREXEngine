/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexindexer.index;

import java.io.IOException;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.apache.commons.lang.StringEscapeUtils;


/**
 *
 * @author rccursach
 */
public class Index {

    private String id;
    private IndexWriter indexWriter;
    private IndexReader indexReader;
    private Directory directory;
    
    private static final Logger logger = Logger.getLogger(Index.class);

    public Index(String id) throws IOException {
        this.id = id;

        //StandardAnalyzer analizer = new StandardAnalyzer();
        SpanishAnalyzer analizer = new SpanishAnalyzer(Version.LATEST);
        directory = new RAMDirectory();
        //directory = FSDirectory.open(new File("/Users/rccursach/testidx_2"));
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, analizer);

        this.indexWriter = new IndexWriter(directory, conf);
        //this.indexReader =  DirectoryReader.open(directory);
    }

    public void addDocument(Map<String, String> document, String[] fields) throws IOException {

        Document doc = new Document();

        /* field for data to be indexed */
        FieldType contentTextFieldType = new FieldType();
        contentTextFieldType.setIndexed(true);
        contentTextFieldType.setTokenized(true);
        contentTextFieldType.setStored(true);
        contentTextFieldType.setStoreTermVectors(true);
        contentTextFieldType.setStoreTermVectorPositions(true);
        contentTextFieldType.setStoreTermVectorOffsets(true);

        /* field not indexed but stored */
        FieldType nonIndexedType = new FieldType();
        nonIndexedType.setIndexed(false);
        nonIndexedType.setStored(true);

        for (String field_name : fields) {

            Field f;
            if (field_name.equals("content")) {
                f = new Field("content", "", contentTextFieldType);
            } else {
                f = new Field(field_name, "", nonIndexedType);
            }
            //try { if field is not null
            f.setStringValue((String) document.get(field_name));
            doc.add(f);
        }
        

        indexWriter.addDocument(doc);
        indexWriter.commit();

        logger.info("Document added successfully");
        // indexWriter.close(); // #commit is preferred over #close as we write
                                // many times into the index
    }

    public int getTermVectors() {

        int docs = indexReader.numDocs();
        Fields[] f = new Fields[docs];

        for (int i = 0; i < docs; i++) {
            try {
                f[i] = indexReader.getTermVectors(i);
            } catch (IOException ex) {
                logger.error(ex);
            }
        }

        return docs;
    }

    public String getVector() throws IOException {
        
        // We can now close the indexWriter
        this.indexWriter.close();
        
        this.indexReader =  DirectoryReader.open(directory);
        
        DocsAndPositionsEnum docsAndPositionsEnum = null;
        Terms termsVector = null;
        TermsEnum termsEnum = null;
        BytesRef term = null;
        String val = null;
        
        String out_json = "[";
        
        for (int i = 0; i < indexReader.maxDoc(); i++) {
            logger.info( String.format("Processing term vector for doc %d of %d.", i, indexReader.maxDoc()) );
            
            String result = "";
            try {

                String rank = indexReader.document(i).get("ranking");
                String url = indexReader.document(i).get("url");
                String snippet = StringEscapeUtils.escapeJava( indexReader.document(i).get("snippet") );
                
                // "rank" : { "url" : "http://www.example.org", "snippet": "", "terms" : [ 
                result += String.format("{ \"ranking\" : %s, \"url\" : \"%s\", \"snippet\" : \"%s\", \"terms\" : [ ", rank, url, snippet);
                
                termsVector = indexReader.getTermVector(i, "content");
                termsEnum = termsVector.iterator(termsEnum);

                while ((term = termsEnum.next()) != null) {
                    // "the_term" : {
                    result += String.format("{ \"term\" : \"%s\",", term.utf8ToString()); // The term

                    docsAndPositionsEnum = termsEnum.docsAndPositions(null, docsAndPositionsEnum);
                    if (docsAndPositionsEnum.nextDoc() >= 0) {
                        
                        int freq = docsAndPositionsEnum.freq();
                        result += String.format("\"freq\" : %d,", freq);
                        
                        result += "\"positions\" : [ ";
                        for (int j = 0; j < freq; j++) {
                            result += "{";
                            result += String.format("\"position\" : %d,", docsAndPositionsEnum.nextPosition());
                            result += String.format("\"offset_start\" : %d,", docsAndPositionsEnum.startOffset());
                            result += String.format("\"offset_end\" : %d", docsAndPositionsEnum.endOffset());
                            result += "}";
                            if(j+1 < freq){ result += ",";}
                        }
                        result += "]";
                    }
                    else {
                        // for freq 1
                        result += String.format("\"freq\" : %d,", 0);
                        result += "\"positions\" : [ ]";
                    }
                    result += "},";
                }
                // Remove last unnecesary semicolon (by removing last character)
                result = result.replaceAll(".$", "");
                
                //end of terms array means also, go to next doc
                result += "]}";
                
                // check if i have more docs after putting another semicolon
                if(i+1 < indexReader.maxDoc() ){ result += ","; }
            }
            catch(Exception e){
                logger.error( String.format("Error tratando de crear vector de terminos en doc %d.", i) );
                // logger.error(e.getMessage()); // We never get an actuall exception here
                result = "";
            }
            
            // Add this doc json to the resulting string
            out_json += result;
        }
        // If last doc failed, another stray semicolon needs to be removed
        if(out_json.charAt(out_json.length()-1) == ','){ out_json = out_json.replaceAll(".$", ""); }
        // and close the array symbol
        out_json += "]";
        
        this.indexReader.close();
        //System.out.println("result: \n" + out_json);
        return out_json;
    }
}
