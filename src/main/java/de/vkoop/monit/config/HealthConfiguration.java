package de.vkoop.monit.config;

import de.vkoop.monit.checks.NamedHealthCheck;
import de.vkoop.monit.checks.result.HealthCheck;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HealthConfiguration {

    @Bean
    public List<NamedHealthCheck> allChecks(List<NamedHealthCheck> pingChecks, List<NamedHealthCheck> portPingChecks, List<NamedHealthCheck> websiteChecks) {
        return pingChecks
                .appendAll(portPingChecks)
                .appendAll(websiteChecks);
    }

    @Bean
    public Map<String, HealthCheck> healthChecks(List<NamedHealthCheck> allChecks) {
        return allChecks
                .toMap(NamedHealthCheck::getName, check -> check);
    }
}
