package org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository;

import java.util.List;
import java.util.Optional;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface that communicates with the data layer relating to the Challenge entity.
 *
 * @author Yasin M.
 * @version 1.0
 * @since 22.4.2024
 */
@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    /**
     * Gets a page of saving challenges of a user.
     * @param user User object that owns the challenges
     * @param pageable Configures the page parameters
     * @return Page of challenges
     */
    Page<Challenge> findByUser(User user, Pageable pageable);

    /**
     * Gets an Optional object, wrapped around either a saving challenge of a user, if it exists, or nothing.
     * @param id Identifies the challenge
     * @param user The user who owns the challenge
     * @return Optional wrapper around the saving challenge of the user
     */
    Optional<Challenge> findByIdAndUser(Long id, User user);

    /**
     * Finds a page of active saving challenges of a user (completion date is not set, therefore "null")
     * @param user User who owns the challenges
     * @param pageable Configures the page parameters
     * @return Page of active challenges
     */
    Page<Challenge> findAllByCompletedOnIsNullAndUser(User user, Pageable pageable);

    /**
     * Finds a list of active saving challenges of a user (completion date is not set, therefore "null")
     * @param user User who owns the challenges
     * @return List of active challenges
     */
    List<Challenge> findAllByCompletedOnIsNullAndUser(User user);

    /**
     * Finds a page of completed saving challenges of a user (completion date is set, therefore not "null")
     * @param user User who owns the challenges
     * @param pageable Configures the page parameters
     * @return Page of completed challenges
     */
    Page<Challenge> findAllByCompletedOnIsNotNullAndUser(User user, Pageable pageable);
}
