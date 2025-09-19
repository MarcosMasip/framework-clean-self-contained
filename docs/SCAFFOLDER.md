# Scaffolder

The DynamiaTools scaffolder generates starter applications or modules without relying on external services like *start.spring.io*.

## Commands
```bash
./dynamia new-app MyApp --group com.example --package com.example.myapp
./dynamia new-module inventory --package com.example.myapp.inventory
```

## Generated Structure (new-app)
```
apps/my-app/
  pom.xml
  src/main/java/<basePackage>/MyAppApplication.java
  src/main/java/<basePackage>/Contact.java
  src/main/java/<basePackage>/ContactModuleProvider.java
  src/main/resources/application.yml
  src/main/resources/META-INF/descriptors/ContactForm.yml
  src/main/resources/META-INF/descriptors/ContactTable.yml
```

## Tokens
Templates reside in `starter/src/main/resources/scaffolder/templates` using `__TOKEN__` syntax.

| Token | Meaning |
|-------|---------|
| `__GROUP_ID__` | Maven groupId |
| `__ARTIFACT_ID__` | Derived artifact id |
| `__APP_NAME__` | Human readable name |
| `__BASE_PACKAGE__` | Java base package |
| `__MAIN_CLASS__` | Main application class name |

## Customization
Edit or add templates, then re-run scaffold command. Existing files are overwritten only if path is regenerated explicitly.
