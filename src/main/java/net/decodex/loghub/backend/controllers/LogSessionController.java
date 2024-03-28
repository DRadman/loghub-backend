package net.decodex.loghub.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.LogSessionWithSourceIdDto;
import net.decodex.loghub.backend.domain.dto.queries.LogSessionQueryDto;
import net.decodex.loghub.backend.services.LogSessionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/log-session")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class LogSessionController {

    private final LogSessionService logSessionService;

    @GetMapping("")
    @Operation(summary = "Retrieves all log sessions")
    @PreAuthorize("hasAuthority('LOG_SESSION:READ')")
    public Page<LogSessionWithSourceIdDto> findAll(@PageableDefault Pageable pageable,
                                                   LogSessionQueryDto filter,
                                                   Principal principal) {
        return logSessionService.findAll(pageable, filter, principal);
    }

}
