package org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository;

import java.util.Optional;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChangePasswordRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChangePasswordRequestRepository
        extends JpaRepository<ChangePasswordRequest, Long> {

    Optional<ChangePasswordRequest> findChangePasswordRequestByUserID(Long userID);

    @Query("SELECT cpr.id FROM ChangePasswordRequest cpr WHERE cpr.userID = :userId")
    Optional<String> findIdByUserId(Long userId);
}
