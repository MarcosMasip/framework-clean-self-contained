package tools.dynamia.dist;

import org.springframework.context.annotation.Configuration;

/**
 * Minimal configuration providing an {@link ApplicationInfo} bean required by
 * several core Dynamia components when running the distribution jar.
 */
@Configuration
public class DistConfiguration {

    // No explicit ApplicationInfo bean here; RootAppConfiguration supplies the canonical one.
}
