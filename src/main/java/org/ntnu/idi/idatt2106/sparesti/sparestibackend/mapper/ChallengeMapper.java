package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ApplicationUtil;

@Mapper(imports = ApplicationUtil.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ChallengeMapper {

    ChallengeMapper INSTANCE = Mappers.getMapper(ChallengeMapper.class);

    @Mapping(
            target = "completion",
            expression =
                    "java(ApplicationUtil.percent(challenge.getSaved(), challenge.getTarget()))")
    ChallengeDTO toDTO(Challenge challenge);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createdOn", ignore = true),
        @Mapping(target = "completedOn", ignore = true),
        @Mapping(
                target = "completion",
                expression =
                        "java(ApplicationUtil.percent(challengeCreateDTO.saved(),"
                                + " challengeCreateDTO.target()))"),
        @Mapping(target = "type", source = "challengeCreateDTO.type", qualifiedByName = "getType")
    })
    Challenge toEntity(ChallengeCreateDTO challengeCreateDTO, User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "user", ignore = true),
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createdOn", ignore = true),
        @Mapping(target = "completedOn", ignore = true),
        @Mapping(
                target = "completion",
                expression =
                        "java(ApplicationUtil.percent(challengeDTO.saved(),challengeDTO.target()))"),
        @Mapping(target = "type", source = "challengeDTO.type", qualifiedByName = "getType")
    })
    Challenge updateEntity(@MappingTarget Challenge challenge, ChallengeUpdateDTO challengeDTO);

    @Named(value = "getType")
    default String getType(String type) {
        if (type == null) return null;

        return type.trim().length() > 1
                ? type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase()
                : type.toUpperCase();
    }

    @AfterMapping
    default void updateType(@MappingTarget Challenge challenge, ChallengeUpdateDTO challengeDTO) {
        if (challengeDTO.type() == null) {
            challenge.setType(null);
        }
    }
}
