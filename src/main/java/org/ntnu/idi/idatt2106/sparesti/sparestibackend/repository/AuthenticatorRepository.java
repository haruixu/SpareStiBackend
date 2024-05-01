package org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository;

import com.yubico.webauthn.data.ByteArray;
import java.util.List;
import java.util.Optional;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Authenticator;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticatorRepository extends JpaRepository<Authenticator, Long> {

    Optional<Authenticator> findByCredentialId(ByteArray credentialId);

    List<Authenticator> findAllByUser(User user);

    void deleteAllByUser(User user);

    List<Authenticator> findAllByCredentialId(ByteArray credentialId);
}
