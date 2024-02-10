package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.dto.PlatformDto;
import net.decodex.loghub.backend.domain.dto.requests.PlatformRequestDto;
import net.decodex.loghub.backend.domain.models.Platform;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlatformMapper {
    Platform toEntity(PlatformDto platformDto);

    PlatformDto toDto(Platform platform);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Platform partialUpdate(PlatformDto platformDto, @MappingTarget Platform platform);

    Platform toEntity(PlatformRequestDto platformDto);

    PlatformDto toDto1(Platform platform);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Platform partialUpdate(PlatformRequestDto platformRequestDto, @MappingTarget Platform platform);
}