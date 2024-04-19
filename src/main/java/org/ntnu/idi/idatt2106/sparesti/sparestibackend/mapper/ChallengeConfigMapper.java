package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeConfig;

@Mapper(
        componentModel = "spring",
        uses = {ChallengeTypeConfigMapper.class})
public interface ChallengeConfigMapper {

    ChallengeConfigMapper INSTANCE = Mappers.getMapper(ChallengeConfigMapper.class);

    ChallengeConfigDTO toDTO(ChallengeConfig challengeConfig);
}
