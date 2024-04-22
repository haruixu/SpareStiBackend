package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.GoalDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.GoalNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.GoalMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.GoalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;

    public GoalDTO findGoalByIdAndUser(Long id, User user) {
        return GoalMapper.INSTANCE.toDTO(
                goalRepository
                        .findByIdAndUser(id, user)
                        .orElseThrow(() -> new GoalNotFoundException(id)));
    }

    public Page<GoalDTO> getUserGoals(User user, Pageable pageable) {
        return goalRepository.findAllByUser(user, pageable).map(GoalMapper.INSTANCE::toDTO);
    }

    public GoalDTO save(GoalDTO goalDTO, User user) {
        return GoalMapper.INSTANCE.toDTO(
                goalRepository.save(GoalMapper.INSTANCE.toEntity(goalDTO, user)));
    }

    public void deleteById(Long id) {
        goalRepository.deleteById(id);
    }

    // Brukt for å testeV
    public List<Goal> getGoals() {
        return goalRepository.findAll();
    }
}
