package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeTypeConfig;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ChallengeTypeConfigMapper {

    ChallengeTypeConfigMapper INSTANCE = Mappers.getMapper(ChallengeTypeConfigMapper.class);

    ChallengeTypeConfigDTO toDTO(ChallengeTypeConfig challengeTypeConfig);

    @Mapping(
            target = "type",
            expression =
                    "java(challengeTypeConfigDTO.type()trim().length() > 1 ?"
                            + " challengeTypeConfigDTO.type().substring(0,1).toUpperCase() +"
                            + " challengeTypeConfigDTO.type().substring(1).toLowerCase() :"
                            + " null)")
    ChallengeTypeConfig toEntity(ChallengeTypeConfigDTO challengeTypeConfigDTO);

    ChallengeTypeConfig updateEntity(
            @MappingTarget ChallengeTypeConfig challengeTypeConfig,
            ChallengeTypeConfigDTO challengeTypeConfigDTO);
}
