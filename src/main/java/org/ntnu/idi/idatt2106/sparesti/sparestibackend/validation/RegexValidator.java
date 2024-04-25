package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation;

import java.util.regex.Pattern;

/**
 * Validator class for regexes
 *
 * @author Harry Linrui Xu
 * @version 1.0
 * @since 25.4.24
 */
public class RegexValidator {

    /**
     * Checks if username is valid.
     * @param username Username
     * @return True, if username is valid. Else, returns false.
     */
    public static boolean isUsernameValid(String username) {
        return Pattern.compile(RegexPattern.USERNAME.getPattern()).matcher(username).matches();
    }

    /**
     * Checks if email is invalid.
     * @param email Email
     * @return True, if email is valid.
     */
    public static boolean isEmailValid(String email) {
        return Pattern.compile(RegexPattern.EMAIL.getPattern()).matcher(email).matches();
    }

    /**
     * Checks if name is valid
     * @param name Name
     * @return If name is valid
     */
    public static boolean isNameValid(String name) {
        return Pattern.compile(RegexPattern.NAME.getPattern()).matcher(name).matches();
    }

    /**
     * Checks if a password meets the strength criteria.
     * @param password The password to check
     * @return true if the password meets the criteria, false otherwise
     */
    public static boolean isPasswordStrong(String password) {
        return Pattern.compile(RegexPattern.PASSWORD.getPattern()).matcher(password).matches();
    }
}
