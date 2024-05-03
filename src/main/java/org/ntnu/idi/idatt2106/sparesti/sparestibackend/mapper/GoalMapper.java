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

/**
 * Mapper interface for converting back and forth
 * from a DTO to a Goal entity.
 *
 * @author Yasin M and Harry X.
 * @version 1.0
 * @since 18.4.24
 */
@Mapper(imports = ApplicationUtil.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface GoalMapper {

    /**
     * Implementation instance of the mapper
     */
    GoalMapper INSTANCE = Mappers.getMapper(GoalMapper.class);

    /**
     *  Maps Goal entity to DTO
     * @param goal Goal entity
     * @return Mapped DTO
     */
    @Mapping(
            target = "completion",
            expression = "java( ApplicationUtil.percent(goal.getSaved(), goal.getTarget()) )")
    GoalResponseDTO toDTO(Goal goal);

    /**
     * Maps DTO to Goal entity
     * @param goalDTO Goal DTO
     * @param user User who owns the goal
     * @return Mapped entity
     */
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

    /**
     * Updates Goal entity based on DTO
     * @param goal Goal entity that is updated
     * @param goalDTO Goal DTO with new changes
     * @return Updated goal entity
     */
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
