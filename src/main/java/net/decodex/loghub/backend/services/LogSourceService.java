package net.decodex.loghub.backend.services;

import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.LogSourceWithOnlineStatusDto;
import net.decodex.loghub.backend.domain.mappers.LogSourceMapper;
import net.decodex.loghub.backend.repositories.LogSessionRepository;
import net.decodex.loghub.backend.repositories.LogSourceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class LogSourceService {

    private final LogSourceRepository logSourceRepository;
    private final LogSourceMapper logSourceMapper;
    private final LogSessionRepository logSessionRepository;

    public Page<LogSourceWithOnlineStatusDto> findAll(Pageable pageable, Predicate predicate, Principal principal) {
        var result = logSourceRepository.findAll(predicate, pageable).map(logSourceMapper::toDtoWithOnlineStatus);
        result.forEach(logSourceWithOnlineStatusDto -> logSourceWithOnlineStatusDto.setOnline(logSessionRepository.existsBySource_LogSourceIdAndEndTimeNull(logSourceWithOnlineStatusDto.getLogSourceId())));
        return result;
    }
}
