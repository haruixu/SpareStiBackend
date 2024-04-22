package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.EmailNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(
                        () ->
                                new UsernameNotFoundException(
                                        "User with username: " + username + " not found"));
    }

    public User findUserByEmail(String email) throws EmailNotFoundException {
        return userRepository
          .findByEmail(email)
          .orElseThrow(
            () ->
              new EmailNotFoundException(
                "User with email: " + email + " not found"));
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean userExistByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void updatePassword(Long userID, String newPassword) {
        User user = userRepository.findById(userID)
          .orElseThrow(() -> new BadInputException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
