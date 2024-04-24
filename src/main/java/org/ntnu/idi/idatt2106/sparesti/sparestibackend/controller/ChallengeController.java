package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.ChallengeService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("users/me/challenges")
@Tag(name = "Challenges", description = "Endpoints for managing user challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final UserService userService;
    private final ChallengeService challengeService;

    @Operation(
            summary = "Get user challenges",
            description = "Retrieve challenges associated with the authenticated user.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Challenges found"),
                @ApiResponse(responseCode = "404", description = "Challenges or user not found")
            })
    @GetMapping
    public ResponseEntity<Page<ChallengeDTO>> getUserChallenges(
            @Parameter(description = "Pageable object for pagination") Pageable pageable,
            @Parameter(description = "Details of the authenticated user") @AuthenticationPrincipal
                    UserDetails userDetails)
            throws ChallengeNotFoundException, UserNotFoundException {
        log.info("Received GET request for challenges by username: {}", userDetails.getUsername());
        User user = getUser(userDetails);

        Page<ChallengeDTO> challenges = challengeService.getChallengesByUser(user, pageable);
        log.info("Retrieved challenges: {}", challenges);
        return ResponseEntity.ok(challenges);
    }

    @Operation(
            summary = "Get active challenges",
            description = "Retrieve active challenges associated with the authenticated user.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Active challenges found"),
                @ApiResponse(responseCode = "404", description = "User not found")
            })
    @GetMapping("/active")
    public ResponseEntity<Page<ChallengeDTO>> getActiveChallenges(
            Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        log.info(
                "Received GET request for active challenges by user: {}",
                userDetails.getUsername());
        User user = userService.findUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(challengeService.getActiveChallenges(user, pageable));
    }

    @Operation(
            summary = "Get completed challenges",
            description = "Retrieve completed challenges associated with the authenticated user.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Completed challenges found"),
                @ApiResponse(responseCode = "404", description = "User not found")
            })
    @GetMapping("/completed")
    public ResponseEntity<Page<ChallengeDTO>> getCompletedChallenges(
            Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        log.info(
                "Received GET request for completed challenges by user: {}",
                userDetails.getUsername());
        User user = getUser(userDetails);
        return ResponseEntity.ok(challengeService.getCompletedChallenges(user, pageable));
    }

    @Operation(
            summary = "Get user challenge",
            description = "Retrieve a specific challenge for the authenticated user by ID.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Challenge found"),
                @ApiResponse(responseCode = "404", description = "Challenge or user not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDTO> getUserChallenge(
            @Parameter(description = "Details of the authenticated user") @AuthenticationPrincipal
                    UserDetails userDetails,
            @Parameter(description = "ID of the challenge to retrieve", example = "123")
                    @PathVariable
                    Long id)
            throws ChallengeNotFoundException, UserNotFoundException {
        log.info("Received GET request for challenge with id: {}", id);
        User user = getUser(userDetails);
        ChallengeDTO retrievedChallenge = challengeService.getChallenge(id, user);
        log.info("Retrieved challenge: {}", retrievedChallenge);
        return ResponseEntity.ok(retrievedChallenge);
    }

    @Operation(
            summary = "Create challenge",
            description = "Creates a new challenge for the authenticated user.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Challenge created"),
                @ApiResponse(responseCode = "404", description = "User not found"),
                @ApiResponse(responseCode = "400", description = "Bad input")
            })
    @PostMapping
    public ResponseEntity<ChallengeDTO> createChallenge(
            @Parameter(description = "Challenge details to create") @RequestBody
                    ChallengeCreateDTO challengeCreateDTO,
            @Parameter(description = "Details of the authenticated user") @AuthenticationPrincipal
                    UserDetails userDetails)
            throws ChallengeNotFoundException, UserNotFoundException, BadInputException {
        log.info("Received POST request for challenge username: {}", userDetails.getUsername());
        User user = getUser(userDetails);

        ChallengeDTO createdChallenge = challengeService.save(challengeCreateDTO, user);
        log.info("Created challenge: {}", createdChallenge);
        return ResponseEntity.ok(createdChallenge);
    }

    @Operation(
            summary = "Complete challenge",
            description = "Marks a challenge as completed for the authenticated user.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Challenge completed"),
                @ApiResponse(responseCode = "404", description = "Challenge not found")
            })
    @PutMapping("/{id}/complete")
    public ResponseEntity<ChallengeDTO> completeChallenge(
            @Parameter(description = "ID of the challenge to complete") @PathVariable Long id,
            @Parameter(description = "Details of the authenticated user") @AuthenticationPrincipal
                    UserDetails userDetails) {
        log.info(
                "Received request by user '{}' to complete challenge with id: {}",
                userDetails.getUsername(),
                id);
        User user = getUser(userDetails);
        ChallengeDTO completedChallenge = challengeService.completeChallenge(id, user);
        log.info(
                "Completion status of challenge with id {} set to: {}",
                completedChallenge.id(),
                completedChallenge.completedOn());
        return ResponseEntity.ok(completedChallenge);
    }

    @Operation(
            summary = "Update challenge",
            description = "Updates an existing challenge for the authenticated user.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Challenge updated"),
                @ApiResponse(responseCode = "404", description = "Challenge or user not found"),
                @ApiResponse(responseCode = "400", description = "Bad input")
            })
    @PutMapping("/{id}")
    public ResponseEntity<ChallengeDTO> updateChallenge(
            @Parameter(description = "ID of the challenge to update") @PathVariable Long id,
            @Parameter(description = "Updated challenge details") @RequestBody
                    ChallengeUpdateDTO challengeUpdateDTO,
            @Parameter(description = "Details of the authenticated user") @AuthenticationPrincipal
                    UserDetails userDetails)
            throws ChallengeNotFoundException, UserNotFoundException, BadInputException {
        log.info("Received PUT request for challenge with id: {}", id);
        User user = getUser(userDetails);
        ChallengeDTO updatedChallenge =
                challengeService.updateChallenge(id, challengeUpdateDTO, user);

        log.info("Updated challenge to: {}", updatedChallenge);
        return ResponseEntity.ok(updatedChallenge);
    }

    @Operation(
            summary = "Delete challenge",
            description = "Deletes a specific challenge for the authenticated user by ID.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Challenge deleted"),
                @ApiResponse(responseCode = "404", description = "Challenge or user not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(
            @Parameter(description = "ID of the challenge to delete") @NotNull @PathVariable
                    Long id,
            @Parameter(description = "Details of the authenticated user") @AuthenticationPrincipal
                    UserDetails userDetails)
            throws ChallengeNotFoundException, UserNotFoundException {
        log.info("Received DELETE request for challenge with id: {}", id);
        User user = getUser(userDetails);
        challengeService.deleteChallenge(id, user);
        log.info("Deleted challenge with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves the user based on the UserDetails.
     *
     * @param userDetails The UserDetails object representing the authenticated user.
     * @return The User object associated with the authenticated user.
     * @throws UserNotFoundException If the user is not found.
     */
    private User getUser(@Parameter(hidden = true) UserDetails userDetails)
            throws UserNotFoundException {
        return userService.findUserByUsername(userDetails.getUsername());
    }
}
