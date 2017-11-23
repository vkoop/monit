package de.vkoop.monit.config;

import de.vkoop.monit.checks.NamedHealthCheck;
import de.vkoop.monit.checks.PingCheck;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(MyConfigProperties.class)
@Configuration
public class PingCheckConfigs {

    @Bean
    public List<NamedHealthCheck> pingChecks(MyConfigProperties hostConfig, PingCheck.PingCommandStrategy pingCommandStrategy) {
        return hostConfig.myhosts.stream()
                .filter(conf -> conf.port == 0)
                .map(conf -> new PingCheck(conf.ip, pingCommandStrategy, conf.name))
                .collect(Collectors.toList());
    }
}
