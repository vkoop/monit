package de.vkoop.monit.config;

import de.vkoop.monit.checks.NamedHealthCheck;
import de.vkoop.monit.checks.impl.WebsiteUpCheck;
import de.vkoop.monit.properties.AppProperties;
import io.vavr.collection.List;
import org.apache.http.client.HttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@EnableConfigurationProperties(AppProperties.class)
@Configuration
public class WebsiteUpCheckConfiguration {

    @Bean
    public List<NamedHealthCheck> websiteChecks(AppProperties hostConfig, HttpClient httpClient) {
        return List.ofAll(hostConfig.websiteTests)
                .filter(conf -> !StringUtils.isEmpty(conf.url))
                .map(conf -> new WebsiteUpCheck(conf.name, conf.url, httpClient));
    }
}
