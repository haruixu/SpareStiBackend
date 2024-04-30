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
        @Mapping(
                target = "type",
                expression =
                        "java(challengeCreateDTO.type() != null ?"
                                + " (challengeCreateDTO.type().substring(0,1).toUpperCase() +"
                                + " challengeCreateDTO.type().substring(1).toLowerCase()).trim() :"
                                + " null)")
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
        @Mapping(
                target = "type",
                expression =
                        "java(challengeDTO.type() != null ?"
                                + " (challengeDTO.type().substring(0,1).toUpperCase() +"
                                + " challengeDTO.type().substring(1).toLowerCase()).trim() : null)")
    })
    Challenge updateEntity(@MappingTarget Challenge challenge, ChallengeUpdateDTO challengeDTO);
}
