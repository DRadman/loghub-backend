package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.dto.ProjectReleaseDto;
import net.decodex.loghub.backend.domain.dto.requests.CreateProjectReleaseDto;
import net.decodex.loghub.backend.domain.models.ProjectRelease;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectReleaseMapper {
    ProjectRelease toEntity(CreateProjectReleaseDto createProjectReleaseDto);

    ProjectReleaseDto toDto(ProjectRelease projectRelease);
    CreateProjectReleaseDto toDto1(ProjectRelease projectRelease);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProjectRelease partialUpdate(CreateProjectReleaseDto createProjectReleaseDto, @MappingTarget ProjectRelease projectRelease);
}