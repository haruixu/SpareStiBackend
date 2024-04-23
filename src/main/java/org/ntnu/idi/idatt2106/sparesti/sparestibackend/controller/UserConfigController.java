package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("users/me/config")
public class UserConfigController {

    private final UserConfigService userConfigService;

    public UserConfigController(UserConfigService userConfigService) {
        this.userConfigService = userConfigService;
    }

    @GetMapping
    public ResponseEntity<UserConfigDTO> getUserConfig(
            @AuthenticationPrincipal UserDetails userDetails)
            throws UserNotFoundException, ConfigNotFoundException {
        UserConfigDTO userConfig = userConfigService.getUserConfig(userDetails.getUsername());
        return ResponseEntity.ok(userConfig);
    }

    // NB! USER CONFIG IS CREATED FROM BEFORE. SEE CHALLENGE CONFIG TO EDIT CHALLENGE CONFIG
    //@PostMapping
    //public ResponseEntity<UserConfigDTO> postUserConfig(
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
    //}
}
