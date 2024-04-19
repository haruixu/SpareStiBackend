package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RegisterMapper {

    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "userConfig", ignore = true)
    })
    User toEntity(RegisterRequest request);

    @Mappings({
        @Mapping(target = "accessToken", ignore = true),
        @Mapping(target = "refreshToken", ignore = true)
    })
    LoginRegisterResponse toDTO(User user);
}
