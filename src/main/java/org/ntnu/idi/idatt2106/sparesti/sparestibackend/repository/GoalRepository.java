package org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository;

import java.util.List;
import java.util.Optional;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    Page<Goal> findAllByUser(User user, Pageable pageable);

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

    @Transactional
    void deleteByIdAndUser(Long id, User user);
}
