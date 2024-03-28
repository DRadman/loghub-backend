package net.decodex.loghub.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.LogSourceWithOnlineStatusDto;
import net.decodex.loghub.backend.domain.dto.queries.LogSourceQueryDto;
import net.decodex.loghub.backend.services.LogSourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/log-source")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class LogSourceController {

    private final LogSourceService logSourceService;

    @GetMapping("")
    @Operation(summary = "Retrieves all log sources")
    @PreAuthorize("hasAuthority('LOG_SOURCE:READ')")
    public Page<LogSourceWithOnlineStatusDto> findAll(@PageableDefault Pageable pageable,
                                                      LogSourceQueryDto filter,
                                                      Principal principal) {
        return logSourceService.findAll(pageable, filter, principal);
    }

}
