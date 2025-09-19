package tools.dynamia.dist;

import org.springframework.stereotype.Component;
import tools.dynamia.navigation.Module;
import tools.dynamia.navigation.ModuleProvider;
import tools.dynamia.navigation.Page;

/**
 * Provides a tiny built-in module so the distribution JAR is not visually blank on first run.
 * <p>
 * It registers a single "Welcome" page pointing to a static HTML resource located in the classpath
 * under <code>static/welcome.html</code>. The page is marked as {@link Page#main()} and featured so
 * navigation UIs can highlight or auto-select it if they implement such behavior.
 * </p>
 */
@Component
public class HomeModuleProvider implements ModuleProvider {

    @Override
    public Module getModule() {
        Module module = new Module("home", "Home");
        Page welcome = new Page("welcome", "Welcome", "/welcome.html");
        welcome.main().featured(1).setClosable(false);
        module.addPage(welcome);
        return module;
    }
}
