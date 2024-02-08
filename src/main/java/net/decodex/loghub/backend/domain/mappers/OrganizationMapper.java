package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.models.Organization;
import net.decodex.loghub.backend.domain.dto.OrganizationDto;
import net.decodex.loghub.backend.services.CreateOrganizationRequestDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper {
    Organization toEntity(OrganizationDto organizationDto);

    OrganizationDto toDto(Organization organization);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Organization partialUpdate(OrganizationDto organizationDto, @MappingTarget Organization organization);

    Organization toEntity(CreateOrganizationRequestDto createOrganizationRequestDto);

    CreateOrganizationRequestDto toDto1(Organization organization);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Organization partialUpdate(CreateOrganizationRequestDto createOrganizationRequestDto, @MappingTarget Organization organization);
}