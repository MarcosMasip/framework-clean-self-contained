package tools.dynamia.dist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Simple smoke test ensuring the distribution application context loads with all auto-configured beans.
 */
@SpringBootTest
class DistApplicationSmokeTest {

    @Test
    void contextLoads() {
        // If the context fails to start, this test will fail.
    }
}
