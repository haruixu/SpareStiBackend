package org.ntnu.idi.idatt2106.sparesti.sparestibackend;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
class SpareStiBackendApplicationTests {

    static {
        System.setProperty("SECRET_EY", "foo");
    }

    @Test
    void contextLoads() {}
}
