package org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository;

import java.util.Optional;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChangePasswordRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for communicating with the data layer in relation to the ChangePasswordRequest entity.
 *
 * @author Lars N.
 * @version 1.0
 * @since 22.4.2024
 */
public interface ChangePasswordRequestRepository
        extends JpaRepository<ChangePasswordRequest, Long> {

    /**
     * Finds a change password request based on its user ID
     * @param userID Id of user that initiated the change password request
     * @return Optional wrapped around the object if it exists, else returns an empty Optional
     */
    Optional<ChangePasswordRequest> findChangePasswordRequestByUserID(Long userID);

    /**
     * Finds the ID of a change password request using the object's user IDc:w
     * @param userId Id of user that initiated the change password request
     * @return Optional wrapped around the ID if it exists, else returns an empty Optional
     */
    @Query("SELECT cpr.id FROM ChangePasswordRequest cpr WHERE cpr.userID = :userId")
    Optional<String> findIdByUserId(Long userId);
}
