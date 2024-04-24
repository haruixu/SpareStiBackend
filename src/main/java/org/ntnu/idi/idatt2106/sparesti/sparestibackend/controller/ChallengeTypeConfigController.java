package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class ChallengeTypeConfigController {

    private final UserConfigService userConfigService;

    @PostMapping
    public ResponseEntity<ChallengeTypeConfigDTO> createChallengeTypeConfig(
            @Valid @RequestBody ChallengeTypeConfigDTO challengeTypeConfigDTO,
            @AuthenticationPrincipal UserDetails userDetails,
            BindingResult bindingResult)
            throws ChallengeConfigNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }
        ChallengeTypeConfigDTO newConfig =
                userConfigService.createChallengeTypeConfig(
                        userDetails.getUsername(), challengeTypeConfigDTO);
        return ResponseEntity.ok(newConfig);
    }

    @GetMapping("/{type}")
    public ResponseEntity<ChallengeTypeConfigDTO> getChallengeTypeConfig(
            @PathVariable String type, @AuthenticationPrincipal UserDetails userDetails)
            throws ChallengeTypeConfigNotFoundException {
        ChallengeTypeConfigDTO config =
                userConfigService.getChallengeTypeConfig(type, userDetails.getUsername());
        return ResponseEntity.ok(config);
    }

    @Operation(
            summary = "Update challenge type config",
            description = "Updates the challenge type config for the authenticated user.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Challenge type config updated"),
                @ApiResponse(responseCode = "404", description = "Challenge type config not found"),
                @ApiResponse(responseCode = "400", description = "Bad input")
            })
    @PutMapping
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

        ChallengeTypeConfigDTO updatedConfig =
                userConfigService.updateChallengeTypeConfig(
                        userDetails.getUsername(), challengeTypeConfigDTO);

        return ResponseEntity.ok(updatedConfig);
    }

    @Operation(
            summary = "Delete challenge type config",
            description = "Deletes the challenge type config for the authenticated user by type.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Challenge type config deleted"),
                @ApiResponse(responseCode = "404", description = "Challenge type config not found")
            })
    @DeleteMapping("/{type}")
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
