package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.UserConfigResponse;
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
    public ResponseEntity<UserConfigResponse> getUserConfig(@PathVariable("id") Long id)
            throws UserNotFoundException, ConfigNotFoundException {
        UserConfigResponse userConfig = userConfigService.getUserConfig(id);
        return ResponseEntity.ok(userConfig);
    }

    @PostMapping
    public ResponseEntity<UserConfigResponse> postUserConfig(@PathVariable("id") Long id, @RequestBody )
}
