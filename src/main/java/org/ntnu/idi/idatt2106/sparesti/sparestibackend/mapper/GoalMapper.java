package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalResponseDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ApplicationUtil;

@Mapper(imports = ApplicationUtil.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface GoalMapper {

    GoalMapper INSTANCE = Mappers.getMapper(GoalMapper.class);

    // to response dto
    @Mapping(
            target = "completion",
            expression = "java( ApplicationUtil.percent(goal.getSaved(), goal.getTarget()) )")
    GoalResponseDTO toDTO(Goal goal);

    // from create or update dto
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createdOn", ignore = true),
        @Mapping(target = "completedOn", ignore = true),
        @Mapping(target = "priority", ignore = true),
        @Mapping(
                target = "completion",
                expression = "java( ApplicationUtil.percent(goalDTO.saved(), goalDTO.target()) )")
    })
    Goal toEntity(GoalCreateDTO goalDTO, User user);

    // from update dto
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "user", ignore = true),
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "completion", ignore = true),
        @Mapping(target = "createdOn", ignore = true),
        @Mapping(target = "priority", ignore = true),
        @Mapping(target = "completedOn", ignore = true),
    })
    Goal updateEntity(@MappingTarget Goal goal, GoalUpdateDTO goalDTO);
}
