package org.ntnu.idi.idatt2106.sparesti.sparestibackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
class SpareStiBackendApplicationTests {

    @Test
    void contextLoads() {}
}
