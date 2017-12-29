package de.vkoop.monit.config;

import de.vkoop.monit.checks.NamedHealthCheck;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class HealthConfiguration {

    @Bean
    public List<NamedHealthCheck> allChecks(List<NamedHealthCheck> pingChecks, List<NamedHealthCheck> portPingChecks, List<NamedHealthCheck> websiteChecks) {
        ArrayList<NamedHealthCheck> healthChecks = new ArrayList<>();
        healthChecks.addAll(pingChecks);
        healthChecks.addAll(portPingChecks);
        healthChecks.addAll(websiteChecks);

        return healthChecks;
    }

}
