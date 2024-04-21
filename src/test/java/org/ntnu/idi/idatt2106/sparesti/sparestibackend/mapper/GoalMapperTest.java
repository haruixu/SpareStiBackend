package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.GoalDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal;

public class GoalMapperTest {

    @Test
    public void toDTOTest() {

        String title = "title";
        BigDecimal saved = new BigDecimal(0);
        BigDecimal target = new BigDecimal(2);
        BigDecimal completion = new BigDecimal(0);
        ZonedDateTime createdOn = ZonedDateTime.now();
        String description = "description";

        Goal goal = new Goal(title, saved, target, description, 1L, createdOn, completion);
        GoalDTO dto = GoalMapper.INSTANCE.toDTO(goal);
        assertEquals(title, dto.getTitle());
        assertEquals(saved, dto.getSaved());
        assertEquals(target, dto.getTarget());
        assertEquals(completion, dto.getCompletion());
        assertEquals(description, dto.getDescription());
        assertEquals(createdOn, goal.getCreatedOn());
        assertEquals(1L, goal.getPriority().longValue());
    }

    @Test
    public void toEntity() {

        String title = "title";
        BigDecimal saved = new BigDecimal(0);
        BigDecimal target = new BigDecimal(2);
        BigDecimal completion = new BigDecimal(0);
        ZonedDateTime createdOn = ZonedDateTime.now();
        String description = "description";

        GoalDTO dto = new GoalDTO(title, saved, target, completion, description, 1L, createdOn);
        Goal goal = GoalMapper.INSTANCE.toEntity(dto);
        assertEquals(title, goal.getTitle());
        assertEquals(saved, goal.getSaved());
        assertEquals(target, goal.getTarget());
        assertEquals(completion, goal.getCompletion());
        assertEquals(description, goal.getDescription());
        assertEquals(createdOn, goal.getCreatedOn());
        assertEquals(1L, goal.getPriority().longValue());
    }
}
