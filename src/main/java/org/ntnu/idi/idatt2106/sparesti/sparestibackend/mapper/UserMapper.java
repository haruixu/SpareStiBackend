package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import java.time.ZonedDateTime;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.StreakResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;

/**
 * Mapper interface for converting back and forth
 * from a DTO to a User entity.
 *
 * @author Yasin M.
 * @version 1.0
 * @since 25.4.24
 */
@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {AccountMapper.class, BadgeMapper.class})
public interface UserMapper {

    /**
     * Implementation instance of the mapper
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Maps from User entity to DTO
     * @param user User entity
     * @return Mapped DTO
     */
    @Mapping(target = "hasPasskey", expression = "java( user.getHandle() != null ? true : false )")
    UserResponse toDTO(User user);

    /**
     * Updates User entity from DTO
     * @param user User entity
     * @param updateDTO DTO with new changes
     * @param encodedPassword Encoded password
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "userConfig", ignore = true),
        @Mapping(target = "authorities", ignore = true),
        @Mapping(target = "goals", ignore = true),
        @Mapping(target = "challenges", ignore = true),
        @Mapping(target = "badges", ignore = true),
        @Mapping(target = "password", source = "encodedPassword"),
        @Mapping(target = "streakStart", ignore = true),
        @Mapping(target = "streak", ignore = true),
        @Mapping(target = "handle", ignore = true),
        @Mapping(target = "savedAmount", ignore = true),
        @Mapping(target = "username", ignore = true)
    })
    void updateEntity(@MappingTarget User user, UserUpdateDTO updateDTO, String encodedPassword);

    /**
     * Maps from User to Streak DTO
     * @param user User entity
     * @param firstDue Date where streak is due
     * @return Mapped DTO
     */
    StreakResponse toStreakResponse(User user, ZonedDateTime firstDue);
}
