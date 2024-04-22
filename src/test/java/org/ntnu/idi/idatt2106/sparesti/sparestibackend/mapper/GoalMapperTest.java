package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.Before;
import org.junit.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.GoalDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

public class GoalMapperTest {

    private User user;

    @Before
    public void setUp() {
        user =
                new User(
                        1L,
                        "testFirstName",
                        "testLastName",
                        "testUsername",
                        "testPassword123!",
                        "testEmail@test.com",
                        new UserConfig(Role.USER, null));
    }

    @Test
    public void toDTOTest() {

        String title = "title";
        BigDecimal saved = new BigDecimal(0);
        BigDecimal target = new BigDecimal(2);
        BigDecimal completion = new BigDecimal(0);
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime due = ZonedDateTime.now();
        String description = "description";

        Goal goal =
                new Goal(
                        1L,
                        title,
                        saved,
                        target,
                        description,
                        1L,
                        createdOn,
                        due,
                        completion,
                        user);
        GoalDTO dto = GoalMapper.INSTANCE.toDTO(goal);
        assertEquals(title, dto.getTitle());
        assertEquals(saved, dto.getSaved());
        assertEquals(target, dto.getTarget());
        assertEquals(completion, dto.getCompletion());
        assertEquals(description, dto.getDescription());
        assertEquals(createdOn, goal.getCreatedOn());
        assertEquals(due, goal.getDue());
        assertEquals(1L, goal.getPriority().longValue());
        assertEquals(user, goal.getUser());
    }

    @Test
    public void toEntity() {

        String title = "title";
        BigDecimal saved = new BigDecimal(0);
        BigDecimal target = new BigDecimal(2);
        BigDecimal completion = new BigDecimal(0);
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime due = ZonedDateTime.now();
        String description = "description";

        GoalDTO dto =
                new GoalDTO(1L, title, saved, target, completion, description, 1L, createdOn, due);
        Goal goal = GoalMapper.INSTANCE.toEntity(dto, user);
        assertEquals(title, goal.getTitle());
        assertEquals(saved, goal.getSaved());
        assertEquals(target, goal.getTarget());
        assertEquals(completion, goal.getCompletion());
        assertEquals(description, goal.getDescription());
        assertEquals(createdOn, goal.getCreatedOn());
        assertEquals(due, goal.getDue());
        assertEquals(1L, goal.getPriority().longValue());
    }
}
