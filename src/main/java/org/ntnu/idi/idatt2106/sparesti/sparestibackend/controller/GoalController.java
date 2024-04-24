package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalResponseDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal.ActiveGoalLimitExceededException;
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

/**
 * Handles all requests directed to goal endpoints. All the endpoints
 * under the currently authenticated user, meaning they are private.
 * This prevents other users from accessing/modifying data of each other.
 *
 * @author Harry L.X
 * @version 1.0
 * @since 22.4.24
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/users/me/goals")
public class GoalController {

    private final GoalService goalService;

    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Tag(name = "Saving goal", description = "CRUD methods for saving goal")
    @Operation(
            summary = "GET a page of saving goals of the currently authenticated (logger in) user",
            description = "Retrieves a page of active all saving goals of the user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful retrieval of saving goals",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoalResponseDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "401",
                        description = "The JWT token is expired or its format is invalid",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Attempt of accessing secure endpoint without token",
                        content = @Content)
            })
    @GetMapping
    public ResponseEntity<Page<GoalResponseDTO>> getUserGoals(
            Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received GET request for goals of user: {}", userDetails.getUsername());
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to get all user goals");
        return ResponseEntity.ok(goalService.getUserGoals(user, pageable));
    }

    @Tag(name = "Saving goal", description = "CRUD methods for saving goal")
    @Operation(
            summary = "GET active saving goals for the currently authenticated (logged in) user",
            description =
                    "GET a list of all active saving goals (completedOn is null). At max 10 active"
                            + " goals are allowed",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful retrieval of active saving goals",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoalResponseDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "401",
                        description = "The JWT token is expired or its format is invalid",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Attempt of accessing secure endpoint without token",
                        content = @Content)
            })
    @GetMapping("/active")
    public ResponseEntity<List<GoalResponseDTO>> getActiveGoals(
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received GET request for active goals of user: {}", userDetails.getUsername());
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to get all active user goals");
        return ResponseEntity.ok(goalService.getActiveUserGoals(user));
    }

    @Tag(name = "Saving goal", description = "CRUD methods for saving goal")
    @Operation(
            summary =
                    "GET a page of completed saving goals of the currently authenticated (logger"
                            + " in) user",
            description = "Retrieves a page of all completed saving goals of the user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful retrieval of completed saving goals",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoalResponseDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "401",
                        description = "The JWT token is expired or its format is invalid",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Attempt of accessing secure endpoint without token",
                        content = @Content)
            })
    @GetMapping("/completed")
    public ResponseEntity<Page<GoalResponseDTO>> getCompletedGoals(
            Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        logger.info(
                "Received GET request for complete goals of user: {}", userDetails.getUsername());
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to get all complete user goals");
        return ResponseEntity.ok(goalService.getCompletedUserGoals(user, pageable));
    }

    @Tag(name = "Saving goal", description = "CRUD methods for saving goal")
    @Operation(
            summary =
                    "GET a goal using its ID which belongs to the currently authenticated (logged"
                            + " in) user",
            description =
                    "Retrieves a goal using its ID. If it exists, the goal must belong to the"
                        + " authenticated user. Even if it existsit will not be returned, as long"
                        + " as it does not belong to the user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful retrieval of completed saving goals",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoalResponseDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "401",
                        description = "The JWT token is expired or its format is invalid",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Attempt of accessing secure endpoint without token",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "The goal could not be found",
                        content = @Content)
            })
    @GetMapping("/{id}")
    public ResponseEntity<GoalResponseDTO> getUserGoal(
            @Parameter(description = "The ID-number of a goal") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received GET request for goal with id {}", id);
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to find goal");
        return ResponseEntity.ok(goalService.findUserGoal(id, user));
    }

    @Tag(name = "Saving goal", description = "CRUD methods for saving goal")
    @Operation(
            summary = "Save a goal",
            description =
                    "Saves a goal under the currently authenticated (logged in) user. Up to 10"
                            + " active goalscan be saved. All goals start off as active",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful save of the saving goal",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoalResponseDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "400",
                        description =
                                "The request body is invalid OR the max limit of 10 active goals"
                                        + " has already been reached",
                        content = @Content),
                @ApiResponse(
                        responseCode = "401",
                        description = "The JWT token is expired or its format is invalid",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Attempt of accessing secure endpoint without token",
                        content = @Content)
            })
    @PostMapping
    public ResponseEntity<GoalResponseDTO> createUserGoal(
            @Valid @NotNull @RequestBody GoalCreateDTO goalDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails)
            throws BadInputException, ActiveGoalLimitExceededException {
        logger.info(
                "Received POST request for goal {} under user {}",
                goalDTO,
                userDetails.getUsername());
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to save goal");
        return ResponseEntity.ok(goalService.save(goalDTO, user));
    }

    @Tag(name = "Saving goal", description = "CRUD methods for saving goal")
    @Operation(
            summary = "Update a goal of the currently authenticated (logged in) user",
            description =
                    "Updates a goal based on its ID. If it exists, the goal must belong to the"
                        + " authenticated user. Even if it existsit will not be updated, as long as"
                        + " it does not belong to the user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful update of saving goal",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoalResponseDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "400",
                        description = "The request body is invalid",
                        content = @Content),
                @ApiResponse(
                        responseCode = "401",
                        description = "The JWT token is expired or its format is invalid",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Attempt of accessing secure endpoint without token",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "The goal could not be found",
                        content = @Content)
            })
    @PutMapping("/{id}")
    public ResponseEntity<GoalResponseDTO> updateUserGoal(
            @Parameter(description = "The ID-number of a goal") @PathVariable Long id,
            @Valid @NotNull @RequestBody GoalUpdateDTO goalDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails)
            throws BadInputException {
        logger.info("Received PUT request for goal with id {} with request body {}", id, goalDTO);
        if (bindingResult.hasErrors()) {
            throw new BadInputException("Fields in the body cannot be null, blank or empty");
        }
        User user = userService.findUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(goalService.update(id, goalDTO, user));
    }

    @Tag(name = "Saving goal", description = "CRUD methods for saving goal")
    @Operation(
            summary = "Updates the priorities of active goals",
            description =
                    "Updates the priorities of active goals based on their IDs. All the ID's must"
                            + " be active goals that belong to the authenticated user)",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful update of goal priorities",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoalResponseDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "400",
                        description = "The request body is invalid",
                        content = @Content),
                @ApiResponse(
                        responseCode = "401",
                        description = "The JWT token is expired or its format is invalid",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Attempt of accessing secure endpoint without token",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "One or more of the goals could not be found",
                        content = @Content)
            })
    @PutMapping
    public ResponseEntity<List<GoalResponseDTO>> updatePriorities(
            @Valid @RequestBody List<Long> goalIds,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received PUT request for updating priorities of goals");
        if (bindingResult.hasErrors()) {
            throw new BadInputException("Invalid request body");
        }
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Trying to update priorities");
        return ResponseEntity.ok(goalService.updatePriorities(goalIds, user));
    }

    @Tag(name = "Saving goal", description = "CRUD methods for saving goal")
    @Operation(
            summary = "DELETE a goal of the current authenticated (logged in) user",
            description =
                    "Deletes a goal using its ID. If the goal does not exist, or if the goal does"
                        + " not belong to the user, it will not be deleted, but the response is the"
                        + " same",
            responses = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Response received from deleting the goal",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoalResponseDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "401",
                        description = "The JWT token is expired or its format is invalid",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Attempt of accessing secure endpoint without token",
                        content = @Content)
            })
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

    @Tag(name = "Saving goal", description = "CRUD methods for saving goal")
    @Operation(
            summary = "Complete a goal of the currently authenticated (logged in) user",
            description =
                    "Completes a goal based on its ID. If it exists, the goal must belong to the"
                        + " authenticated user. Even if it existsit will not be updated, as long as"
                        + " it does not belong to the user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful update of saving goal",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoalResponseDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "401",
                        description = "The JWT token is expired or its format is invalid",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Attempt of accessing secure endpoint without token",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "The goal could not be found",
                        content = @Content)
            })
    @PutMapping("/{id}/complete")
    public ResponseEntity<GoalResponseDTO> completeUserGoal(
            @Parameter(description = "The ID-number of a goal") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Received PUT request for completing a goal with id {}", id);
        User user = userService.findUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(goalService.completeGoal(id, user));
    }
}
