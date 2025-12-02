package de.seuhd.campuscoffee.api.mapper;

import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.domain.model.User;

@Mapper(componentModel = "spring")
@ConditionalOnMissingBean
public interface UserDtoMapper {
    UserDto fromDomain(User source);
    User toDomain(UserDto source);
}