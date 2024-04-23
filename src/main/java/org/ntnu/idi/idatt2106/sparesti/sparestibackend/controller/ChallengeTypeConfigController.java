package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeTypeConfigDTO;
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

    @PutMapping
    public ResponseEntity<ChallengeTypeConfigDTO> updateChallengeTypeConfig(
            @Valid @RequestBody ChallengeTypeConfigDTO challengeTypeConfigDTO,
            @AuthenticationPrincipal UserDetails userDetails,
            BindingResult bindingResult)
            throws ChallengeTypeConfigNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }

        ChallengeTypeConfigDTO updatedConfig =
                userConfigService.updateChallengeTypeConfig(
                        userDetails.getUsername(), challengeTypeConfigDTO);

        return ResponseEntity.ok(updatedConfig);
    }

    @DeleteMapping("/{type}")
    public ResponseEntity<ChallengeTypeConfigDTO> deleteChallengeTypeConfig(
            @Valid @PathVariable String type,
            @AuthenticationPrincipal UserDetails userDetails,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }

        userConfigService.deleteChallengeTypeConfig(type, userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }
}
