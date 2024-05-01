package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.StreakResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.FileSystemStorageService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for managing endpoints for a user's profile
 *
 * @author Yasin M.
 * @version 1.0
 * @since 23.4.24
 */
@RestController
@CrossOrigin
@RequestMapping("/profile")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "Operations related to user profile management")
public class UserController {

    private final UserService userService;

    private final FileSystemStorageService fileSystemStorageService;

    /**
     * Gets a users profile
     * @param userDetails Current user
     * @return User profile
     */
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

    /**
     * Updates a user profile
     * @param userDetails Current user
     * @param userUpdateDTO Wrapper for user's new info
     * @return Updated user profile
     */
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

    /**
     * Gets the streak of a user
     * @param userDetails Current user
     * @return Wrapper for user's streak info
     * @throws UserNotFoundException If user could not be found
     */
    @GetMapping("/streak")
    @Operation(
            summary = "Get User Streak",
            description = "Get the current streak for the currently authenticated user.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Streak retrieved successfully.",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = StreakResponse.class))),
                @ApiResponse(responseCode = "401", description = "User is not authenticated.")
            })
    public ResponseEntity<StreakResponse> getStreak(
            @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException {
        log.info("Received GET request for streak by user '{}'", userDetails.getUsername());
        return ResponseEntity.ok(userService.getStreak(userDetails.getUsername()));
    }

    /**
     * Gets the profile picture of a user
     * @param userDetails Current user
     * @param file Profile picture
     * @return Resource wrapper for image
     * @throws IOException Upon IO-errors
     */
    @Operation(summary = "Get image", description = "Get profile pic")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully get file",
                        content = @Content),
                @ApiResponse(
                        responseCode = "401",
                        description = "Invalid or expired JWT token",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "No JWT token provided",
                        content = @Content)
            })
    @PostMapping("/picture")
    public ResponseEntity<String> handleFileUpload(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file)
            throws IOException {
        fileSystemStorageService.save(file, userDetails.getUsername() + "-P", userDetails);
        return ResponseEntity.ok("OK");
    }

    /**
     * Uploads the profile picture of a user
     * @param userDetails Current user
     * @return Resource wrapper for image
     * @throws IOException Upon IO-errors
     */
    @Operation(summary = "Upload image", description = "Uploaded profile pic")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully get file",
                        content = @Content),
                @ApiResponse(
                        responseCode = "401",
                        description = "Invalid or expired JWT token",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "No JWT token provided",
                        content = @Content)
            })
    @GetMapping("/picture")
    @ResponseBody
    public ResponseEntity<Resource> findFile(@AuthenticationPrincipal UserDetails userDetails)
            throws IOException {

        Resource file =
                fileSystemStorageService.getImage(userDetails.getUsername() + "-P", userDetails);

        if (file == null) return ResponseEntity.notFound().build();

        String mimeType;
        try {
            mimeType = Files.probeContentType(Paths.get(file.getURI()));
        } catch (IOException e) {
            mimeType = "application/octet-stream"; // default MIME type if detection fails
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(file);
    }
}
