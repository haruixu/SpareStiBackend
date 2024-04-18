package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import java.math.RoundingMode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ApplicationUtil;

@Mapper(componentModel = "spring", imports = ApplicationUtil.class)
public interface ChallengeMapper {

    ChallengeMapper INSTANCE = Mappers.getMapper(ChallengeMapper.class);
    RoundingMode roundingMode = RoundingMode.HALF_UP;

    @Mapping(
            target = "completion",
            expression =
                    "java( ApplicationUtil.percentage(challenge.getSaved(), challenge.getTarget())"
                            + " )")
    ChallengeDTO toDTO(Challenge challenge);

    @Mapping(
            target = "completion",
            expression =
                    "java( ApplicationUtil.percentage(challengeDTO.getSaved(),"
                            + " challengeDTO.getTarget()) )")
    Challenge toEntity(ChallengeDTO challengeDTO);
}
