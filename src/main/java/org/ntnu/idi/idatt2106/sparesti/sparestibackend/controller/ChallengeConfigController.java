package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller used to manage endpoints related to a user's challenge configuration.
 *
 * @author Yasin A.M.
 * @version 1.0
 * @since 23.4.24
 */
@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/config/challenge")
@Tag(name = "Challenge Config", description = "Endpoints for managing challenge configurations")
public class ChallengeConfigController {

    private final UserConfigService userConfigService;

    /**
     * Creates a challenge config
     * @param challengeConfigDTO Wrapper for challenge config
     * @param userDetails Current user
     * @return Wrapper for the created challenge config info
     * @throws UserNotFoundException If the user was not found
     * @throws BadInputException For bad user input
     * @throws ObjectNotValidException If supplied config data is invalid
     */
    @PostMapping
    @Operation(
            summary = "Create challenge config",
            description = "Creates a new challenge config for the authenticated user.",
            tags = {"Challenge Config"})
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Challenge config created",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                ChallengeConfigDTO.class))),
                @ApiResponse(responseCode = "404", description = "User not found"),
                @ApiResponse(responseCode = "400", description = "Bad input")
            })
    public ResponseEntity<ChallengeConfigDTO> createChallengeConfig(
            @Parameter(description = "Challenge config details to create") @RequestBody
                    ChallengeConfigDTO challengeConfigDTO,
            @Parameter(description = "Details of the authenticated user") @AuthenticationPrincipal
                    UserDetails userDetails)
            throws UserNotFoundException, BadInputException, ObjectNotValidException {
        log.info(
                "Received request to create challenge config: {}, by user: {}",
                challengeConfigDTO,
                userDetails.getUsername());
        ChallengeConfigDTO newConfig =
                userConfigService.createChallengeConfig(
                        userDetails.getUsername(), challengeConfigDTO);
        log.info("Successfully created challenge config: {}", newConfig);
        return ResponseEntity.ok(newConfig);
    }

    /**
     * Gets a user's challenge config
     * @param userDetails Current user
     * @return The user's challenge config
     * @throws ChallengeConfigNotFoundException If the user has not registered a challenge config
     */
    @GetMapping
    @Operation(
            summary = "Get challenge config",
            description = "Retrieves the challenge config for the authenticated user.",
            tags = {"Challenge Config"})
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Challenge config found",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                ChallengeConfigDTO.class))),
                @ApiResponse(responseCode = "404", description = "Challenge config not found")
            })
    public ResponseEntity<ChallengeConfigDTO> getChallengeConfig(
            @Parameter(description = "Details of the authenticated user") @AuthenticationPrincipal
                    UserDetails userDetails)
            throws ChallengeConfigNotFoundException {
        log.info(
                "Received request to get challenge config for username: {}",
                userDetails.getUsername());
        ChallengeConfigDTO config = userConfigService.getChallengeConfig(userDetails.getUsername());
        log.info("Successfully retrieved challenge config: {}", config);
        return ResponseEntity.ok(config);
    }

    /**
     * Updates a user's challenge config
     * @param challengeConfigDTO Wrapper for the new challenge config data
     * @param userDetails Current user
     * @return Wrapper of for the updated challenge config's data
     * @throws ChallengeConfigNotFoundException If the user's challenge config could not be found
     * @throws BadInputException On bad user input
     * @throws ObjectNotValidException If the supplied config data is invalid
     */
    @PutMapping
    @Operation(
            summary = "Update challenge config",
            description = "Updates the challenge config for the authenticated user.",
            tags = {"Challenge Config"})
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Challenge config updated",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                ChallengeConfigDTO.class))),
                @ApiResponse(responseCode = "404", description = "Challenge config not found"),
                @ApiResponse(responseCode = "400", description = "Bad input")
            })
    public ResponseEntity<ChallengeConfigDTO> updateChallengeConfig(
            @Parameter(description = "Updated challenge config details") @RequestBody
                    ChallengeConfigDTO challengeConfigDTO,
            @Parameter(description = "Details of the authenticated user") @AuthenticationPrincipal
                    UserDetails userDetails)
            throws ChallengeConfigNotFoundException, BadInputException, ObjectNotValidException {
        log.info("Received request to update challenge config to: {}", challengeConfigDTO);
        ChallengeConfigDTO updatedConfig =
                userConfigService.updateChallengeConfig(
                        userDetails.getUsername(), challengeConfigDTO);
        log.info("Successfully updated challenge config to: {}", updatedConfig);
        return ResponseEntity.ok(updatedConfig);
    }
}
