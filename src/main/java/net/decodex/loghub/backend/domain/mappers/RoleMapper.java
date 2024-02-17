package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.dto.RoleDto;
import net.decodex.loghub.backend.domain.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    RoleDto toDto(Role role);
}