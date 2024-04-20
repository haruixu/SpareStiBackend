package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.UserConfigResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;

@Mapper(
        componentModel = "spring",
        uses = {ChallengeConfigMapper.class},
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserConfigMapper {

    UserConfigMapper INSTANCE = Mappers.getMapper(UserConfigMapper.class);

    UserConfigResponse toUserConfigResponse(UserConfig userConfig);
}
