package tools.dynamia.scaffold;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Simple CLI scaffolder for DynamiaTools applications and modules.
 * <p>
 * This class intentionally avoids external template engines to remain
 * lightweight and offline friendly. It performs plain token replacement
 * on template resources located under:
 * <pre>starter/src/main/resources/scaffolder/templates</pre>
 * Tokens use the form {@code __TOKEN__}.
 * <p>
 * Usage examples:
 * <pre>{@code
 * java -cp target/dynamia-tools-starter-*.jar tools.dynamia.scaffold.DynamiaScaffolder \
 *      new-app MyApp --group com.example --package com.example.myapp
 *
 * java -cp ... tools.dynamia.scaffold.DynamiaScaffolder new-module inventory --package com.example.myapp
 * }</pre>
 * Generated applications are placed in {@code apps/<artifactId>} by default.
 */
public class DynamiaScaffolder {

    private static final String TEMPLATE_ROOT = "/scaffolder/templates";

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
            printHelp();
            return;
        }

        String command = args[0];
        List<String> rest = Arrays.asList(args).subList(1, args.length);
        switch (command) {
            case "new-app" -> newApp(rest);
            case "new-module" -> newModule(rest);
            default -> {
                System.err.println("Unknown command: " + command);
                printHelp();
            }
        }
    }

    private static void printHelp() {
        System.out.println("DynamiaTools Scaffolder CLI\n" +
                "Commands:\n" +
                "  new-app <Name> [--group <groupId>] [--package <basePackage>] [--dir <targetDir>]\n" +
                "  new-module <name> [--package <basePackage>] [--dir <modulesDir>]\n" +
                "Examples:\n" +
                "  new-app MyApp --group com.example --package com.example.demo\n" +
                "  new-module inventory --package com.example.demo\n");
    }

    /* ===================== new-app ===================== */
    private static void newApp(List<String> args) throws IOException {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("new-app requires a name");
        }
        String name = args.get(0);
        Map<String, String> opts = parseOpts(args.subList(1, args.size()));
        String group = opts.getOrDefault("group", "demo");
        String basePackage = opts.getOrDefault("package", group + "." + name.toLowerCase());
        String artifactId = nameToArtifact(name);
        String mainClass = sanitizeClassName(name) + "Application";
        String targetDir = opts.getOrDefault("dir", "apps/" + artifactId);

        Path root = Path.of(targetDir).normalize();
        Files.createDirectories(root);
        System.out.println("[scaffold] Creating application at " + root.toAbsolutePath());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("GROUP_ID", group);
        tokens.put("ARTIFACT_ID", artifactId);
        tokens.put("APP_NAME", name);
        tokens.put("BASE_PACKAGE", basePackage);
        tokens.put("MAIN_CLASS", mainClass);
        tokens.put("YEAR", String.valueOf(LocalDateTime.now().getYear()));
        tokens.put("DESCRIPTION", "DynamiaTools generated application");

        // Java package path
        Path srcMainJava = root.resolve("src/main/java");
        Path packagePath = srcMainJava.resolve(basePackage.replace('.', '/'));
        Files.createDirectories(packagePath);

        // Resources path
        Path resources = root.resolve("src/main/resources");
        Files.createDirectories(resources);
        Files.createDirectories(resources.resolve("META-INF/descriptors"));

        // Basic files
        writeTemplate("pom.xml.tpl", root.resolve("pom.xml"), tokens);
        writeTemplate("Application.java.tpl", packagePath.resolve(mainClass + ".java"), tokens);
        writeTemplate("application.yml.tpl", resources.resolve("application.yml"), tokens);
        writeTemplate("gitignore.tpl", root.resolve(".gitignore"), tokens);

        // Demo entity and descriptors
        writeTemplate("Contact.java.tpl", packagePath.resolve("Contact.java"), tokens);
        Path descriptors = resources.resolve("META-INF/descriptors");
        writeTemplate("ContactForm.yml.tpl", descriptors.resolve("ContactForm.yml"), tokens);
        writeTemplate("ContactTable.yml.tpl", descriptors.resolve("ContactTable.yml"), tokens);
        writeTemplate("ModuleProvider.java.tpl", packagePath.resolve("ContactModuleProvider.java"), tokens);

        System.out.println("[scaffold] Application generated. Next steps:\n" +
                "  cd " + targetDir + "\n" +
                "  mvn spring-boot:run\n");
    }

    /* ===================== new-module ===================== */
    private static void newModule(List<String> args) throws IOException {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("new-module requires a name");
        }
        String name = args.get(0);
        Map<String, String> opts = parseOpts(args.subList(1, args.size()));
        String basePackage = opts.getOrDefault("package", "demo.module." + name.toLowerCase());
        String artifactId = nameToArtifact(name);
        String targetDir = opts.getOrDefault("dir", "modules/" + artifactId);
        Path root = Path.of(targetDir).normalize();
        Files.createDirectories(root);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("GROUP_ID", basePackage.substring(0, Math.max(basePackage.lastIndexOf('.'), 0))); // simplistic
        tokens.put("ARTIFACT_ID", artifactId);
        tokens.put("APP_NAME", name);
        tokens.put("BASE_PACKAGE", basePackage);
        tokens.put("MAIN_CLASS", sanitizeClassName(name) + "Module");
        tokens.put("YEAR", String.valueOf(LocalDateTime.now().getYear()));
        tokens.put("DESCRIPTION", "DynamiaTools generated module");

        Path srcMainJava = root.resolve("src/main/java");
        Path packagePath = srcMainJava.resolve(basePackage.replace('.', '/'));
        Files.createDirectories(packagePath);
        Path resources = root.resolve("src/main/resources");
        Files.createDirectories(resources);

        writeTemplate("pom.xml.tpl", root.resolve("pom.xml"), tokens);
        System.out.println("[scaffold] Module skeleton created at " + root.toAbsolutePath());
    }

    /* ===================== helpers ===================== */
    private static Map<String, String> parseOpts(List<String> list) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            String a = list.get(i);
            if (a.startsWith("--")) {
                String key = a.substring(2);
                String value = i + 1 < list.size() ? list.get(++i) : "";
                map.put(key, value);
            }
        }
        return map;
    }

    private static String nameToArtifact(String name) {
        return name.trim().replaceAll("[A-Z]", m -> "-" + m.group().toLowerCase())
                .replaceAll("^-", "")
                .toLowerCase().replaceAll("[^a-z0-9-]", "-");
    }

    private static String sanitizeClassName(String name) {
        String cleaned = name.replaceAll("[^A-Za-z0-9]", "");
        if (cleaned.isEmpty()) cleaned = "App";
        return Character.toUpperCase(cleaned.charAt(0)) + cleaned.substring(1);
    }

    private static void writeTemplate(String templateName, Path target, Map<String, String> tokens) throws IOException {
        String resourcePath = TEMPLATE_ROOT + "/" + templateName;
        try (InputStream is = DynamiaScaffolder.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Template not found: " + resourcePath);
            }
            String raw = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            String out = replaceTokens(raw, tokens);
            Files.createDirectories(target.getParent());
            Files.writeString(target, out, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    private static String replaceTokens(String content, Map<String, String> tokens) {
        String result = content;
        for (var e : tokens.entrySet()) {
            result = result.replace("__" + e.getKey() + "__", e.getValue());
        }
        return result;
    }
}
