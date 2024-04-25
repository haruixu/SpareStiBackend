package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalResponseDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Account;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

public class GoalMapperTest {

    private User user;

    private Goal goal;

    @BeforeEach
    public void setUp() {
        // User
        user =
                new User(
                        1L,
                        "testFirstName",
                        "testLastName",
                        "testUsername",
                        "testPassword123!",
                        "testEmail@test.com",
                        new UserConfig(Role.USER, null),
                        new Account(),
                        new Account());

        // Goal
        String title = "title";
        BigDecimal saved = new BigDecimal(0);
        BigDecimal target = new BigDecimal(2);
        BigDecimal completion = new BigDecimal("0.000");
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime completedOn = ZonedDateTime.now();
        ZonedDateTime due = ZonedDateTime.now();
        String description = "description";

        goal =
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
                        completedOn,
                        user);
    }

    @Test
    public void toDTOTest() {
        GoalResponseDTO dto = GoalMapper.INSTANCE.toDTO(goal);
        assertEquals(goal.getTitle(), dto.title());
        assertEquals(goal.getSaved(), dto.saved());
        assertEquals(goal.getTarget(), dto.target());
        assertEquals(goal.getCompletion(), dto.completion());
        assertEquals(goal.getDescription(), dto.description());
        assertEquals(goal.getCreatedOn(), dto.createdOn());
        assertEquals(goal.getDue(), dto.due());
        assertEquals(goal.getPriority().longValue(), dto.priority().longValue());
    }

    @Test
    public void toEntity() {

        String title = "title";
        BigDecimal saved = new BigDecimal(1);
        BigDecimal target = new BigDecimal(1000);
        BigDecimal completion = new BigDecimal("0.100");
        ZonedDateTime due = ZonedDateTime.now();
        String description = "description";

        GoalCreateDTO dto = new GoalCreateDTO(title, saved, target, description, due);
        Goal goal = GoalMapper.INSTANCE.toEntity(dto, user);
        assertEquals(title, goal.getTitle());
        assertEquals(saved, goal.getSaved());
        assertEquals(target, goal.getTarget());
        assertEquals(completion, goal.getCompletion());
        assertEquals(description, goal.getDescription());
        assertEquals(due, goal.getDue());
        assertEquals(user, goal.getUser());
    }

    @Test
    public void testUpdateEntityWithValidParameters() {
        String title = "newTitle";
        BigDecimal saved = new BigDecimal(1);
        BigDecimal target = new BigDecimal(1);
        String description = "newDescription";
        ZonedDateTime due = ZonedDateTime.now();

        GoalUpdateDTO dto = new GoalUpdateDTO(title, saved, target, description, due);
        Goal updatedGoal = GoalMapper.INSTANCE.updateEntity(goal, dto);

        assertEquals(title, updatedGoal.getTitle());
        assertEquals(saved, updatedGoal.getSaved());
        assertEquals(target, updatedGoal.getTarget());
        assertEquals(description, updatedGoal.getDescription());
        assertEquals(due, updatedGoal.getDue());
    }

    @Test
    public void testUpdateEntityWithNullParameters() {
        GoalUpdateDTO dto = new GoalUpdateDTO(null, null, null, null, null);
        Goal updatedGoal = GoalMapper.INSTANCE.updateEntity(goal, dto);

        assertEquals(goal.getTitle(), updatedGoal.getTitle());
        assertEquals(goal.getSaved(), updatedGoal.getSaved());
        assertEquals(goal.getTarget(), updatedGoal.getTarget());
        assertEquals(goal.getDescription(), updatedGoal.getDescription());
        assertEquals(goal.getDue(), updatedGoal.getDue());
    }
}
