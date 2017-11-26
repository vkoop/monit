package de.vkoop.monit.config;

import de.vkoop.monit.checks.NamedHealthCheck;
import de.vkoop.monit.checks.PortCheck;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(MyConfigProperties.class)
@Configuration
public class PortPingCheckConfigs {

    @Bean
    public List<NamedHealthCheck> portPingChecks(MyConfigProperties hostConfig) {
        return hostConfig.myhosts.stream()
                .filter(conf -> conf.port != 0)
                .map(conf -> new PortCheck(conf.port, conf.ip, conf.name))
                .collect(Collectors.toList());
    }
}