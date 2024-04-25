package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {AccountMapper.class, BadgeMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponse toDTO(User user);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "userConfig", ignore = true),
        @Mapping(target = "authorities", ignore = true),
        @Mapping(target = "goals", ignore = true),
        @Mapping(target = "challenges", ignore = true),
        @Mapping(target = "badges", ignore = true),
        @Mapping(target = "password", source = "encodedPassword")
    })
    void updateEntity(@MappingTarget User user, UserUpdateDTO updateDTO, String encodedPassword);
}
