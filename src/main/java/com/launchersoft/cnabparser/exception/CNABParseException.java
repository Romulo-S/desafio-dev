package com.launchersoft.cnabparser.exception;

public class CNABParseException extends RuntimeException {
    public CNABParseException(String message) {
        super(message);
    }

    public CNABParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
