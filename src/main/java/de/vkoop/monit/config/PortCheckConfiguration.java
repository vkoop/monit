package de.vkoop.monit.config;

import de.vkoop.monit.checks.NamedHealthCheck;
import de.vkoop.monit.checks.PortCheck;
import de.vkoop.monit.properties.AppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(AppProperties.class)
@Configuration
public class PortCheckConfiguration {

    @Bean
    public List<NamedHealthCheck> portPingChecks(AppProperties hostConfig) {
        return hostConfig.myhosts.stream()
                .filter(conf -> conf.port != 0)
                .map(conf -> new PortCheck(conf.port, conf.ip, conf.name))
                .collect(Collectors.toList());
    }
}
