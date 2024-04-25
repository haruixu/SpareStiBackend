package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeTypeConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserConfigService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ApplicationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/me/config/challenge/type")
@Tag(
        name = "Challenge Type Config",
        description = "Endpoints for managing challenge type configurations")
public class ChallengeTypeConfigController {

    private final UserConfigService userConfigService;

    @PostMapping
    @Operation(
            summary = "Create Challenge Type Config",
            description = "Creates a new challenge type configuration for the authenticated user.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Challenge type config created",
                        content = {
                            @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema =
                                            @io.swagger.v3.oas.annotations.media.Schema(
                                                    implementation = ChallengeTypeConfigDTO.class))
                        }),
                @ApiResponse(responseCode = "400", description = "Bad input"),
                @ApiResponse(responseCode = "404", description = "Challenge config not found")
            })
    public ResponseEntity<ChallengeTypeConfigDTO> createChallengeTypeConfig(
            @Valid @RequestBody ChallengeTypeConfigDTO challengeTypeConfigDTO,
            @AuthenticationPrincipal UserDetails userDetails,
            BindingResult bindingResult)
            throws ChallengeConfigNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }
        log.info(
                "Creating challenge type config: {} for user: {}",
                challengeTypeConfigDTO,
                userDetails.getUsername());
        ChallengeTypeConfigDTO newConfig =
                userConfigService.createChallengeTypeConfig(
                        userDetails.getUsername(), challengeTypeConfigDTO);
        return ResponseEntity.ok(newConfig);
    }

    @GetMapping("/{type}")
    @Operation(
            summary = "Get Challenge Type Config",
            description =
                    "Retrieves the challenge type configuration for the authenticated user by"
                            + " type.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Challenge type config retrieved",
                        content = {
                            @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema =
                                            @io.swagger.v3.oas.annotations.media.Schema(
                                                    implementation = ChallengeTypeConfigDTO.class))
                        }),
                @ApiResponse(responseCode = "404", description = "Challenge type config not found")
            })
    public ResponseEntity<ChallengeTypeConfigDTO> getChallengeTypeConfig(
            @PathVariable String type, @AuthenticationPrincipal UserDetails userDetails)
            throws ChallengeTypeConfigNotFoundException {
        log.info(
                "Getting challenge type config for user '{}' and type {}",
                userDetails.getUsername(),
                type);
        ChallengeTypeConfigDTO config =
                userConfigService.getChallengeTypeConfig(type, userDetails.getUsername());
        log.info("Successfully retrieved challenge type config '{}'", config);
        return ResponseEntity.ok(config);
    }

    @PutMapping
    @Operation(
            summary = "Update Challenge Type Config",
            description = "Updates the challenge type configuration for the authenticated user.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Challenge type config updated",
                        content = {
                            @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema =
                                            @io.swagger.v3.oas.annotations.media.Schema(
                                                    implementation = ChallengeTypeConfigDTO.class))
                        }),
                @ApiResponse(responseCode = "400", description = "Bad input"),
                @ApiResponse(responseCode = "404", description = "Challenge type config not found")
            })
    public ResponseEntity<ChallengeTypeConfigDTO> updateChallengeTypeConfig(
            @Parameter(description = "Updated challenge type config details") @Valid @RequestBody
                    ChallengeTypeConfigDTO challengeTypeConfigDTO,
            @Parameter(description = "Details of the authenticated user") @AuthenticationPrincipal
                    UserDetails userDetails,
            BindingResult bindingResult)
            throws ChallengeTypeConfigNotFoundException, BadInputException {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }

        log.info(
                "Received request to update challenge type config for user: {}",
                userDetails.getUsername());
        ChallengeTypeConfigDTO updatedConfig =
                userConfigService.updateChallengeTypeConfig(
                        userDetails.getUsername(), challengeTypeConfigDTO);
        log.info(
                "Successfully updated challenge type config for user: {} to {}",
                userDetails.getUsername(),
                updatedConfig);

        return ResponseEntity.ok(updatedConfig);
    }

    @DeleteMapping("/{type}")
    @Operation(
            summary = "Delete Challenge Type Config",
            description =
                    "Deletes the challenge type configuration for the authenticated user by type.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Challenge type config deleted"),
                @ApiResponse(responseCode = "404", description = "Challenge type config not found")
            })
    public ResponseEntity<Void> deleteChallengeTypeConfig(
            @Parameter(description = "Type of the challenge config to delete") @PathVariable
                    String type,
            @Parameter(description = "Details of the authenticated user") @AuthenticationPrincipal
                    UserDetails userDetails) {
        log.info(
                "Received delete request by '{}' for challenge type config {}",
                userDetails.getUsername(),
                type);
        userConfigService.deleteChallengeTypeConfig(type, userDetails.getUsername());

        log.info("Successfully deleted challenge type config");
        return ResponseEntity.noContent().build();
    }
}
