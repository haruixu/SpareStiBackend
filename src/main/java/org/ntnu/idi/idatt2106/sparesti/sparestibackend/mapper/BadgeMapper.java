package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.BadgeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Badge;

/**
 * Mapper interface for converting back and forth from the Badge entity to its DTO's in addition to cascading updates from dto's onto the entity object
 * from a DTO to the entity.
 *
 * @author Yasin M.
 * @version 1.0
 * @since 20.4.24
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BadgeMapper {

    /**
     * Implementation instance of the interface
     */
    BadgeMapper INSTANCE = Mappers.getMapper(BadgeMapper.class);

    /**
     * Converts badge entity to a dto
     * @param badge Badge entity
     * @return Badge entity
     */
    BadgeDTO toDTO(Badge badge);
}
