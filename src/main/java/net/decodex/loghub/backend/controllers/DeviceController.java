package net.decodex.loghub.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.annotations.IsMongoId;
import net.decodex.loghub.backend.domain.dto.LogSessionDto;
import net.decodex.loghub.backend.domain.dto.LogSourceDto;
import net.decodex.loghub.backend.domain.dto.requests.CreateLogSessionDto;
import net.decodex.loghub.backend.domain.dto.requests.CreateLogSourceDto;
import net.decodex.loghub.backend.services.DeviceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/register")
    @Operation(summary = "Register device or update existing device")
    public LogSourceDto register(@RequestBody @Valid CreateLogSourceDto dto, @RequestHeader("X-API-KEY") String key) {
        return deviceService.register(dto, key);
    }

    @PostMapping("/start-session/{deviceId}")
    @Operation(summary = "Start device session")
    public LogSessionDto startSession(@PathVariable("deviceId") @IsMongoId String deviceId,
                                      @RequestBody @Valid CreateLogSessionDto dto,
                                      @RequestHeader("X-API-KEY") String key) {
        return deviceService.startSession(deviceId, dto, key);
    }

    @PostMapping("/update-session/{sessionId}")
    @Operation(summary = "Stop device session")
    public LogSessionDto updateSession(@PathVariable("sessionId") @IsMongoId String sessionId,
                                      @RequestBody @Valid CreateLogSessionDto dto,
                                      @RequestHeader("X-API-KEY") String key) {
        return deviceService.updateSession(sessionId, dto, key);
    }
}
