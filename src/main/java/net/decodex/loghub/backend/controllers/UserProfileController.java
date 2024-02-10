package net.decodex.loghub.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.requests.UserProfileChangeRequestDto;
import net.decodex.loghub.backend.domain.dto.requests.ChangePasswordRequestDto;
import net.decodex.loghub.backend.domain.dto.FileDto;
import net.decodex.loghub.backend.domain.dto.requests.ResetPasswordRequestDto;
import net.decodex.loghub.backend.domain.dto.UserDto;
import net.decodex.loghub.backend.services.UserProfileService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user-profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PatchMapping("")
    @Operation(summary = "Update user profile")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDto updateUserProfile(@RequestBody @Valid UserProfileChangeRequestDto dto, Principal principal) {
        return userProfileService.updateUserProfile(dto, principal);
    }

    @PatchMapping("/change-password")
    @Operation(summary = "Change password")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDto updateUserProfile(@RequestBody @Valid ChangePasswordRequestDto dto, Principal principal) {
        return userProfileService.changePassword(dto, principal);
    }

    @Operation(summary = "Update User Picture")
    @PatchMapping(path = "/update-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    public FileDto updatePicture(@RequestPart("picture") MultipartFile file, Principal principal) {
        return userProfileService.updatePicture(file, principal);
    }

    @Operation(summary = "Reset User password")
    @PostMapping("/reset-password")
    public UserDto resetPassword(@RequestBody @Valid ResetPasswordRequestDto dto) {
        return userProfileService.resetPassword(dto);
    }

}
