package __BASE_PACKAGE__;

import org.springframework.stereotype.Component;
import tools.dynamia.crud.CrudPage;
import tools.dynamia.navigation.Module;
import tools.dynamia.navigation.ModuleProvider;

@Component
public class ContactModuleProvider implements ModuleProvider {
    @Override
    public Module getModule() {
        Module module = new Module("contacts", "Contacts");
        module.addPage(new CrudPage("contacts", "Contacts", Contact.class));
        return module;
    }
}
