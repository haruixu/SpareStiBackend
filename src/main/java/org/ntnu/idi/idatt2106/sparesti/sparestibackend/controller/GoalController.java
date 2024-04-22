package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.GoalDTO;
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

    // TODO: Finne aktive og inaktive mål i request param
    @GetMapping
    public ResponseEntity<Page<GoalDTO>> getUserGoals(
            Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received GET request for goals of user: " + userDetails.getUsername());
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to get all user goals");
        return ResponseEntity.ok(goalService.getUserGoals(user, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalDTO> getUserGoal(
            @Parameter(description = "The ID-number of a goal") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received GET request for goal with id {}", id);
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to find goal");
        return ResponseEntity.ok(goalService.findUserGoal(id, user));
    }

    @PostMapping
    public ResponseEntity<GoalDTO> createUserGoal(
            @Valid @RequestBody GoalDTO goalDTO,
            @AuthenticationPrincipal UserDetails userDetails,
            BindingResult bindingResult) {
        logger.info("Received POST request for goal with id {}", goalDTO.id());
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to save goal");
        return ResponseEntity.ok(goalService.save(goalDTO, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalDTO> updateUserGoal(
            @Valid @RequestBody GoalDTO goalDTO,
            @AuthenticationPrincipal UserDetails userDetails,
            BindingResult bindingResult) {
        logger.info("Received PUT request for goal {}", goalDTO);
        if (bindingResult.hasErrors()) {
            throw new BadInputException("Fields in the body cannot be null, blank or empty");
        }
        User user = userService.findUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(goalService.update(goalDTO));
    }

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
}
