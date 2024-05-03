package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config.ConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.user.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing endpoints of a user's configuration
 *
 * @author Yasin M.
 * @version 1.0
 * @since 19.4.24
 */
@RestController
@CrossOrigin
@RequestMapping("/config")
@RequiredArgsConstructor
@Tag(
        name = "User and challenge configuration",
        description = "Endpoints for managing user and challenge configurations")
public class UserConfigController {

    private final UserConfigService userConfigService;

    /**
     * Gets a user's configuration
     * @param userDetails Current user
     * @return User configuration
     * @throws UserNotFoundException User not found
     * @throws ConfigNotFoundException If user has not created config
     */
    @Operation(
            summary = "Get user configuration",
            description = "Retrieve the configuration of the currently authenticated user.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User configuration found",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserConfigDTO.class))
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
                        description = "User or configuration not found",
                        content = @Content)
            })
    @GetMapping
    public ResponseEntity<UserConfigDTO> getUserConfig(
            @Parameter(description = "Authenticated user details") @AuthenticationPrincipal
                    UserDetails userDetails)
            throws UserNotFoundException, ConfigNotFoundException {
        UserConfigDTO userConfig = userConfigService.getUserConfig(userDetails.getUsername());
        return ResponseEntity.ok(userConfig);
    }

    // NB! USER CONFIG IS CREATED FROM BEFORE. SEE CHALLENGE CONFIG TO EDIT CHALLENGE CONFIG
    // @PostMapping
    // public ResponseEntity<UserConfigDTO> postUserConfig(
    //        @Valid @RequestBody UserConfigDTO userConfigDTO,
    //        BindingResult bindingResult,
    //        @AuthenticationPrincipal UserDetails userDetails)
    //        throws UserNotFoundException, BadInputException {
    //    if (bindingResult.hasErrors()) {
    //        throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
    //    }
    //    UserConfigDTO newConfig =
    //            userConfigService.createUserConfig(userDetails.getUsername(), userConfigDTO);
    //    return ResponseEntity.ok(newConfig);
    // }
}
