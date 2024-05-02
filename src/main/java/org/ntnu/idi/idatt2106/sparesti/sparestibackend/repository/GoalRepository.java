package org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository;

import java.util.List;
import java.util.Optional;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository interface responsible for communicating with the data layer in relation to the Goal entity.
 *
 * @author Harry X.
 * @version 1.0
 * @since 22.4.24
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {

    /**
     * Finds a page of all saving goal entities of a user
     * @param user User who owns the goals
     * @param pageable Configures the page parameters
     * @return Page of all saving goals of a user
     */
    Page<Goal> findAllByUser(User user, Pageable pageable);

    /**
     * Finds the goal of a user specified by its id, wrapped by an Optional object.
     * If the goal does not exist or does not belong to the user, the Optional wraps nothing
     * @param id Identifies a goal
     * @param user User who owns the goal
     * @return Optional of the specified goal, else returns Optional wrapped around nothing
     */
    Optional<Goal> findByIdAndUser(Long id, User user);

    /**
     * Finds all saving goals of a user that are active (completedOn is null)
     * @param user The user, owning the goals
     * @return List of active goals
     */
    List<Goal> findAllByCompletedOnIsNullAndUser(User user);

    /**
     * Finds a page of saving goals of a user that are complete/inactive (completedOn is not null)
     * @param user The user, owning the goals
     * @param pageable Pageable object to paginate the return value
     * @return A page of completed goals
     */
    Page<Goal> findAllByCompletedOnIsNotNullAndUser(User user, Pageable pageable);

    /**
     * Finds all saving goals of a user that are active (completedOn is null),
     * sorted by priority, from lowest value (high priority) to highest (low priority)
     * @param user The user, owning the goals
     * @return List of active goals
     */
    List<Goal> findAllByCompletedOnIsNullAndUserOrderByPriorityAsc(User user);

    /**
     * Transactional method used for deleting a goal of a user
     * @param id Identifies the goal
     * @param user User that owns the Goal
     */
    @Transactional
    void deleteByIdAndUser(Long id, User user);
}
