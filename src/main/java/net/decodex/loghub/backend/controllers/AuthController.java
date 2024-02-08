package net.decodex.loghub.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import net.decodex.loghub.backend.domain.dto.*;
import net.decodex.loghub.backend.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @Operation(summary = "Authenticate user by username, and password")
    @PostMapping(path = "/authenticate")
    public TokenResponseDto authenticate(@RequestBody @Valid AuthenticationRequestDto authDto) {
        return authService.authenticate(authDto);
    }

    @Operation(summary = "Refresh token to get new access token")
    @PostMapping("/refreshToken")
    public TokenResponseDto refreshToken(@RequestBody @Valid RefreshTokenRequestDto dto) {
        return authService.refreshToken(dto);
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public UserDto registerNewUser(@RequestBody @Valid RegisterUserRequestDto dto) {
        return authService.registerNewUser(dto);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get Current Authenticated User information")
    @GetMapping("/me")
    public UserDto me(Principal principal) {
        return authService.getLoggedUserInformation(principal.getName());
    }

    @Operation(summary = "Retrieves information whether username is taken or not")
    @GetMapping("/username/taken")
    public boolean isUsernameTaken(@RequestParam String username) {
        return authService.isUsernameTaken(username);
    }
}