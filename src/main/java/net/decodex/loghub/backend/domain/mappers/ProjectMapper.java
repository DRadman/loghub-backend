package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.dto.ProjectDetailsDto;
import net.decodex.loghub.backend.domain.dto.ProjectDto;
import net.decodex.loghub.backend.domain.dto.requests.ProjectRequestDto;
import net.decodex.loghub.backend.domain.models.Project;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {
    Project toEntity(ProjectDto projectDto);

    ProjectDto toDto(Project project);

    ProjectDetailsDto toDetailsDto(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Project partialUpdate(ProjectDto projectDto, @MappingTarget Project project);

    Project toEntity(ProjectRequestDto projectRequestDto);

    ProjectRequestDto toDto1(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Project partialUpdate(ProjectRequestDto projectRequestDto, @MappingTarget Project project);
}