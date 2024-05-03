package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ApplicationUtil;

/**
 * Mapper interface for converting back and forth
 * from a DTO to a Challenge entity.
 *
 * @author Yasin M.
 * @version 1.0
 * @since 18.4.2024
 */
@Mapper(imports = ApplicationUtil.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ChallengeMapper {

    /**
     * Implementation instance of the mapper
     */
    ChallengeMapper INSTANCE = Mappers.getMapper(ChallengeMapper.class);

    /**
     * Converts from Challenge entity to DTO. Calculates completion value as
     * percentage of saved amount to target amount
     * @param challenge Challenge entity
     * @return Converted DTO
     */
    @Mapping(
            target = "completion",
            expression =
                    "java(ApplicationUtil.percent(challenge.getSaved(), challenge.getTarget()))")
    ChallengeDTO toDTO(Challenge challenge);

    /**
     * Converts from challenge DTO to entity. Ignore null values. Calculates completion value as
     * percentage of saved amount to target amount. Type is capitalized only on first letter.
     * @param challengeCreateDTO Challenge DTO
     * @param user User who owns the challenge
     * @return Converted challenge entity
     */
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

    /**
     * Updates a challenge entity using dto. Ignore null values. Calculates completion value as
     * percentage of saved amount to target amount. Type is capitalized only on first letter.
     * @param challenge Challenge entity that is updated
     * @param challengeDTO DTO with new changes
     * @return Updated challenge entity
     */
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

    /**
     * Formats type of challenge to capitalized first letter
     * @param type Unformatted type
     * @return Formatted type
     */
    @Named(value = "getType")
    default String getType(String type) {
        if (type == null) return null;

        return type.trim().length() > 1
                ? type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase()
                : type.toUpperCase();
    }

    /**
     * Sets the type of challenge to null if DTO type is null
     * @param challenge Challenge entity
     * @param challengeDTO Challenge DTO
     */
    @AfterMapping
    default void updateType(@MappingTarget Challenge challenge, ChallengeUpdateDTO challengeDTO) {
        if (challengeDTO.type() == null) {
            challenge.setType(null);
        }
    }
}
