package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

/**
 * Mapper interface for converting between a User entity and register/login DTO's
 *
 * @author Yasin M.
 * @version 1.0
 * @since 19.4.24
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        imports = {UserConfig.class, java.math.BigDecimal.class})
public interface RegisterMapper {

    /**
     * Implementation instance of the mapper
     */
    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

    /**
     * Maps User register DTO to entity
     * @param request DTO
     * @param role User role
     * @param encodedPassword Encoded password
     * @return Mapped user entity
     */
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "userConfig", expression = "java( new UserConfig(role, null) )"),
        @Mapping(target = "password", source = "encodedPassword"),
        @Mapping(target = "spendingAccount", ignore = true),
        @Mapping(target = "savingAccount", ignore = true),
        @Mapping(target = "streakStart", ignore = true),
        @Mapping(target = "streak", expression = "java( 0L )"),
        @Mapping(target = "handle", ignore = true),
        @Mapping(target = "savedAmount", expression = "java( BigDecimal.ZERO )")
    })
    User toEntity(RegisterRequest request, Role role, String encodedPassword);

    /**
     * Maps User entity to DTO
     * @param user User entity
     * @param accessToken Access JWT token
     * @param refreshToken Refresh JWT token
     * @return Mapped DTO
     */
    LoginRegisterResponse toDTO(User user, String accessToken, String refreshToken);
}
