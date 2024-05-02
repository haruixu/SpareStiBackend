package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception thrown in relation to uploading and getting files
 *
 * @author Lars N.
 * @version 1.0
 * @since 30.4.24
 */
public class StorageException extends RuntimeException {

    /**
     * Constructor for exception
     * @param message Custom message
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Constructor for exception with throwable cause
     * @param message custom message
     * @param cause Throwable cause object
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
