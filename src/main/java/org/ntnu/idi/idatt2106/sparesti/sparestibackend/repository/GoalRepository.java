package org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository;

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

    @Transactional
    void deleteByIdAndUser(Long id, User user);
}
