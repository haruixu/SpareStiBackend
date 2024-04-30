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

@Getter
@Repository
@RequiredArgsConstructor
public class RegistrationService implements CredentialRepository {

    private final UserRepository userRepo;

    private final AuthenticatorRepository authRepository;

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

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        User user =
                userRepo.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));
        return Optional.of(user.getHandle());
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        User user = userRepo.findByHandle(userHandle);
        return Optional.of(user.getUsername());
    }

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
