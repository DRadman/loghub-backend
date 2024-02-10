package net.decodex.loghub.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.annotations.IsMongoId;
import net.decodex.loghub.backend.domain.dto.FileDto;
import net.decodex.loghub.backend.domain.dto.PlatformDto;
import net.decodex.loghub.backend.domain.dto.requests.PlatformRequestDto;
import net.decodex.loghub.backend.services.PlatformService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/platform")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class PlatformController {

    private final PlatformService platformService;

    @GetMapping("")
    @Operation(summary = "Get All platforms")
    @PreAuthorize("hasAuthority('PLATFORM:READ')")
    public List<PlatformDto> findAllPlatforms() {
        return platformService.findAll();
    }

    @GetMapping("/{platformId}")
    @Operation(summary = "Get Platform by id")
    @PreAuthorize("hasAuthority('PLATFORM:READ')")
    public PlatformDto getPlatformById(@PathVariable("platformId") @IsMongoId String platformId) {
        return platformService.findById(platformId);
    }

    @GetMapping("/{platformId}/tags")
    @Operation(summary = "Get default tags for platform by id")
    @PreAuthorize("hasAuthority('PLATFORM:READ')")
    public List<String> getPlatformTags(@PathVariable("platformId") @IsMongoId String platformId) {
        return platformService.findPlatformDefaultTags(platformId);
    }

    @Operation(summary = "Update Platform Picture")
    @PatchMapping(path = "/{platformId}/update-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('PLATFORM:UPDATE')")
    public FileDto updatePicture(@RequestPart("picture") MultipartFile file, @PathVariable("platformId") @IsMongoId String platformId) {
        return platformService.updatePicture(file, platformId);
    }

    @Operation(summary = "Update Platform Tags")
    @PatchMapping(path = "/tags/{platformId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('PLATFORM:UPDATE')")
    public PlatformDto updatePlatform(@RequestBody @NotNull List<String> dto, @PathVariable("platformId") @IsMongoId String platformId) {
        return platformService.updatePlatformTags(dto, platformId);
    }

    @Operation(summary = "Update Platform")
    @PatchMapping(path = "/{platformId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('PLATFORM:UPDATE')")
    public PlatformDto updatePlatform(@RequestBody @Valid PlatformRequestDto dto, @PathVariable("platformId") @IsMongoId String platformId) {
        return platformService.updatePlatform(dto, platformId);
    }

    @Operation(summary = "Create Platform")
    @PostMapping(path = "")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('PLATFORM:CREATE')")
    public PlatformDto createPlatform(@RequestBody @Valid PlatformRequestDto dto) {
        return platformService.createPlatform(dto);
    }

    @Operation(summary = "Delete Platform")
    @DeleteMapping(path = "/{platformId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('PLATFORM:DELETE')")
    public Object deletePlatform(@PathVariable("platformId") @IsMongoId String platformId) {
        return platformService.deletePlatform(platformId);
    }
}
