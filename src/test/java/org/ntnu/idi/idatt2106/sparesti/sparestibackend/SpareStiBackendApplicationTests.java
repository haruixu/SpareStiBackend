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
            new EnvironmentVariablesRule("SYSTEM_KEY", "32fa266d3e3e7e22167a7da202a1be8967e762cbd0ff0bebeb0fce28a49dc4d5");

    @Test
    void contextLoads() {}
}
