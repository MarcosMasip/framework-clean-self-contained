package __BASE_PACKAGE__;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tools.dynamia.app.EnableDynamiaTools;

@SpringBootApplication
@EnableDynamiaTools
public class __MAIN_CLASS__ {
    public static void main(String[] args) {
        SpringApplication.run(__MAIN_CLASS__.class, args);
    }
}
