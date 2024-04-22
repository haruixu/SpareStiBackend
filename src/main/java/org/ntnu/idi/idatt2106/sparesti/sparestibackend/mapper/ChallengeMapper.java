package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ApplicationUtil;

@Mapper(
        componentModel = "spring",
        imports = ApplicationUtil.class,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ChallengeMapper {

    ChallengeMapper INSTANCE = Mappers.getMapper(ChallengeMapper.class);

    @Mapping(
            target = "completion",
            expression =
                    "java(ApplicationUtil.percent(challenge.getSaved(), challenge.getTarget()))")
    ChallengeDTO toDTO(Challenge challenge);

    @Mapping(
            target = "completion",
            expression =
                    "java(ApplicationUtil.percent(challengeDTO.getSaved(),challengeDTO.getTarget()))")
    @Mapping(target = "id", source = "challengeDTO.id")
    Challenge toEntity(ChallengeDTO challengeDTO, User user);

    @Mappings({
        @Mapping(target = "user", ignore = true),
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createdOn", ignore = true),
        @Mapping(target = "completedOn", ignore = true),
        @Mapping(
                target = "completion",
                expression =
                        "java(ApplicationUtil.percent(challengeDTO.getSaved(),challengeDTO.getTarget()))")
    })
    Challenge updateEntity(@MappingTarget Challenge challenge, ChallengeDTO challengeDTO);
}
