package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.mail.MessagingException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.account.AccountAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.account.AccountNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.challenge.ChallengeAlreadyCompletedException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.challenge.ChallengeNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config.ChallengeConfigAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config.ChallengeConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config.ConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal.ActiveGoalLimitExceededException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal.GoalNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal.NotActiveGoalException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.storage.StorageException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.storage.StorageFileNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.user.UserAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.user.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Global exception handler to handle different types of exceptions across the application. It provides centralized
 * exception handling for various types of exceptions that may occur during the execution of the application.
 *
 * @author Harry L.X and Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Default constructor
     */
    public GlobalExceptionHandler() {}

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Log the error to the logger.
     *
     * @param ex
     *            The exception to be logged.
     */
    private void logError(Exception ex) {
        logger.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
    }

    private String createErrorResponseMsg(Exception ex) {
        return ex.getMessage();
    }

    /**
     * Handle exceptions related to existing objects.
     *
     * @param ex
     *            The exception indicating that an object already exists.
     * @return ResponseEntity with an appropriate HTTP status code and error message.
     */
    @ExceptionHandler(
            value = {
                UserAlreadyExistsException.class,
                ChallengeConfigAlreadyExistsException.class,
                AccountAlreadyExistsException.class
            })
    public ResponseEntity<String> handleObjectAlreadyExistException(Exception ex) {
        logError(ex);
        String msg = createErrorResponseMsg(ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
    }

    /**
     * Handle exceptions related to non-existing objects.
     *
     * @param ex
     *            The exception indicating that an object does not exist.
     * @return ResponseEntity with an appropriate HTTP status code and error message.
     */
    @ExceptionHandler(
            value = {
                UserNotFoundException.class,
                UsernameNotFoundException.class,
                GoalNotFoundException.class,
                ChallengeConfigNotFoundException.class,
                ChallengeNotFoundException.class,
                ConfigNotFoundException.class,
                AccountNotFoundException.class,
                AssertionRequestNotFoundException.class,
                EmailNotFoundException.class
            })
    public ResponseEntity<String> handleObjectDoesNotExistException(Exception ex) {
        logError(ex);
        String msg = createErrorResponseMsg(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
    }

    /**
     * Handle exceptions related to bad input or invalid requests.
     *
     * @param ex
     *            The exception indicating bad input or invalid request.
     * @return ResponseEntity with an appropriate HTTP status code and error message.
     */
    @ExceptionHandler(
            value = {
                IllegalArgumentException.class,
                HttpMessageNotReadableException.class,
                BadInputException.class,
                NullPointerException.class,
                MissingServletRequestParameterException.class,
                HttpRequestMethodNotSupportedException.class,
                MessagingException.class,
                MethodArgumentNotValidException.class,
                ActiveGoalLimitExceededException.class,
                DataIntegrityViolationException.class,
                ChallengeAlreadyCompletedException.class,
                ActiveGoalLimitExceededException.class,
                NotActiveGoalException.class,
                StorageException.class,
                StorageFileNotFoundException.class
            })
    public ResponseEntity<String> handleBadInputException(Exception ex) {
        logError(ex);
        String msg = createErrorResponseMsg(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    /**
     * Exception handler method for BadCredentialsException
     * @param ex Exception
     * @return ResponseEntity with error message
     */
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(Exception ex) {
        logError(ex);
        String msg = "Brukernavn eller passord er feil";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    /**
     * Handle exception for invalid objects
     * @param ex ObjectNotValid exception
     * @return ResponseEntity with bad request response code containing all violations
     */
    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<Set<String>> handleValidationException(ObjectNotValidException ex) {
        logError(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrorMessages());
    }

    /**
     * Handle exceptions related to constraint violations in validation.
     *
     * @param ex
     *            The exception indicating constraint violations during validation.
     * @return ResponseEntity with an appropriate HTTP status code and error message.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<String> handleConstraintViolationException(
            ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        String errorMessage = String.join(", ", errors);
        return ResponseEntity.badRequest().body(errorMessage);
    }
    /*
    /**
     * Handle any remaining exceptions that are not explicitly handled.
     *
     * @param ex
     *            The exception that is not explicitly handled.
     * @return ResponseEntity with an appropriate HTTP status code and error message.

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<String> handleRemainderExceptions(Exception ex) {
      logError(ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getClass().getSimpleName());
    }
    */
}
