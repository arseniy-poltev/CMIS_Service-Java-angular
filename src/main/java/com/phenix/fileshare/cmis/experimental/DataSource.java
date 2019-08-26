package com.phenix.fileshare.cmis.experimental;

/**
 * Class DataSource
 */
public class DataSource {

    private String fileName;

    // Constructor
    public DataSource(String fileName) {
        this.fileName = fileName;
    }

    // Getters and setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
