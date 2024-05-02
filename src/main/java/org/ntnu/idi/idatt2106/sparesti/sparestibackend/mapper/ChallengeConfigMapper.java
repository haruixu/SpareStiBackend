package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeConfig;

/**
 * Mapper interface for converting back and forth
 * from DTO to entity for Challenge entity
 *
 * @author Yasin M.
 * @version 1.0
 * @since 19.4.2024
 */
@Mapper(
        uses = {ChallengeTypeConfigMapper.class},
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ChallengeConfigMapper {

    /**
     * Implementation instance of the mapper
     */
    ChallengeConfigMapper INSTANCE = Mappers.getMapper(ChallengeConfigMapper.class);

    /**
     * Converts to DTO from ChallengeConfig entity to dto
     * @param challengeConfig Challenge config entity
     * @return Mapped DTO
     */
    ChallengeConfigDTO toDTO(ChallengeConfig challengeConfig);

    /**
     * Converts a ChallengeConfigDTO to an entity
     * @param challengeConfigDTO DTO that is mapped
     * @return Converted entity
     */
    ChallengeConfig toEntity(ChallengeConfigDTO challengeConfigDTO);

    /**
     * Cascades changes from a ChallengeConfigDTO to target entity.
     * @param challengeConfig Entity that is updated
     * @param challengeConfigDTO DTO that contains the changes
     * @return Updated entity
     */
    ChallengeConfig updateEntity(
            @MappingTarget ChallengeConfig challengeConfig, ChallengeConfigDTO challengeConfigDTO);
}
