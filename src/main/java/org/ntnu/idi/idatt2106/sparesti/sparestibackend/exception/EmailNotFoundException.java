package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

public class EmailNotFoundException extends RuntimeException {
  public EmailNotFoundException(String message) {
    super(message);
  }
}
