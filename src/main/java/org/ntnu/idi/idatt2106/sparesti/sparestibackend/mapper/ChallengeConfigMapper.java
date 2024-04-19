package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeConfig;

@Mapper(
        componentModel = "spring",
        uses = {ChallengeTypeConfigMapper.class})
public abstract class ChallengeConfigMapper {

    ChallengeConfigMapper INSTANCE = Mappers.getMapper(ChallengeConfigMapper.class);

    public abstract ChallengeConfigDTO toDTO(ChallengeConfig challengeConfig);
}
