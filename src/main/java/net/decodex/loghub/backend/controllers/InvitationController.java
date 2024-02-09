package net.decodex.loghub.backend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.CreateInvitationDto;
import net.decodex.loghub.backend.domain.dto.InvitationDto;
import net.decodex.loghub.backend.domain.dto.requests.RegisterUserRequestDto;
import net.decodex.loghub.backend.domain.dto.UserDto;
import net.decodex.loghub.backend.services.InvitationService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/invitation")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @GetMapping("/details/{invitationId}")
    public InvitationDto getInvitationDetails(@PathVariable("invitationId") String invitationId) {
        return invitationService.findById(invitationId);
    }

    @PostMapping("/send")
    @SecurityRequirement(name = "Bearer Authentication")
    public InvitationDto sendInvitation(Principal principal, @RequestBody @Valid CreateInvitationDto dto) {
        return invitationService.sendInvitation(principal, dto);
    }

    @PostMapping("/accept/{invitationId}")
    public UserDto acceptInvitation(@PathVariable("invitationId") String invitationId,
                                    @RequestBody @Valid RegisterUserRequestDto userDto) {
        return invitationService.acceptInvitation(invitationId, userDto);
    }
}
