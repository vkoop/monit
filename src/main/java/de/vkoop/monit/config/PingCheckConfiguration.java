package de.vkoop.monit.config;

import de.vkoop.monit.checks.NamedHealthCheck;
import de.vkoop.monit.checks.PingCheck;
import de.vkoop.monit.properties.AppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(AppProperties.class)
@Configuration
public class PingCheckConfiguration {

    @Bean
    public List<NamedHealthCheck> pingChecks(AppProperties hostConfig, PingCheck.PingCommandStrategy pingCommandStrategy) {
        List<AppProperties.CheckTargetProperties> myhosts = hostConfig.hosts;
        if (myhosts == null) {
            return Collections.emptyList();
        }

        return myhosts.stream()
                .filter(conf -> conf.port == 0)
                .map(conf -> new PingCheck(conf.ip, pingCommandStrategy, conf.name))
                .collect(Collectors.toList());
    }
}
