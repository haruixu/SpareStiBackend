package org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository;

import com.yubico.webauthn.data.ByteArray;
import java.util.List;
import java.util.Optional;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Authenticator;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository interface for abstracting communication with the data layer related to
 * the Authenticator entity used for storing biometric login credentials
 *
 * @author Yasin M.
 * @version 1.0
 * @since 28.4.24
 */
@Repository
public interface AuthenticatorRepository extends JpaRepository<Authenticator, Long> {

    /**
     * Finds an Authenticator object using its credential id, if it exists
     * @param credentialId Byte array used to identify an Authenticator object
     * @return An Optional object, containing the Authenticator if it exists. Else, the optional contains nothing
     */
    Optional<Authenticator> findByCredentialId(ByteArray credentialId);

    /**
     * Finds all biometric entries for a user
     * @param user The user who's biometric registrations are being found
     * @return List of Authenticator objects
     */
    List<Authenticator> findAllByUser(User user);

    /**
     * Finds a list of biometric entries using their credential id's
     * @param credentialId Byte array used to identify Authenticator objects
     * @return List of authenticator objects with matching credential id's
     */
    List<Authenticator> findAllByCredentialId(ByteArray credentialId);

    /**
     * Transactional statement used when removing all biometric entries of a user.
     * Rollbacks if this fails.
     * @param user User who's biometric entries are removed
     */
    @Transactional
    void removeAllByUser(User user);
}
