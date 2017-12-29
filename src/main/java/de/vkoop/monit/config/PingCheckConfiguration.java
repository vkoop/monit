package de.vkoop.monit.config;

import de.vkoop.monit.checks.NamedHealthCheck;
import de.vkoop.monit.checks.impl.PingCheck;
import de.vkoop.monit.properties.AppProperties;
import io.vavr.collection.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@EnableConfigurationProperties(AppProperties.class)
@Configuration
public class PingCheckConfiguration {

    @Bean
    public List<NamedHealthCheck> pingChecks(AppProperties hostConfig, PingCheck.PingCommandStrategy pingCommandStrategy) {
        return List.ofAll(hostConfig.hosts)
                .filter(conf -> conf.port == 0)
                .map(conf -> new PingCheck(conf.ip, pingCommandStrategy, conf.name));
    }
}
