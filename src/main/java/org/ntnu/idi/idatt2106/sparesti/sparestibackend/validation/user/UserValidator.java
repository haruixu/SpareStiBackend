package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.user;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.UserRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.RegexValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("userValidator")
@RequiredArgsConstructor
public class UserValidator<T> extends ObjectValidator<T> {

    private final UserRepository userRepository;

    @Override
    public void validate(T object) {
        checkConstraints(object);
        if (object instanceof RegisterRequest) {
            validateRegisterRequestDTO((RegisterRequest) object);
        } else if (object instanceof UserUpdateDTO) {
            validateUserUpdateDTO((UserUpdateDTO) object);
        } else
            throw new ClassCastException("Object must be of type RegisterRequest or UserUpdateDTO");
    }

    public void validateRegisterRequestDTO(RegisterRequest request) {

        if (!(RegexValidator.isUsernameValid(request.username()))) {
            throw new BadInputException(
                    "The username can only contain letters, numbers and underscore, "
                            + "with the first character being a letter. "
                            + "The length must be between 3 and 30 characters");
        }
        if (!RegexValidator.isEmailValid(request.email())) {
            throw new BadInputException("The email address is invalid.");
        }
        if (!RegexValidator.isNameValid(request.firstName())) {
            throw new BadInputException(
                    "The first name: '" + request.firstName() + "' is invalid.");
        }
        if (!RegexValidator.isNameValid(request.lastName())) {
            throw new BadInputException("The last name: '" + request.lastName() + "' is invalid.");
        }
        if (userExistsByUsername(request.username())) {
            throw new UserAlreadyExistsException(
                    "User with username: " + request.username() + " already exists");
        }
        if (userExistByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                    "User with email: " + request.email() + " already exists");
        }
        if (!RegexValidator.isPasswordStrong(request.password())) {
            throw new BadInputException(
                    "Password must be at least 8 characters long and at max 30 characters, include"
                            + " numbers, upper and lower case letters, and at least one special"
                            + " character");
        }
    }

    public void validateUserUpdateDTO(UserUpdateDTO dto) {
        if (dto.email() != null) {
            if (!RegexValidator.isEmailValid(dto.email())) {
                throw new BadInputException("The email address is invalid.");
            }
        }

        if (dto.firstName() != null) {
            if (!RegexValidator.isNameValid(dto.firstName())) {
                throw new BadInputException(
                        "The first name: '" + dto.firstName() + "' is invalid.");
            }
        }

        if (dto.lastName() != null) {
            if (!RegexValidator.isNameValid(dto.lastName())) {
                throw new BadInputException("The last name: '" + dto.lastName() + "' is invalid.");
            }
        }

        if (dto.email() != null) {
            if (userExistByEmail(dto.email())) {
                throw new UserAlreadyExistsException(
                        "User with email: " + dto.email() + " already exists");
            }
        }

        if (dto.password() != null) {
            if (!RegexValidator.isPasswordStrong(dto.password())) {
                throw new BadInputException(
                        "Password must be at least 8 characters long and at max 30 characters,"
                                + " include numbers, upper and lower case letters, and at least one"
                                + " special character");
            }
        }
    }

    /**
     * Determines whether a user with the given username exists
     * @param username The username
     * @return True, if the user exists.
     */
    private boolean userExistsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Determines whether a user with the given email exists
     * @param email Email
     * @return True, if a user with the given email exists
     */
    private boolean userExistByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
