package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.BadgeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Badge;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BadgeMapper {

    BadgeMapper INSTANCE = Mappers.getMapper(BadgeMapper.class);

    BadgeDTO toDTO(Badge badge);
}
