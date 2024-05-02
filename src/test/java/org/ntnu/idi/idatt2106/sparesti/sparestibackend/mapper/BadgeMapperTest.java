package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.BadgeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Badge;

public class BadgeMapperTest {

    @Test
    public void testBadgeMapper() {
        Badge badge = new Badge();
        badge.setTitle("Title");
        badge.setDescription("Description");
        BadgeDTO badgeDTO = BadgeMapper.INSTANCE.toDTO(badge);

        assertEquals(badge.getTitle(), badgeDTO.title());
        assertEquals(badge.getDescription(), badgeDTO.description());

        assertNull(BadgeMapper.INSTANCE.toDTO(null));
    }
}
