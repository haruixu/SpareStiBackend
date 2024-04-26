package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.StreakResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/profile")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "Operations related to user profile management")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Get User",
            description = "Get user details for the currently authenticated user.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User details retrieved successfully.",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UserResponse.class))),
                @ApiResponse(responseCode = "401", description = "User is not authenticated.")
            })
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Received GET request for user by user '{}'", userDetails.getUsername());
        return ResponseEntity.ok(userService.findUserByUsernameToDTO(userDetails.getUsername()));
    }

    @PutMapping
    @Operation(
            summary = "Update User",
            description = "Update user details for the currently authenticated user.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User details updated successfully.",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UserResponse.class))),
                @ApiResponse(responseCode = "401", description = "User is not authenticated.")
            })
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserUpdateDTO userUpdateDTO) {

        log.info("Received PUT request to update user '{}'", userDetails.getUsername());
        UserResponse updatedUser = userService.updateUser(userDetails.getUsername(), userUpdateDTO);
        log.info("Successfully updated user '{}' to ", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/streak")
    public ResponseEntity<StreakResponse> getStreak(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Received GET request for streak by user '{}'", userDetails.getUsername());
        return ResponseEntity.ok(userService.getStreak(userDetails.getUsername()));
    }
}
