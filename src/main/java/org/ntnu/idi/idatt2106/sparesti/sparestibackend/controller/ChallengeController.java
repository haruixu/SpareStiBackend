package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.ChallengeService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("users/me/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final UserService userService;
    private final ChallengeService challengeService;

    @GetMapping
    public ResponseEntity<Page<ChallengeDTO>> getUserChallenges(
            Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUser(userDetails);
        return ResponseEntity.ok(challengeService.getChallengesByUser(user, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDTO> getUserChallenge(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        User user = getUser(userDetails);
        return ResponseEntity.ok(challengeService.getChallenge(id, user));
    }

    @PostMapping
    public ResponseEntity<ChallengeDTO> createChallenge(
            @RequestBody @Valid ChallengeDTO challengeDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUser(userDetails);
        return ResponseEntity.ok(challengeService.save(challengeDTO, user));
    }

    @PutMapping
    public ResponseEntity<ChallengeDTO> updateChallenge(
            @RequestBody @Valid ChallengeDTO challengeDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUser(userDetails);
        return ResponseEntity.ok(challengeService.updateChallenge(challengeDTO, user));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteChallenge(
            @RequestBody @Valid ChallengeDTO challengeDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUser(userDetails);
        challengeService.deleteChallenge(challengeDTO, user);
        return ResponseEntity.noContent().build();
    }

    private User getUser(UserDetails userDetails) {
        return userService.findUserByUsername(userDetails.getUsername());
    }
}
