package net.decodex.loghub.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.RoleDto;
import net.decodex.loghub.backend.services.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    @GetMapping("")
    @Operation(summary = "Get All Roles")
    @PreAuthorize("hasAuthority('ROLE:READ')")
    public List<RoleDto> findAllPlatforms() {
        return roleService.findAll();
    }
}
