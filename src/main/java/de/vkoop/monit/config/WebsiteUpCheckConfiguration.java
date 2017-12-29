package de.vkoop.monit.config;

import de.vkoop.monit.checks.NamedHealthCheck;
import de.vkoop.monit.checks.impl.WebsiteUpTest;
import de.vkoop.monit.properties.AppProperties;
import org.apache.http.client.HttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(AppProperties.class)
@Configuration
public class WebsiteUpCheckConfiguration {

    @Bean
    public List<NamedHealthCheck> websiteChecks(AppProperties hostConfig, HttpClient httpClient) {
        List<AppProperties.WebsiteTestProperties> myhosts = hostConfig.websiteTests;
        if (myhosts == null) {
            return Collections.emptyList();
        }

        return myhosts.stream()
                .filter(conf -> !StringUtils.isEmpty(conf.url))
                .map(conf -> new WebsiteUpTest(conf.name, conf.url, httpClient))
                .collect(Collectors.toList());
    }
}
