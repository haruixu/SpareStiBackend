package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeTypeConfig;

/**
 * Mapper interface for converting back and forth
 * from a DTO to a Challenge entity.
 *
 * @author Yasin 19.4.24
 * @version 1.0
 * @since 19.4.24
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ChallengeTypeConfigMapper {

    /**
     * Implementation instance of the mapper
     */
    ChallengeTypeConfigMapper INSTANCE = Mappers.getMapper(ChallengeTypeConfigMapper.class);

    /**
     * Maps to DTO from ChallengeTypeConfig entity
     * @param challengeTypeConfig Type config entity
     * @return Converted DTO
     */
    ChallengeTypeConfigDTO toDTO(ChallengeTypeConfig challengeTypeConfig);

    /**
     * Converts ChallengeTypeConfigDTO to entity
     * @param challengeTypeConfigDTO DTO that is converted
     * @return Converted entity
     */
    @Mapping(target = "type", source = "type", qualifiedByName = "getType")
    ChallengeTypeConfig toEntity(ChallengeTypeConfigDTO challengeTypeConfigDTO);

    /**
     * Updates entity based on DTO
     * @param challengeTypeConfig Updated entity
     * @param challengeTypeConfigDTO DTO with new changes
     * @return Updated entity
     */
    ChallengeTypeConfig updateEntity(
            @MappingTarget ChallengeTypeConfig challengeTypeConfig,
            ChallengeTypeConfigDTO challengeTypeConfigDTO);

    /**
     * Formats the type to capitalized first letter only
     * @param type Unformatted type
     * @return Formatted type
     */
    @Named(value = "getType")
    default String getType(String type) {
        return type.trim().length() > 1
                ? type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase()
                : type.toUpperCase();
    }
}
