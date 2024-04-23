package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalResponseDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.GoalService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ApplicationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/users/me/goals")
public class GoalController {

    private final GoalService goalService;

    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public ResponseEntity<Page<GoalResponseDTO>> getUserGoals(
            Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received GET request for goals of user: {}", userDetails.getUsername());
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to get all user goals");
        return ResponseEntity.ok(goalService.getUserGoals(user, pageable));
    }

    @GetMapping("/active")
    public ResponseEntity<List<GoalResponseDTO>> getActiveGoals(
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received GET request for active goals of user: {}", userDetails.getUsername());
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to get all active user goals");
        return ResponseEntity.ok(goalService.getActiveUserGoals(user));
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<GoalResponseDTO>> getCompleteGoals(
            Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        logger.info(
                "Received GET request for complete goals of user: {}", userDetails.getUsername());
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to get all complete user goals");
        return ResponseEntity.ok(goalService.getCompletedUserGoals(user, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalResponseDTO> getUserGoal(
            @Parameter(description = "The ID-number of a goal") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received GET request for goal with id {}", id);
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to find goal");
        return ResponseEntity.ok(goalService.findUserGoal(id, user));
    }

    @PostMapping
    public ResponseEntity<GoalResponseDTO> createUserGoal(
            @Valid @NotNull @RequestBody GoalCreateDTO goalDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info(
                "Received POST request for goal {} under user {}",
                goalDTO,
                userDetails.getUsername());
        if (bindingResult.hasErrors()) {
            logger.error(bindingResult.getAllErrors().toString());
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to save goal");
        return ResponseEntity.ok(goalService.save(goalDTO, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalResponseDTO> updateUserGoal(
            @Parameter(description = "The ID-number of a goal") @PathVariable Long id,
            @Valid @NotNull @RequestBody GoalUpdateDTO goalDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received PUT request for goal with id {} with request body {}", id, goalDTO);
        if (bindingResult.hasErrors()) {
            throw new BadInputException("Fields in the body cannot be null, blank or empty");
        }
        User user = userService.findUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(goalService.update(id, goalDTO, user));
    }

    // TODO: PUT metode raw på /goals hvor en liste av goals sendes (for prioritet)

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserGoal(
            @Parameter(description = "The ID-number of a goal") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received DELETE request for goal with id {}", id);
        User user = userService.findUserByUsername(userDetails.getUsername());
        goalService.deleteUserGoal(id, user);
        logger.info("Successfully deleted goal");
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/completed")
    public ResponseEntity<GoalResponseDTO> completeUserGoal(
            @Parameter(description = "The ID-number of a goal") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received PUT request for completing a goal with id {}", id);
        User user = userService.findUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(goalService.completeGoal(id, user));
    }
}
