package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.storage;

/**
 * Exception thrown in relation to files uploads when they are not found
 *
 * @author Lars N.
 * @version 1.0
 * @since 29.4.24
 */
public class StorageFileNotFoundException extends StorageException {

    /**
     * Constructor for exception
     * @param message custom message
     */
    public StorageFileNotFoundException(String message) {
        super(message);
    }
}
