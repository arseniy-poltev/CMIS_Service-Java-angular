package com.phenix.fileshare.cmis.utils;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Mapping {

    private Map<String, String> leftMap = new HashMap<String, String>();
    private Map<String, String> rightMap = new HashMap<String, String>();

    public Mapping(String filename) {
        readPropertiesFile(filename);
    }

    public String getRight(String left) {
        return leftMap.get(left);
    }

    public String getLeft(String right) {
        return rightMap.get(right);
    }

    private void readPropertiesFile(String filename) {
        InputStream stream = null;
        try {
            stream = Mapping.class.getResourceAsStream("/" + filename);

            Properties props = new Properties();
            props.load(stream);

            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);
                leftMap.put(key.trim(), value.trim());
                rightMap.put(value.trim(), key.trim());
            }
        } catch (Exception e) {
            // ignore
        } finally {
            try {
                stream.close();
            } catch (Exception ie) {
                // ignore
            }
        }
    }
}