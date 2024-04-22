package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.ChallengeService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ApplicationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("users/me/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final UserService userService;
    private final ChallengeService challengeService;

    @GetMapping
    public ResponseEntity<Page<ChallengeDTO>> getUserChallenges(
            Pageable pageable, @AuthenticationPrincipal UserDetails userDetails)
            throws ChallengeNotFoundException, UserNotFoundException {
        log.info("Received GET request for challenges by username: {}", userDetails.getUsername());
        User user = getUser(userDetails);

        Page<ChallengeDTO> challenges = challengeService.getChallengesByUser(user, pageable);
        log.info("Retrieved challenges: {}", challenges);
        return ResponseEntity.ok(challenges);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDTO> getUserChallenge(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id)
            throws ChallengeNotFoundException, UserNotFoundException {
        log.info("Received GET request for challenge with id: {}", id);
        User user = getUser(userDetails);
        ChallengeDTO retrievedChallenge = challengeService.getChallenge(id, user);
        log.info("Retrieved challenge: {}", retrievedChallenge);
        return ResponseEntity.ok(retrievedChallenge);
    }

    @PostMapping
    public ResponseEntity<ChallengeDTO> createChallenge(
            @RequestBody @Valid ChallengeDTO challengeDTO,
            @AuthenticationPrincipal UserDetails userDetails,
            BindingResult bindingResult)
            throws ChallengeNotFoundException, UserNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }
        log.info("Received POST request for challenge username: {}", userDetails.getUsername());
        User user = getUser(userDetails);

        ChallengeDTO createdChallenge = challengeService.save(challengeDTO, user);
        log.info("Created challenge: {}", createdChallenge);
        return ResponseEntity.ok(createdChallenge);
    }

    @PutMapping
    public ResponseEntity<ChallengeDTO> updateChallenge(
            @RequestBody @Valid ChallengeDTO challengeDTO,
            @AuthenticationPrincipal UserDetails userDetails,
            BindingResult bindingResult)
            throws ChallengeNotFoundException, UserNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }
        log.info("Received PUT request for challenge with id: {}", challengeDTO.id());
        User user = getUser(userDetails);
        ChallengeDTO updatedChallenge = challengeService.updateChallenge(challengeDTO, user);

        log.info("Updated challenge to: {}", challengeDTO);
        return ResponseEntity.ok(updatedChallenge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(
            @NotNull @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails)
            throws ChallengeNotFoundException, UserNotFoundException {
        log.info("Received DELETE request for challenge with id: {}", id);
        User user = getUser(userDetails);
        challengeService.deleteChallenge(id, user);
        log.info("Deleted challenge with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    private User getUser(UserDetails userDetails) throws UserNotFoundException {
        return userService.findUserByUsername(userDetails.getUsername());
    }
}
