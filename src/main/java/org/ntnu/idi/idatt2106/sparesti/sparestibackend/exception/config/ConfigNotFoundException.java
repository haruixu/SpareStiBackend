package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config;

/**
 * Exception thrown for config not found
 *
 * @author Yasin M.
 * @version 1.0
 * @since 19.4.24
 */
public class ConfigNotFoundException extends RuntimeException {

    /**
     * Constructor for exception
     * @param message Custom message
     */
    public ConfigNotFoundException(String message) {
        super(message);
    }
}
