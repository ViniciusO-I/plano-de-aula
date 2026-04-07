package br.com.fiap.skill_hub.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class CustomInfoContributor implements InfoContributor {

    private final Environment environment;

    public CustomInfoContributor(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("runtime", Map.of(
                "javaVersion", System.getProperty("java.version"),
                "os", System.getProperty("os.name"),
                "activeProfiles", Arrays.asList(environment.getActiveProfiles()).isEmpty()
                        ? List.of("default")
                        : Arrays.asList(environment.getActiveProfiles())
        ));

        builder.withDetail("dependencies", Map.of(
                "database", "H2",
                "cache", "N/A",
                "messaging", "N/A"
        ));

        builder.withDetail("features", List.of(
                "JWT Authentication",
                "User Management",
                "Skill Tracking"
        ));
    }
}