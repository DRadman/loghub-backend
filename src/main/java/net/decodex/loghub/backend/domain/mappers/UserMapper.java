package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.dto.requests.UserProfileChangeRequestDto;
import net.decodex.loghub.backend.domain.dto.requests.RegisterUserRequestDto;
import net.decodex.loghub.backend.domain.dto.UserDto;
import net.decodex.loghub.backend.domain.models.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);

    User toEntity(RegisterUserRequestDto registerUserRequestDto);

    RegisterUserRequestDto toDto1(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(RegisterUserRequestDto registerUserRequestDto, @MappingTarget User user);

    User toEntity(UserProfileChangeRequestDto userProfileChangeRequestDto);

    UserProfileChangeRequestDto toDto2(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserProfileChangeRequestDto userProfileChangeRequestDto, @MappingTarget User user);
}