package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Class to represent the different regexes that are used to validate input fields.
 *
 * @author Harry L.X
 * @version 1.0
 * @since 25.4.24
 */
@Getter
@RequiredArgsConstructor
public enum RegexPattern {

    /**
     * Valid username starts with a letter.
     * Valid characters are Norwegian letters, numbers and underscore.
     * Length must be between 3 and 30 characters.
     */
    USERNAME("^[ÆØÅæøåA-Za-z][æÆøØåÅA-Za-z0-9_]{2,29}$"),

    /**
     * Valid password contains at least 8 characters and at max 30 characters, including numbers,
     * Norwegian letters and at least 1 special character
     */
    PASSWORD("^(?=.*[0-9])(?=.*[a-zæøå])(?=.*[ÆØÅA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,30}$"),

    /**
     * Valid email must start with Norwegian letters, numbers, underscore, '+', '&', '*', '-'
     * Valid email can include must include '@'
     * Must include a period after '@'
     * Must have letters after period of length 2-7 characters
     */
    EMAIL(
            "^[æÆøØåÅa-zA-Z0-9_+&*-]+(?:\\.[æÆøØåÅa-zA-Z0-9_+&*-]+)*@(?:[æÆøØåÅa-zA-Z0-9-]+\\.)+[æÆøØåÅa-zA-Z]{2,7}$"),

    /**
     *  A valid contains can only contain Norwegian letters, white space, comma, period, singe quotes and hyphens
     *  The name must have at least 1 character and at max 30 characters
     */
    NAME("^[æÆøØåÅa-zA-Z,.'-][æÆøØåÅa-zA-Z ,.'-]{1,29}$");

    /**
     * The regex pattern
     */
    private final String pattern;
}
