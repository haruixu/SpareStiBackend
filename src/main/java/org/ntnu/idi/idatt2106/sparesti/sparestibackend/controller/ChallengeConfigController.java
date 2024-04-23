package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserNotFoundException;
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
@RequestMapping("/users/me/config/challenge")
public class ChallengeConfigController {

    private final UserConfigService userConfigService;

    @PostMapping
    public ResponseEntity<ChallengeConfigDTO> createChallengeConfig(
            @Valid @RequestBody ChallengeConfigDTO challengeConfigDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails)
            throws UserNotFoundException, BadInputException {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }
        ChallengeConfigDTO newConfig =
                userConfigService.createChallengeConfig(
                        userDetails.getUsername(), challengeConfigDTO);
        return ResponseEntity.ok(newConfig);
    }

    @GetMapping
    public ResponseEntity<ChallengeConfigDTO> getChallengeConfig(
            @AuthenticationPrincipal UserDetails userDetails, BindingResult bindingResult)
            throws ChallengeConfigNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }
        ChallengeConfigDTO config = userConfigService.getChallengeConfig(userDetails.getUsername());

        return ResponseEntity.ok(config);
    }

    @PutMapping
    public ResponseEntity<ChallengeConfigDTO> updateChallengeConfig(
            @Valid @RequestBody ChallengeConfigDTO challengeConfigDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails)
            throws ChallengeConfigNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }

        ChallengeConfigDTO updatedConfig =
                userConfigService.updateChallengeConfig(
                        userDetails.getUsername(), challengeConfigDTO);

        return ResponseEntity.ok(updatedConfig);
    }
}
