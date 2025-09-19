package tools.dynamia.dist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Minimal Spring Boot entry point bundling DynamiaTools core modules into a single executable jar.
 */
@SpringBootApplication(scanBasePackages = "tools.dynamia")
public class DynamiaDistApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamiaDistApplication.class, args);
    }
}
