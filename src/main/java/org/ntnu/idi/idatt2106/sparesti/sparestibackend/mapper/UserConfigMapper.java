package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;

/**
 * Mapper interface for converting back and forth
 * from a DTO to a UserConfig entity.
 *
 * @author Yasin M.
 * @version 1.0
 * @since 19.4.24
 */
@Mapper(
        uses = {ChallengeConfigMapper.class},
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserConfigMapper {

    /**
     * Implementation instance of the mapper
     */
    UserConfigMapper INSTANCE = Mappers.getMapper(UserConfigMapper.class);

    /**
     * Maps to DTO from userConfig entity
     * @param userConfig UserConfig entity
     * @return Mapped DTO
     */
    UserConfigDTO toDTO(UserConfig userConfig);

    /**
     * Maps to entity from UserConfig DTO
     * @param request UserConfig DTO
     * @return Mapped entity
     */
    UserConfig toEntity(UserConfigDTO request);
}
