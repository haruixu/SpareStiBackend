package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.GoalDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ApplicationUtil;

@Mapper(componentModel = "spring", imports = ApplicationUtil.class)
public interface GoalMapper {

    GoalMapper INSTANCE = Mappers.getMapper(GoalMapper.class);

    @Mapping(
            target = "completion",
            expression = "java( ApplicationUtil.percentage(goal.getSaved(), goal.getTarget()) )")
    GoalDTO toDTO(Goal goal);

    @Mapping(
            target = "completion",
            expression =
                    "java( ApplicationUtil.percentage(goalDTO.getSaved(), goalDTO.getTarget()) )")
    Goal toEntity(GoalDTO goalDTO);
}
