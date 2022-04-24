package com.launchersoft.cnabparser.exception;

public class CNABStorageException extends RuntimeException {
    public CNABStorageException(String message) {
        super(message);
    }

    public CNABStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
