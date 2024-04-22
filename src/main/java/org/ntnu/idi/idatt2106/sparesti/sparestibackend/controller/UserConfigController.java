package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("users/{id}/config")
public class UserConfigController {

    private final UserConfigService userConfigService;

    public UserConfigController(UserConfigService userConfigService) {
        this.userConfigService = userConfigService;
    }

    @GetMapping
    public ResponseEntity<UserConfigDTO> getUserConfig(@PathVariable("id") Long id)
            throws UserNotFoundException, ConfigNotFoundException {
        UserConfigDTO userConfig = userConfigService.getUserConfig(id);
        return ResponseEntity.ok(userConfig);
    }

    @PostMapping
    public ResponseEntity<UserConfigDTO> postUserConfig(
            @NotNull @PathVariable("id") Long userId,
            @Valid @RequestBody UserConfigDTO userConfigDTO)
            throws UserNotFoundException {
        UserConfigDTO newConfig = userConfigService.updateUserConfig(userId, userConfigDTO);
        return ResponseEntity.ok(newConfig);
    }
}
