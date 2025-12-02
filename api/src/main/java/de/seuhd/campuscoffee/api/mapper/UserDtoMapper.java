package de.seuhd.campuscoffee.api.mapper;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.domain.model.User;

public interface UserDtoMapper {
    UserDto fromDomain(User source);
    User toDomain(UserDto source);
}