package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        imports = UserConfig.class)
public interface RegisterMapper {

    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "userConfig", expression = "java( new UserConfig(role, null) )"),
        @Mapping(target = "password", source = "encodedPassword")
    })
    User toEntity(RegisterRequest request, Role role, String encodedPassword);

    @Mapping(target = "userId", source = "user.id")
    LoginRegisterResponse toDTO(User user, String accessToken, String refreshToken);
}
