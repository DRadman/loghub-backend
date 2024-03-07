package net.decodex.loghub.backend.services;

import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.LogSessionDto;
import net.decodex.loghub.backend.domain.dto.LogSourceDto;
import net.decodex.loghub.backend.domain.dto.requests.CreateLogSessionDto;
import net.decodex.loghub.backend.domain.dto.requests.CreateLogSourceDto;
import net.decodex.loghub.backend.domain.mappers.LogSessionMapper;
import net.decodex.loghub.backend.domain.mappers.LogSourceMapper;
import net.decodex.loghub.backend.domain.models.LogSource;
import net.decodex.loghub.backend.exceptions.specifications.ResourceNotFoundException;
import net.decodex.loghub.backend.repositories.LogSessionRepository;
import net.decodex.loghub.backend.repositories.LogSourceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final ProjectService projectService;
    private final LogSourceRepository logSourceRepository;
    private final LogSourceMapper logSourceMapper;
    private final LogSessionMapper logSessionMapper;
    private final LogSessionRepository logSessionRepository;

    public LogSourceDto register(CreateLogSourceDto dto, String key) {
        var project = projectService.getProjectByKey(key);
        var existingSource = logSourceRepository.findByUniqueIdentifier(project.getProjectId() + "_" + dto.getUniqueIdentifier());
        LogSource logSource;
        if (existingSource.isPresent()) {
            logSource = existingSource.get();
            logSource = logSourceMapper.partialUpdate(dto, logSource);
            logSource.setUniqueIdentifier(project.getProjectId() + "_" + dto.getUniqueIdentifier());
        } else {
            logSource = logSourceMapper.toEntity(dto);
            logSource.setUniqueIdentifier(project.getProjectId() + "_" + dto.getUniqueIdentifier());
        }

        logSource.setProject(project);
        if (dto.getReleaseId() != null && !dto.getReleaseId().equalsIgnoreCase("unknown")) {
            var result = project.getReleases().stream()
                    .filter(release -> dto.getReleaseId().equalsIgnoreCase(release.getReleaseId()))
                    .findFirst();
            //noinspection OptionalGetWithoutIsPresent
            logSource.setRelease(result.get());
        }
        logSource = logSourceRepository.save(logSource);
        return logSourceMapper.toDto(logSource);
    }

    public LogSessionDto startSession(String deviceId, CreateLogSessionDto dto, String key) {
        var project = projectService.getProjectByKey(key);
        var logSourceOp = logSourceRepository.findById(deviceId);
        if (logSourceOp.isEmpty()) {
            throw new ResourceNotFoundException("LogSource", "logSourceId", deviceId);
        }

        var logSource = logSourceOp.get();
        var logSession = logSessionMapper.toEntity(dto);
        logSession.setSource(logSource);
        logSession.setProject(project);

        var lastLogSessionOp = logSessionRepository.findFirstBySourceOrderByStartTimeDesc(logSource);
        if (lastLogSessionOp.isPresent() && lastLogSessionOp.get().getEndTime() == null) {
            var lastSession = lastLogSessionOp.get();
            lastSession.setEndTime(LocalDateTime.now());
            logSessionRepository.save(lastSession);
        }

        logSession = logSessionRepository.save(logSession);
        return logSessionMapper.toDto(logSession);
    }

    public LogSessionDto updateSession(String sessionId, CreateLogSessionDto dto, String key) {
        projectService.getProjectByKey(key);
        var logSessionOp = logSessionRepository.findById(sessionId);
        if (logSessionOp.isEmpty()) {
            throw new ResourceNotFoundException("LogSession", "logSessionId", sessionId);
        }
        var logSession = logSessionOp.get();
        logSession = logSessionMapper.partialUpdate(dto, logSession);
        logSession = logSessionRepository.save(logSession);

        return logSessionMapper.toDto(logSession);
    }
}
