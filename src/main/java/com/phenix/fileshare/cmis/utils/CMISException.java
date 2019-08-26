package com.phenix.fileshare.cmis.utils;

public class CMISException extends Exception {
    private static final long serialVersionUID = 1L;

    public CMISException(String message) {
        super(message);
    }

    public CMISException(String message, Throwable cause) {
        super(message, cause);
    }
}
