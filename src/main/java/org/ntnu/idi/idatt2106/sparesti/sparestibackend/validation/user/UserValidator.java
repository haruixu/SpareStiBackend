package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.user;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
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
                    "Brukernavnet kan kun inneholde bokstaver, nummer og understrek. Første"
                            + " karakter må være en bokstav og lengden må være mellom 3 og 30"
                            + " karakterer");
        }
        if (!RegexValidator.isEmailValid(request.email())) {
            throw new BadInputException("Ugylid mailadresse");
        }
        if (!RegexValidator.isNameValid(request.firstName())) {
            throw new BadInputException("Fornavnet '" + request.firstName() + "' er ugylig");
        }
        if (!RegexValidator.isNameValid(request.lastName())) {
            throw new BadInputException("Etternavnet '" + request.lastName() + "' er ugyldig");
        }
        if (userExistsByUsername(request.username())) {
            throw new UserAlreadyExistsException(
                    "Brukernavnet '" + request.username() + "' er allerede tatt");
        }
        if (userExistByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                    "Mailen '" + request.email() + "' er allerede tatt");
        }
        if (!RegexValidator.isPasswordStrong(request.password())) {
            throw new BadInputException(
                    "Passordet må være mellom 8 og 30 bokstaver, inkludere numre, store og små"
                            + " bokstaver og minst et spesielt tegn");
        }
    }

    public void validateUserUpdateDTO(UserUpdateDTO dto) {
        if (dto.email() != null) {
            if (!RegexValidator.isEmailValid(dto.email())) {
                throw new BadInputException("Ugylid mailadresse");
            }
            if (userExistByEmail(dto.email()) && !isEmailOwn(dto.email(), dto.username())) {
                throw new UserAlreadyExistsException(
                        "Mailen '" + dto.email() + "' er allerede tatt");
            }
        }

        if (dto.firstName() != null) {
            if (!RegexValidator.isNameValid(dto.firstName())) {
                throw new BadInputException("Fornavnet '" + dto.firstName() + "' er ugyldig");
            }
        }

        if (dto.lastName() != null) {
            if (!RegexValidator.isNameValid(dto.lastName())) {
                throw new BadInputException("Etternavnet '" + dto.lastName() + "' er ugyldig");
            }
        }

        if (dto.password() != null) {
            if (!RegexValidator.isPasswordStrong(dto.password())) {
                throw new BadInputException(
                        "Passordet må være mellom 8 og 30 bokstaver, inkludere numre, store og små"
                                + " bokstaver og minst et spesielt tegn");
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

    private boolean isEmailOwn(String email, String username) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return false;

        return user.getUsername().equals(username);
    }
}
