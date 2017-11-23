package de.vkoop.monit.config;

import com.codahale.metrics.health.HealthCheckRegistry;
import de.vkoop.monit.checks.NamedHealthCheck;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class HealthConfiguration {
    private HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    @Bean
    public List<NamedHealthCheck> allChecks(List<NamedHealthCheck> pingChecks, List<NamedHealthCheck> portPingChecks) {
        ArrayList<NamedHealthCheck> healthChecks = new ArrayList<>();
        healthChecks.addAll(pingChecks);
        healthChecks.addAll(portPingChecks);

        return healthChecks;
    }

    @Bean
    public HealthCheckRegistry getHealthCheckRegistry(List<NamedHealthCheck> allChecks) {
        for (NamedHealthCheck pingCheck : allChecks) {
            healthCheckRegistry.register(pingCheck.getName(), pingCheck);
        }

        return healthCheckRegistry;
    }
}
