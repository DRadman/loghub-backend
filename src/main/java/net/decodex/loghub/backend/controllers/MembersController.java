package net.decodex.loghub.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.annotations.IsMongoId;
import net.decodex.loghub.backend.domain.dto.MembersDto;
import net.decodex.loghub.backend.domain.dto.queries.MembersQueryDto;
import net.decodex.loghub.backend.services.MembersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class MembersController {

    private final MembersService membersService;

    @GetMapping("")
    @Operation(summary = "Find all members")
    @PreAuthorize("hasAuthority('USER:READ')")
    public MembersDto findAllMembers(Principal principal, @RequestParam MembersQueryDto query) {
        return membersService.findAll(principal, query);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete member by id")
    @PreAuthorize("hasAuthority('USER:DELETE')")
    public Object deleteMember(Principal principal, @PathVariable("id") @IsMongoId String id) {
        return membersService.deleteMember(id, principal);
    }

}
