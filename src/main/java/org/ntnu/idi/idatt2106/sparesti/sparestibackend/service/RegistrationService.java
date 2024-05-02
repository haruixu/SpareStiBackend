package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Authenticator;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.AuthenticatorRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

/**
 * Service class for handling business logic related to biometric registration and login
 *
 * @author Yasin M.
 * @version 1.0
 * @since 28.4.24
 */
@Getter
@Repository
@RequiredArgsConstructor
public class RegistrationService implements CredentialRepository {

    private final UserRepository userRepo;

    private final AuthenticatorRepository authRepository;

    /**
     * Gets all associated credential id's for a user, using their username
     * @param username Username of user
     * @return Set of Credential Id's
     */
    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        User user =
                userRepo.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));
        List<Authenticator> auth = authRepository.findAllByUser(user);
        return auth.stream()
                .map(
                        credential ->
                                PublicKeyCredentialDescriptor.builder()
                                        .id(credential.getCredentialId())
                                        .build())
                .collect(Collectors.toSet());
    }

    /**
     * Gets the handle of a user, based on the user's username
     * @param username Username of user
     * @return Handle of the User entity, if they have one
     */
    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        User user =
                userRepo.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));
        return Optional.of(user.getHandle());
    }

    /**
     * Gets the username of a User based on their handle
     * @param userHandle A users unique handle, used for biometric purposes
     * @return Username of the user, if they exist
     */
    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        User user = userRepo.findByHandle(userHandle);
        return Optional.of(user.getUsername());
    }

    /**
     * Looks up biometric registration credentials using the credential ID
     * @param credentialId Identifies biometric entry
     * @param userHandle User handle
     * @return RegisteredCredentials of a user, if it exists
     */
    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        Optional<Authenticator> auth = authRepository.findByCredentialId(credentialId);
        return auth.map(
                credential ->
                        RegisteredCredential.builder()
                                .credentialId(credential.getCredentialId())
                                .userHandle(credential.getUser().getHandle())
                                .publicKeyCose(credential.getPublicKey())
                                .signatureCount(credential.getCount())
                                .build());
    }

    /**
     * Gets a set of Registered credentials using credential Id
     * @param credentialId Identifies biometric registration entries
     * @return Set of registered credentials of a user
     */
    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        List<Authenticator> auth = authRepository.findAllByCredentialId(credentialId);
        return auth.stream()
                .map(
                        credential ->
                                RegisteredCredential.builder()
                                        .credentialId(credential.getCredentialId())
                                        .userHandle(credential.getUser().getHandle())
                                        .publicKeyCose(credential.getPublicKey())
                                        .signatureCount(credential.getCount())
                                        .build())
                .collect(Collectors.toSet());
    }
}
