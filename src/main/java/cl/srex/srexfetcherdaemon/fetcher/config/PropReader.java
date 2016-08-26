/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexfetcherdaemon.fetcher.config;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by rccursach on 2/2/16.
 */

/**
 * Loads a .properties file and get its values. It will try to load a user defined version if a file preceded with "usr_file_prefix." is found first
 */
public class PropReader {
    Properties prop;
    InputStream input;

    private static final Logger logger = Logger.getLogger(PropReader.class);

    /**
     * Creates an instance of PropReader
     * @param prop_filename .properties file name
     * @param prop_dirname Path for the file without a trailing slash (/) at the end
     * @param usr_file_prefix A prefix for a user defined version of the prop file, a dot will be added after the prefix, IE: user.default.properties if "user". If no specified, the original prop_file will be loaded.
     * @throws IOException
     */
    public PropReader(String prop_filename, String prop_dirname, String usr_file_prefix) throws IOException {

        // Default if user defined variant of the .properties file is not present
        String prop_custom = String.format("%s%s.%s",prop_dirname, usr_file_prefix, prop_filename);
        
        if(!prop_dirname.equals("")){ if(!prop_dirname.endsWith("/")){ prop_dirname += "/"; }}

        String p = (new File(prop_custom).exists()) ? prop_custom : String.format("%s%s", prop_dirname, prop_filename);

        logger.info(String.format("Trying to load properties file %s", p));

        input = new FileInputStream(p);
        //input = ClassLoader.class.getResourceAsStream(p);
        prop =  new Properties();
        prop.load(input);
    }

    /**
     * Creates an instance of PropReader with defaults path to "resources/user."+prop_filename
     * @param prop_filename .properties file name
     * @throws IOException
     */
    public PropReader(String prop_filename) throws IOException {
        this(prop_filename, "", "user");
    }

    /**
     * Get a value for a given key found on the .properties file
     * @param key
     * @return String or null
     */
    public String getValueFor(String key) {
        return prop.getProperty(key);
    }
}
