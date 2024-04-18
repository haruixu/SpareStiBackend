package org.ntnu.idi.idatt2106.sparesti.sparestibackend;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import uk.org.webcompere.systemstubs.rules.EnvironmentVariablesRule;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
class SpareStiBackendApplicationTests {

    @Rule
    public EnvironmentVariablesRule environmentVariablesRule =
            new EnvironmentVariablesRule("SYSTEM_KEY", "12345");

    @Test
    void contextLoads() {}
}
